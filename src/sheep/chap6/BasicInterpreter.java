package sheep.chap6;
import sheep.*;
import sheep.ast.ASTree;
import sheep.ast.NullStmnt;

public class BasicInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new BasicParser(), new BasicEnv());
    }

    /**
     * bp: 複数の部分構文木(factorやexpr)の情報をelementsの中にもつ
     */
    public static void run(BasicParser bp, Environment env) throws ParseException {
        Lexer lexer = new Lexer(new CodeDialog());
        while(lexer.peek(0) != Token.EOF) {
            ASTree t = bp.parse(lexer);
            if(!(t instanceof NullStmnt)) {
                Object r = ((BasicEvaluator.ASTreeEx)t).eval(env);
                System.out.println("=> " + r);
            }
        }
    }

    /**
     * 生成される構文木をチェックする
     */
    public static void checkAst(BasicParser bp, Environment env) throws ParseException {
        Lexer l = new Lexer(new CodeDialog());
        while (l.peek(0) != Token.EOF) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }
}

