package sheep.chap7;
import sheep.*;
import sheep.chap6.*;

public class ClosureInterpreter extends BasicInterpreter {
    public static void main(String args[]) throws ParseException {
        run(new ClosureParser(), new NestedEnv());
    }
}
