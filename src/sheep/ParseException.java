package sheep;
import java.io.IOException;

public class ParseException extends Exception {
    public ParseException(Token t) {
        this("", t);
    }

    public ParseException(String msg, Token t) {
        super("syntax error around " + generateLocationMessage(t) + ". " + msg);
    }

    public ParseException(IOException e) {
        super(e);
    }

    public ParseException(String msg) {
        super(msg);
    }

    private static String generateLocationMessage(Token t) {
        if (t == Token.EOF) {
            return "the last line";
        } else {
            return "\"" + t.getText() + "\" at line " + t.getLineNumber();
        }
    }
}