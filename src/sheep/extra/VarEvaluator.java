package sheep.extra;
import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.VarParser;
import sheep.ast.ASTree;
import sheep.ast.Name;
import sheep.ast.VarExpr;
import sheep.chap6.BasicEvaluator.ASTreeEx;
import sheep.chap6.BasicEvaluator.BinaryEx;
import sheep.chap6.Environment;
import sheep.chap7.*;
import sheep.chap7.FuncEvaluator.EnvEx;

@Require({ClosureEvaluator.class, VarParser.class})
@Reviser
public class VarEvaluator {
    @Reviser
    public static class VarExprEx extends VarExpr {
        public VarExprEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            ASTree symbol = this.getVariable();
            if(!(symbol instanceof Name)) {
                throw new SheepException("bad definition", this);
            }
            ((EnvEx) env).putNew(((Name)symbol).name(), null);
            return null;
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
            if ("=".equals(op)) {
                Object right = ((ASTreeEx) right()).eval(env);
                return computeAssign(env, right);
            } else {
                Object left = ((ASTreeEx) left()).eval(env);
                Object right = ((ASTreeEx) right()).eval(env);
                return computeOp(left, op, right);
            }
        }

        @Override
        protected Object computeAssign(Environment env, Object right) {
            ASTree left = this.left();
            if(this.left() instanceof VarExpr) {
                VarExpr leftVar = (VarExpr)left;
                if (!(leftVar.getVariable() instanceof Name)) {
                    throw new SheepException("bad assignment", this);
                }
                ((EnvEx) env).putNew(((Name) leftVar.getVariable()).name(), right);
                return right;
            }
            return super.computeAssign(env, right);
        }
    }
}
