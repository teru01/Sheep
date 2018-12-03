package sheep.chap9;
import java.io.FileNotFoundException;

import sheep.*;
import sheep.chap6.*;
import sheep.chap7.*;
import sheep.chap8.*;

public class ClassInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException, FileNotFoundException{
        if(args[0].equals("parser")) {
            for (int i = 1; i < args.length; i++) {
                checkAst(new ClassParser(), new Natives().environment(new NestedEnv()), args[i]);
            }
        } else if(args[0].equals("lexer")) {
            checkLexer();
        } else {
            for (int i=1; i<args.length; i++) {
                run(new ClassParser(), new Natives().environment(new NestedEnv()), args[i]);
            }
        }
    }
}
