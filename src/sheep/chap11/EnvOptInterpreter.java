package sheep.chap11;
import sheep.chap6.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import sheep.*;
import sheep.chap8.*;
import sheep.ast.*;

public class EnvOptInterpreter {
    public static void main(String[] args) throws ParseException, FileNotFoundException {
        String fileName = args[0];
        run(new ClosureParser(), new Natives().environment(new ResizableArrayEnv()), fileName);
    }

    public static void run(BasicParser bp, Environment env, String fileName) throws ParseException, FileNotFoundException {
        Lexer lexer = new Lexer(new FileReader(fileName));
        while(lexer.peek(0) != Token.EOF) {
            ASTree t = bp.parse(lexer);
            if(!(t instanceof NullStmnt)) {
                ((EnvOptimizer.ASTreeOptEx)t).lookup(((EnvOptimizer.EnvEx2)env).symbols());
                Object r = ((BasicEvaluator.ASTreeEx)t).eval(env);
                //System.out.println("=>" + r);
            }
        }
    }
}
