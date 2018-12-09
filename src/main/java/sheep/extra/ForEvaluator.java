package sheep.extra;

import java.util.List;

import javassist.gluonj.*;
import sheep.*;
import sheep.ast.*;
import sheep.core.BasicEvaluator.*;
import static sheep.core.BasicEvaluator.*;
import sheep.core.Environment;
import sheep.function.*;

@Require(ClosureEvaluator.class)
@Reviser
public class ForEvaluator {
    @Reviser
    public static class ForStmntEx extends ForStmnt {
        public ForStmntEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            Environment newEnv = makeEnv(env);
            ForIterExprEx iterExpr = (ForIterExprEx) iterExpr();
            iterExpr.init(env, newEnv);
            return iterExpr.eval(newEnv, iterBody());
        }
    }

    @Reviser
    public static class ForIterExprEx extends ForIterExpr {
        public ForIterExprEx(List<ASTree> c) {
            super(c);
        }

        /**
         * for文の外の環境で初期化式を評価し返す。
         */
        public void init(Environment env, Environment newEnv) {
            if((initExpr().numChildren()) == 0) {
                return;
            }
            ((ASTreeEx) initExpr()).evalForAnotherScope(env, newEnv);
        }

        public Object eval(Environment newEnv, Object body) {
            Object result = 0;
            Object c = TRUE;
            boolean conditionExists = (conditionExpr().numChildren()) != 0;
            boolean updateExists = (updateExpr().numChildren()) != 0;
            while (true) {
                if(conditionExists) {
                    c = ((ASTreeEx) conditionExpr()).eval(newEnv);
                }
                if (c instanceof Integer && ((Integer) c).intValue() == FALSE) {
                    return result;
                } else {
                    result = ((ASTreeEx) body).eval(newEnv);
                }
                if(updateExists) {
                    ((ASTreeEx)updateExpr()).eval(newEnv);
                }
            }
        }
    }

    @Reviser
    public static class VarAssignIterFor extends BinaryEx {
        public VarAssignIterFor(List<ASTree> c) {
            super(c);
        }

        /**
         * 右辺はfor文が書かれた環境、左辺のassignはfor文の中の環境で行う
         */
        public Object evalForAnotherScope(Environment env, Environment newEnv) {
            String op = operator();
            if ("=".equals(op)) {
                Object right = ((ASTreeEx) right()).eval(env);
                return computeAssign(newEnv, right);
            } else {
                return evalCalcurate(env, op);
            }
        }
    }

    @Reviser
    public static class ASTListFor extends ASTList {
        public ASTListFor(List<ASTree> c) { super(c); }
        public Object evalForAnotherScope(Environment currentScope, Environment anotherScope) {
            for(ASTree expr:this.children) {
                ((ASTListFor) expr).evalForAnotherScope(currentScope, anotherScope);
            }
            return null;
        }
    }

}
