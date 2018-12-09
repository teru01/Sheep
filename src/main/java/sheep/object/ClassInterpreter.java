package sheep.object;
import java.io.FileNotFoundException;

import sheep.*;
import sheep.core.*;
import sheep.function.*;
import sheep.javanative.*;

public class ClassInterpreter {
    public static void main(String[] args) throws ParseException, FileNotFoundException, SheepException{
        if(args[0].equals("parser")) {
            for (int i = 1; i < args.length; i++) {
                BasicInterpreter.checkAst(new BasicParser(), new Natives().environment(new NestedEnv()), args[i]);
            }
        } else if(args[0].equals("lexer")) {
            BasicInterpreter.checkLexer();
        } else {
            BasicInterpreter.run(new BasicParser(), new Natives().environment(new NestedEnv()), args[0]);
        }
    }
}
