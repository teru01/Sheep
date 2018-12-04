package sheep.chap10;

import javassist.gluonj.util.Loader;
import sheep.chap7.ClosureEvaluator;
import sheep.chap8.NativeEvaluator;
import sheep.chap9.ClassEvaluator;
import sheep.chap9.ClassInterpreter;
import sheep.extra.BlockScopeEvaluator;
import sheep.extra.ForEvaluator;
import sheep.extra.VarEvaluator;

public class ArrayRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ClassInterpreter.class, args, ClassEvaluator.class, ArrayEvaluator.class, NativeEvaluator.class,
                ClosureEvaluator.class, BlockScopeEvaluator.class, VarEvaluator.class, ForEvaluator.class);
    }
}
