package sheep.extra;

import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.ForParser;
import sheep.SheepException;
import sheep.ast.ASTree;
import sheep.ast.BlockStmnt;
import sheep.ast.ForIterExpr;
import sheep.ast.ForStmnt;
import sheep.chap6.BasicEvaluator.*;
import static sheep.chap6.BasicEvaluator.FALSE;
import static sheep.chap6.BasicEvaluator.TRUE;
import sheep.chap6.Environment;
import sheep.chap7.*;

@Require({ClosureEvaluator.class, ForParser.class })
@Reviser
public class ForEvaluator {
    @Reviser
    public static class ForStmntEx extends ForStmnt {
        public ForStmntEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            Environment newEnv = this.makeEnv(env);
            ((ForIterExprEx) this.iterExpr()).init(env, newEnv);
            return ((ForIterExprEx) this.iterExpr()).eval(newEnv, this.iterBody());
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
            if((this.initExpr().numChildren()) != 0){
                ((BinaryEx) this.initExpr()).evalToinitFor(env, newEnv);
            }
        }

        public Object eval(Environment newEnv, Object body) {
            Object result = 0;
            Object c = TRUE;
            boolean conditionExists = (this.conditionExpr().numChildren()) != 0;
            boolean updateExists = (this.updateExpr().numChildren()) != 0;
            while (true) {
                if(conditionExists) {
                    c = ((ASTreeEx) this.conditionExpr()).eval(newEnv);
                }
                if (c instanceof Integer && ((Integer) c).intValue() == FALSE) {
                    return result;
                } else {
                    result = ((ASTreeEx) body).eval(newEnv);
                }
                if(updateExists) {
                    ((ASTreeEx)this.updateExpr()).eval(newEnv);
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
        @Override
        public Object evalToinitFor(Environment env, Environment newEnv) {
            String op = operator();
            if ("=".equals(op)) {
                Object right = ((ASTreeEx) right()).eval(env);
                return computeAssign(newEnv, right);
            } else {
                Object left = ((ASTreeEx) left()).eval(env);
                Object right = ((ASTreeEx) right()).eval(env);
                return computeOp(left, op, right);
            }
        }
    }

}
