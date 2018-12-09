package sheep.optimizer;

import javassist.gluonj.util.Loader;
import sheep.chap12.InlineCache;
import sheep.javanative.NativeEvaluator;

public class EnvOptRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(EnvOptInterpreter.class, args, EnvOptimizer.class, NativeEvaluator.class);
    }
}
