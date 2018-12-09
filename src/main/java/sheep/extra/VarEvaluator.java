package sheep.extra;
import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.ast.ASTree;
import sheep.ast.Name;
import sheep.ast.VarExpr;
import sheep.core.BasicEvaluator.BinaryEx;
import sheep.core.Environment;
import sheep.function.*;
import sheep.function.FuncEvaluator.EnvEx;

@Require(ClosureEvaluator.class)
@Reviser
public class VarEvaluator {
    @Reviser
    public static class VarExprEx extends VarExpr {
        public VarExprEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            ASTree symbol = getVariable();
            if(!(symbol instanceof Name)) {
                throw new SheepException("bad definition", this);
            }
            ((EnvEx) env).putInCurrentEnv(((Name)symbol).name(), null);
            return null;
        }
    }

    @Reviser
    public static class AssignExForBlockScope extends BinaryEx {
        public AssignExForBlockScope(List<ASTree> c) {
            super(c);
        }

        @Override
        protected Object computeAssign(Environment env, Object right) {
            ASTree left = left();
            if(!(left() instanceof VarExpr)) {
                return super.computeAssign(env, right);
            }
            VarExpr leftVar = (VarExpr)left;
            if (!(leftVar.getVariable() instanceof Name)) {
                throw new SheepException("bad assignment", this);
            }
            ((EnvEx) env).putInCurrentEnv(((Name) leftVar.getVariable()).name(), right);
            return right;
        }
    }
}
