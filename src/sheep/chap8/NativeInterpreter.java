package sheep.chap8;

import sheep.ClosureParser;
import sheep.ParseException;
import sheep.chap6.BasicInterpreter;
import sheep.chap7.NestedEnv;

public class NativeInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClosureParser(), new Natives().environment(new NestedEnv()));
    }
}
