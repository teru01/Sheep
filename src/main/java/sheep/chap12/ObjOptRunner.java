package sheep.chap12;

import javassist.gluonj.util.Loader;
import sheep.array.ArrayEvaluator;
import sheep.javanative.NativeEvaluator;

public class ObjOptRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ObjOptInterpreter.class, args, ObjOptimizer.class, NativeEvaluator.class, ArrayEvaluator.class);
    }
}
