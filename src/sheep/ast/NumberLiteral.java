package sheep.ast;
import sheep.Token;

public class NumberLiteral extends ASTLeaf {
    public NumberLiteral(Token t) { super(t); }
    
    public int value() {
        return getToken().getNumber();
    }
}
