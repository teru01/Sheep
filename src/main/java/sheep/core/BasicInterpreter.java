package sheep.core;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import sheep.BasicParser;
import sheep.CodeDialog;
import sheep.Lexer;
import sheep.ParseException;
import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTree;
import sheep.ast.BreakStmnt;
import sheep.ast.ContinueStmnt;
import sheep.ast.NullStmnt;
import sheep.util.Statements;

public class BasicInterpreter {
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
            if(t instanceof NullStmnt) {
                continue;
            }
            Object r = ((BasicEvaluator.ASTreeEx)t).eval(env);
            if(r instanceof ReturnObject) {
                return;
            } else if(r == Statements.CONTINUE || r == Statements.BREAK) {
                throw new SheepException("This statement is not permitted: " + ((Statements)r).getValue());
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

