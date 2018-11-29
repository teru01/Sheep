package tests;
import sheep.ast.ASTree;
import sheep.*;

public class ParseRunner {
    public static void main(String[] args) throws ParseException{
        Lexer l = new Lexer(new CodeDialog());
        BasicParser bp = new ClassParser();
        while (l.peek(0) != Token.EOF) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }
}
