package sheep.chap12;
import java.util.*;
import static javassist.gluonj.GluonJ.revise;
import javassist.gluonj.*;
import sheep.*;
import sheep.ast.*;
import sheep.core.*;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.function.FuncEvaluator.PrimaryEx;
import sheep.object.ClassEvaluator.ClassBodyEx;
import sheep.optimizer.*;
import sheep.optimizer.EnvOptimizer.ASTreeOptEx;
import sheep.optimizer.EnvOptimizer.DefStmntEx;
import sheep.optimizer.EnvOptimizer.EnvEx2;
import sheep.optimizer.EnvOptimizer.ParamsEx;
import sheep.chap12.OptSheepObject.AccessException;


@Require(EnvOptimizer.class)
@Reviser
public class ObjOptimizer {
    @Reviser
    public static class ClassStmntEx extends ClassStmnt {
        public ClassStmntEx(List<ASTree> c) {
            super(c);
        }

        public void lookup(Symbols syms) {}

        public Object eval(Environment env) {
            // グローバルなシンボルを親にしてclass内部のメソッド用シンボルを作成する
            Symbols methodNames = new MemberSymbols(((EnvEx2)env).symbols(), MemberSymbols.METHOD);
            // メソッド用シンボルを親にしてフィールド用シンボルを作成
            Symbols fieldNames = new MemberSymbols(methodNames, MemberSymbols.FIELD);
            OptClassInfo ci = new OptClassInfo(this, env, methodNames, fieldNames);
            ((EnvEx2)env).put(name(), ci);
            ArrayList<DefStmnt> methods = new ArrayList<>();
            // スーパークラスのフィールド、メソッドのシンボルをコピーする。
            if(ci.superClass() != null) {
                ci.superClass().copyTo(fieldNames, methodNames, methods);
            }
            Symbols newSyms = new SymbolThis(fieldNames);
            ((ClassBodyEx)body()).lookup(newSyms, methodNames, fieldNames, methods);
            ci.setMethods(methods);
            return name();
        }
    }

    @Reviser
    public static class ClassBodyEx extends ClassBody {
        public ClassBodyEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            for(ASTree t: this) {
                if(!(t instanceof DefStmnt)) {
                    ((ASTreeEx)t).eval(env);
                }
            }
            return null;
        }

        public void lookup(Symbols syms, Symbols methodName, Symbols fieldNames, ArrayList<DefStmnt> methods) {
            for(ASTree t: this) {
                // メソッドの時は
                if(t instanceof DefStmnt) {
                    DefStmnt def = (DefStmnt)t;
                    int oldSize = methodName.size();
                    int i = methodName.putNew(def.name());
                    // 子クラスでの新たなメソッド
                    if(i >= oldSize) {
                        methods.add(def);
                    } else {
                        // オーバーライド
                        methods.set(i, def);
                    }
                    ((DefStmntEx2)def).lookupAsMethod(fieldNames);
                } else {
                    ((ASTreeOptEx)t).lookup(syms);
                }
            }
        }
    }

    @Reviser
    public static class DefStmntEx2 extends EnvOptimizer.DefStmntEx {
        public DefStmntEx2(List<ASTree> c) {
            super(c);
        }

        public int locals() {
            return this.size;
        }

        public void lookupAsMethod(Symbols syms) {
            Symbols newSyms = new Symbols(syms);
            newSyms.putNew(SymbolThis.NAME);
            ((ParamsEx)parameters()).lookup(newSyms);
            ((ASTreeOptEx)revise(body())).lookup(newSyms);
            this.size = newSyms.size();
        }
    }

    @Reviser
    public static class DotEx extends Dot {
        public DotEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env, Object value) {
            String member = name();
            if(value instanceof OptClassInfo) {
                if("new".equals(member)) {
                    OptClassInfo ci = (OptClassInfo)value;
                    ArrayEnv newEnv = new ArrayEnv(1, ci.environment());
                    OptSheepObject so = new OptSheepObject(ci, ci.size());
                    newEnv.put(0, 0, so);
                    initObject(ci, so, newEnv);
                    return so;
                }
            } else if(value instanceof OptSheepObject) {
                try {
                    return ((OptSheepObject)value).read(member);
                } catch(AccessException e){
                    return new AccessException("cannot access:" + member);
                }
            }
            throw new SheepException("bad member access:" + member, this);
        }

        protected void initObject(OptClassInfo ci, OptSheepObject obj, Environment env) {
            if(ci.superClass() != null) {
                initObject(ci.superClass(), obj, env);
            }
            ((ClassBodyEx)ci.body()).eval(env);
        }
    }

    @Reviser
    public static class NameEx2 extends EnvOptimizer.NameEx {
        public NameEx2(Token t) {
            super(t);
        }

        @Override
        public Object eval(Environment env) {
            if(this.index == UNKNOWN) {
                return env.get(name());
            } else if(this.nest == MemberSymbols.FIELD) {
                return getThis(env).read(this.index);
            } else if(this.nest == MemberSymbols.METHOD) {
                return getThis(env).method(this.index);
            } else {
                return ((EnvEx2)env).get(this.nest, this.index);
            }
        }

        @Override
        public void evalForAssign(Environment env, Object value) {
            if(this.index == UNKNOWN) {
                env.put(name(), value);
            } else if(this.nest == MemberSymbols.FIELD) {
                getThis(env).write(this.index, value);
            } else if(this.nest == MemberSymbols.METHOD) {
                throw new SheepException("cannot update a method: " + name(), this);
            } else {
                ((EnvEx2)env).put(this.nest, this.index, value);
            }
        }

        protected OptSheepObject getThis(Environment env) {
            return (OptSheepObject)((EnvEx2)env).get(0, 0);
        }
    }

    @Reviser
    public static class AssignEx extends BasicEvaluator.BinaryEx {
        public AssignEx(List<ASTree> c) {
            super(c);
        }

        @Override
        protected Object computeAssign(Environment env, Object rvalue) {
            ASTree le = left();
            if (!(le instanceof PrimaryExpr)) {
                return super.computeAssign(env, rvalue);
            }
            PrimaryEx p = (PrimaryEx) le;
            if (!(p.hasPostfix(0) && p.postfix(0) instanceof Dot)) {
                return super.computeAssign(env, rvalue);
            }
            Object t = ((PrimaryEx) le).evalSubExpr(env, 1);
            if (!(t instanceof OptSheepObject)) {
                return super.computeAssign(env, rvalue);
            }
            return setField((OptSheepObject) t, (Dot) p.postfix(0), rvalue);
        }

        protected Object setField(OptSheepObject obj, Dot expr, Object rvalue) {
            String name = expr.name();
            try {
                obj.write(name, rvalue);
                return rvalue;
            } catch (AccessException e) {
                throw new SheepException("bad member access: " + name, this);
            }
        }
    }
}
