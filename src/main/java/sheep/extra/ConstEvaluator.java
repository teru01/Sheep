package sheep.extra;

import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.ast.ASTree;
import sheep.ast.ConstExpr;
import sheep.ast.Name;
import sheep.core.BasicEvaluator.*;
import sheep.core.Environment;
import sheep.function.ClosureEvaluator;
import sheep.function.FuncEvaluator.EnvEx;

@Require(ClosureEvaluator.class)
@Reviser
public class ConstEvaluator {
    @Reviser
    public static class ConstExprEx extends ConstExpr {
        public ConstExprEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment evn) throws SheepException{
            throw new SheepException("const variable must have an initializer", this);
        }

        @Override
        public Object assign(Object right, Environment env) {
            if (!(getConstant() instanceof Name)) {
                throw new SheepException("bad assignment", this);
            }
            ((EnvEx) env).putConst(((Name)getConstant()).name(), right);
            return right;
        }
    }
}
