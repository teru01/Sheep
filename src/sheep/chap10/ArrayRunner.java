package sheep.chap10;

import javassist.gluonj.util.Loader;
import sheep.chap7.ClosureEvaluator;
import sheep.chap8.NativeEvaluator;
import sheep.chap9.ClassEvaluator;
import sheep.chap9.ClassInterpreter;
import sheep.extra.BlockScopeEvaluator;

public class ArrayRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ClassInterpreter.class, args, ArrayEvaluator.class, ClassEvaluator.class, NativeEvaluator.class,
                ClosureEvaluator.class, BlockScopeEvaluator.class);
    }
}
