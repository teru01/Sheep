package sheep.chap7;

import sheep.FuncParser;
import sheep.ParseException;
import sheep.chap6.BasicInterpreter;

public class FuncInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new FuncParser(), new NestedEnv());
    }
}
