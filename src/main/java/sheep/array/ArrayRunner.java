package sheep.array;

import javassist.gluonj.util.Loader;
import sheep.extra.ConstEvaluator;
import sheep.extra.ForEvaluator;
import sheep.extra.VarEvaluator;
import sheep.function.ClosureEvaluator;
import sheep.javanative.NativeEvaluator;
import sheep.object.ClassEvaluator;
import sheep.object.ClassInterpreter;

public class ArrayRunner {
    public static void main(String[] args) throws Throwable {
        run(args);
    }

    public static void run(String[] args) throws Throwable{
        Loader.run(ClassInterpreter.class, args, ClassEvaluator.class, ArrayEvaluator.class, NativeEvaluator.class,
                ClosureEvaluator.class, VarEvaluator.class, ForEvaluator.class,
                ConstEvaluator.class);
    }
}
