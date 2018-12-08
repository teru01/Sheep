package sheep.chap8;

import javassist.gluonj.util.Loader;
import sheep.chap7.ClosureEvaluator;

public class NativeRunner {
    public static void main(String[] args) throws Throwable {
        Loader.run(NativeInterpreter.class, args, NativeEvaluator.class, ClosureEvaluator.class);
    }
}
