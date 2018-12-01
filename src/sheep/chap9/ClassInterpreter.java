package sheep.chap9;
import java.io.FileNotFoundException;

import sheep.*;
import sheep.chap6.*;
import sheep.chap7.*;
import sheep.chap8.*;

public class ClassInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException, FileNotFoundException{
        for (String fileName : args) {
            run(new ClassParser(), new Natives().environment(new NestedEnv()), fileName);
        }
        //checkAst(new ClassParser(), new Natives().environment(new NestedEnv()));
    }
}
