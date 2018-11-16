package sheep.chap9;

import javassist.gluonj.util.Loader;
import sheep.chap7.ClosureEvaluator;
import sheep.chap8.NativeEvaluator;

public class ClassRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ClassInterpreter.class, args, ClassEvaluator.class,
                     NativeEvaluator.class, ClosureEvaluator.class);
    }
}
