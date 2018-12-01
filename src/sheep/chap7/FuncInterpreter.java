package sheep.chap7;

import java.io.FileNotFoundException;

import sheep.FuncParser;
import sheep.ParseException;
import sheep.chap6.BasicInterpreter;

public class FuncInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException, FileNotFoundException {
        for (String fileName : args) {
            run(new FuncParser(), new NestedEnv(), fileName);
        }
    }
}
