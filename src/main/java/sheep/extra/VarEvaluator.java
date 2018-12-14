package sheep.extra;
import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.ast.ASTree;
import sheep.ast.Name;
import sheep.ast.VarExpr;
import sheep.core.BasicEvaluator.*;
import sheep.core.Environment;
import sheep.function.*;
import sheep.function.FuncEvaluator.EnvEx;
import sheep.function.FuncEvaluator.PrimaryEx;


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
            try {
                ((EnvEx) env).putInCurrentEnv(((Name)symbol).name(), 0);
            } catch(SheepException e) {
                throw new SheepException(e.getMessage(), this);
            }
            return null;
        }

        @Override
        public Object assign(Object right, Environment env) {
            if (!(getVariable() instanceof Name)) {
                throw new SheepException("bad assignment", this);
            }
            try {
                ((EnvEx) env).putInCurrentEnv(((Name)getVariable()).name(), right);
            } catch(SheepException e) {
                throw new SheepException(e.getMessage(), this);
            }
            return right;
        }
    }
}
