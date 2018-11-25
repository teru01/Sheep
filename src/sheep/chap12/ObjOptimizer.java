package sheep.chap12;
import java.util.*;
import static javassist.gluonj.GluonJ.revise;
import javassist.gluonj.*;
import sheep.*;
import sheep.ast.*;
import sheep.chap6.*;
import sheep.chap6.BasicEvaluator.ASTreeEx;
import sheep.chap7.FuncEvaluator.PrimaryEx;
import sheep.chap9.ClassEvaluator.ClassBodyEx;
import sheep.chap11.*;
import sheep.chap11.EnvOptimizer.ASTreeOptEx;
import sheep.chap11.EnvOptimizer.DefStmntEx;
import sheep.chap11.EnvOptimizer.EnvEx2;
import sheep.chap11.EnvOptimizer.ParamsEx;
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
                    this.initObject(ci, so, newEnv);
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
}
