package sheep.function;
import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.ast.ASTree;
import sheep.ast.Arguments;
import sheep.ast.ArrayRef;
import sheep.ast.DefStmnt;
import sheep.ast.Dot;
import sheep.ast.Function;
import sheep.ast.ParameterList;
import sheep.ast.Postfix;
import sheep.ast.PrimaryExpr;
import sheep.core.BasicEvaluator;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.BasicEvaluator.BlockEx;
import sheep.core.Environment;
import sheep.core.ReturnObject;
import sheep.object.SheepObject;
import sheep.object.SheepObject.AccessException;
import sheep.util.Statements;

@Require(BasicEvaluator.class)
@Reviser
public class FuncEvaluator {
    @Reviser
    public static interface EnvEx extends Environment {
        void putInCurrentEnv(String name, Object value);
        void putConst(String name, Object value);
        Environment where(String name);
        void setOuter(Environment e);
    }

    @Reviser
    public static class DefStmntEx extends DefStmnt {
        public DefStmntEx(List<ASTree> c) { super(c); }

        // Functionオブジェクトを作成し、関数名とオブジェクトを環境に追加する
        public Object eval(Environment env) {
            try {
                ((EnvEx)env).putInCurrentEnv(name(), new Function(parameters(), body(), env));
            } catch(SheepException e) {
                throw new SheepException(e.getMessage(), this);
            }
            return name();
        }
    }

    @Reviser
    public static class PrimaryEx extends PrimaryExpr {
        public PrimaryEx(List<ASTree> c) { super(c); }
        // 関数の名前を返す
        public ASTree operand() { return child(0); }

        public boolean hasPostfix(int nest) {
            return numChildren() - nest > 1;
        }

        //実引数列があればそれを返す
        public Postfix postfix(int nest) {
            return (Postfix)child(numChildren() - nest - 1);
        }

        // 関数呼び出しの式
        public Object eval(Environment env) {
            return evalSubExpr(env, 0);
        }

        // foo(2)(3)のような式呼び出しに対応している。
        public Object evalSubExpr(Environment env, int nest) {
            if(hasPostfix(nest)) {
                Object target = evalSubExpr(env, nest+1);
                return ((PostfixEx)postfix(nest)).eval(env, target);
            } else {
                return ((ASTreeEx)operand()).eval(env);
            }
        }

        @Override
        public Object assign(Object right, Environment env) {
            if(!hasPostfix(0)) {
                throw new SheepException("bad assign", this);
            }
            Object subExpr = evalSubExpr(env, 1);
            Postfix postfix = postfix(0);
            if(postfix instanceof Dot) {
                if(!(subExpr instanceof SheepObject)) {
                    throw new SheepException("bad assign", this);
                }
                return setField((SheepObject) subExpr, (Dot)postfix, right);
            } else if(postfix instanceof ArrayRef) {
                if(!(subExpr instanceof Object[])) {
                    throw new SheepException("bad array access", this);
                }
                ArrayRef aref = (ArrayRef)postfix;
                Object index = ((ASTreeEx)aref.index()).eval(env);
                if(!(index instanceof Integer)) {
                    throw new SheepException("bad array access", this);
                }
                ((Object[])subExpr)[(Integer)index] = right;
                return right;
            }
            throw new SheepException("bad assign", this);
        }

        protected Object setField(SheepObject obj, Dot expr, Object rvalue) {
            String name = expr.name();
            try {
                obj.write(name, rvalue);
                return rvalue;
            } catch(AccessException e) {
                throw new SheepException("bad member access " + location() + ": " + name);
            }
        }
    }

    @Reviser
    public static abstract class PostfixEx extends Postfix {
        public PostfixEx(List<ASTree> c) { super(c); }
        public abstract Object eval(Environment env, Object value);
    }

    @Reviser
    public static class ArgumentsEx extends Arguments {
        public ArgumentsEx(List<ASTree> c) { super(c); }
        /**
         * callerEnvを使って実引数を計算し、受け取った関数オブジェクトのnewEnvに仮引数として追加する。
         * @param callerEnv 呼び出し元の環境オブジェクト
         * @param value     呼び出す関数オブジェクト
         * @return　関数の返り値
         */
        public Object eval(Environment callerEnv, Object value) {
            if(!(value instanceof Function)) {
                throw new SheepException("bad function", this);
            }
            Function func = (Function)value;
            ParameterList params = func.parameters();
            if(size() != params.size()) {
                throw new SheepException("bad number of arguments", this);
            }
            Environment newEnv = func.makeEnv();
            int num = 0;
            // 実引数の計算（呼び出し元の環境を用いる）
            for(ASTree a: this) {
                ((ParamsEx)params).eval(newEnv, num++, ((ASTreeEx)a).eval(callerEnv));
            }
            Object result = ((BlockEx)func.body()).eval(newEnv);
            if(result instanceof ReturnObject) {
                return ((ReturnObject)result).getValue();
            } else if(result == Statements.BREAK || result == Statements.CONTINUE) {
                throw new SheepException("This statement is not permitted here", this);
            }
            return null;
        }
    }

    @Reviser
    public static class ParamsEx extends ParameterList {
        public ParamsEx(List<ASTree> c) { super(c); }
        // 仮引数の計算（代入）。関数ブロック内の環境に追加する
        public void eval(Environment env, int index, Object value) {
            try {
                ((EnvEx)env).putInCurrentEnv(name(index), value);
            } catch(SheepException e) {
                throw new SheepException(e.getMessage(), this);
            }
        }
    }
}
