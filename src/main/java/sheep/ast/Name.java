package sheep.ast;

import sheep.Token;

public class Name extends ASTLeaf {
    public Name(Token t) { super(t); }

    public String name() {
        return getToken().getText();
    }
}
