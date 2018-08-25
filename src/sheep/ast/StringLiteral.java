package sheep.ast;
import sheep.Token;

public class StringLiteral extends ASTLeaf {
    public StringLiteral(Token t) { super(t); }

    public String value() {
        return getToken().getText();
    }
}