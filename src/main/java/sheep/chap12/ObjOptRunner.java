package sheep.chap12;

import javassist.gluonj.util.Loader;
import sheep.chap10.ArrayEvaluator;
import sheep.chap8.NativeEvaluator;

public class ObjOptRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(ObjOptInterpreter.class, args, ObjOptimizer.class, NativeEvaluator.class, ArrayEvaluator.class);
    }
}
