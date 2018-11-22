package sheep.chap11;
import sheep.chap6.*;
import sheep.*;
import sheep.chap8.*;
import sheep.ast.*;

public class EnvOptInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClosureParser(), new Natives().environment(new ResizableArrayEnv()));
    }

    public static void run(BasicParser bp, Environment env) throws ParseException {
        Lexer lexer = new Lexer(new CodeDialog());
        while(lexer.peek(0) != Token.EOF) {
            ASTree t = bp.parse(lexer);
            if(!(t instanceof NullStmnt)) {
                ((EnvOptimizer.ASTreeOptEx)t).lookup(((EnvOptimizer.EnvEx2)env).symbols());
                Object r = ((BasicEvaluator.ASTreeEx)t).eval(env);
                System.out.println("=>" + r);
            }
        }
    }
}
