package sheep.ast;
import sheep.Token;
import sheep.core.Environment;

public class Name extends ASTLeaf {
    public Name(Token t) { super(t); }

    public String name() {
        return getToken().getText();
    }
}
