package sheep.chap11;

import javassist.gluonj.util.Loader;
import sheep.chap12.InlineCache;
import sheep.chap8.NativeEvaluator;

public class EnvOptRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(EnvOptInterpreter.class, args, EnvOptimizer.class, NativeEvaluator.class);
    }
}
