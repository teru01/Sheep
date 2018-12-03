package sheep.chap7;
import java.util.List;
import javassist.gluonj.*;
import sheep.SheepException;
import sheep.ast.*;
import sheep.chap6.BasicEvaluator;
import sheep.chap6.Environment;
import sheep.chap6.BasicEvaluator.ASTreeEx;
import sheep.chap6.BasicEvaluator.BlockEx;

@Require(BasicEvaluator.class)
@Reviser
public class FuncEvaluator {
    @Reviser
    public static interface EnvEx extends Environment {
        void putNew(String name, Object value);
        Environment where(String name);
        void setOuter(Environment e);
        Environment getOutermostEnv();
    }

    @Reviser
    public static class DefStmntEx extends DefStmnt {
        public DefStmntEx(List<ASTree> c) { super(c); }
        // Functionオブジェクトを作成し、関数名とオブジェクトを環境に追加する
        public Object eval(Environment env) {
            ((EnvEx)env).putNew(name(), new Function(this.parameters(), this.body(), env));
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
            return ((BlockEx)func.body()).eval(newEnv);
        }
    }

    @Reviser
    public static class ParamsEx extends ParameterList {
        public ParamsEx(List<ASTree> c) { super(c); }
        // 仮引数の計算（代入）。関数ブロック内の環境に追加する
        public void eval(Environment env, int index, Object value) {
            ((EnvEx)env).putNew(name(index), value);
        }
    }

}
