package sheep.chap6;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import sheep.BasicParser;
import sheep.CodeDialog;
import sheep.Lexer;
import sheep.ParseException;
import sheep.Token;
import sheep.ast.ASTree;
import sheep.ast.NullStmnt;

public class BasicInterpreter {
    public static void main(String[] args) throws Exception {
        String fileName = args[0];
        run(new BasicParser(), new BasicEnv(), fileName);
    }

    /**
     * bp: 複数の部分構文木(factorやexpr)の情報をelementsの中にもつ
     */
    public static void run(BasicParser bp, Environment env, String fileName) throws ParseException {
        Reader reader = null;
        try {
            reader = new FileReader(fileName);
        } catch(FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(-1);
        }
        Lexer lexer = new Lexer(reader);
        while(lexer.peek(0) != Token.EOF) {
            ASTree t = bp.parse(lexer);
            if(!(t instanceof NullStmnt)) {
                ((BasicEvaluator.ASTreeEx)t).eval(env);
                //System.out.println("=> " + r);
            }
        }
    }

    public static void run(BasicParser bp, Environment env) throws ParseException {
        Lexer lexer = new Lexer(new CodeDialog());
        while (lexer.peek(0) != Token.EOF) {
            ASTree t = bp.parse(lexer);
            if (!(t instanceof NullStmnt)) {
                ((BasicEvaluator.ASTreeEx) t).eval(env);
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

    public static void checkAst(BasicParser bp, Environment env, String fileName) throws ParseException {
        Reader reader = null;
        try {
            reader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(-1);
        }
        Lexer l = new Lexer(reader);
        while (l.peek(0) != Token.EOF) {
            ASTree ast = bp.parse(l);
            System.out.println("=> " + ast.toString());
        }
    }

    public static void checkLexer() throws ParseException{
        Lexer l = new Lexer(new CodeDialog());
        for(Token t; (t = l.read()) != Token.EOF;) {
            System.out.println("=> " + t.getText());
        }
    }
}
