package sheep.extra;
import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.VarParser;
import sheep.ast.ASTree;
import sheep.ast.Name;
import sheep.ast.VarExpr;
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
            if(left() instanceof VarExpr) {
                VarExpr leftVar = (VarExpr)left;
                if (!(leftVar.getVariable() instanceof Name)) {
                    throw new SheepException("bad assignment", this);
                }
                ((EnvEx) env).putInCurrentEnv(((Name) leftVar.getVariable()).name(), right);
                return right;
            }
            return super.computeAssign(env, right);
        }
    }
}
