package sheep.extra;
import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.VarParser;
import sheep.ast.ASTree;
import sheep.ast.Name;
import sheep.ast.PrimaryExpr;
import sheep.ast.VarExpr;
import sheep.chap6.BasicEvaluator.ASTreeEx;
import sheep.chap6.BasicEvaluator.BinaryEx;
import sheep.chap6.Environment;
import sheep.chap7.FuncEvaluator;
import sheep.chap7.FuncEvaluator.EnvEx;

@Require({FuncEvaluator.class, VarParser.class})
@Reviser
public class VarEvaluator {
    @Reviser
    public static class VarExprEx extends VarExpr {
        public VarExprEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            return ((ASTreeEx)getAssignExpr()).eval(env);
        }
    }

    @Reviser
    public static class AssignExForBlockScope extends BinaryEx {
        public AssignExForBlockScope(List<ASTree> c) {
            super(c);
        }

        @Override
        public Object eval(Environment env) {
            String op = operator();
            // 代入式に対しては左辺値にevalを呼んではならない
            if ("=".equals(op)) {
                Object right = ((ASTreeEx) right()).eval(env);
                if (left() instanceof VarExpr) {
                    return this.computeAssignForBlockScope(env, right);
                }
                return computeAssign(env, right);
            } else {
                Object left = ((ASTreeEx) left()).eval(env);
                Object right = ((ASTreeEx) right()).eval(env);
                return computeOp(left, op, right);
            }
        }

        protected Object computeAssignForBlockScope(Environment env, Object rvalue) {
            VarExpr left = (VarExpr)left();
            if(!(left.getAssignExpr() instanceof Name)) {
                throw new SheepException("bad assignment", this);
            }
            ((EnvEx)env).putNew(((Name)left.getAssignExpr()).name(), rvalue);
            return rvalue;
        }
    }
}
