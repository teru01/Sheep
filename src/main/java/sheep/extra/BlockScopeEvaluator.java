package sheep.extra;

import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.ast.ASTree;
import sheep.ast.BlockStmnt;
import sheep.ast.NullStmnt;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.*;
import sheep.function.ClosureEvaluator;
import sheep.function.NestedEnv;

@Require(ClosureEvaluator.class)
@Reviser
public class BlockScopeEvaluator {
    @Reviser
    public static class BlockEx extends BlockStmnt {
        public BlockEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            NestedEnv newEnv = new NestedEnv(env);
            Object result = 0;
            for (ASTree t : this) {
                if (!(t instanceof NullStmnt)) {
                    result = ((ASTreeEx) t).eval(newEnv);
                }
            }
            return result;
        }
    }
}

