package sheep.operator;

import sheep.Token;
import sheep.ast.*;
import sheep.core.*;

public abstract class BinaryOperator extends ASTLeaf {
    public BinaryOperator(Token t) {
        super(t);
    }
    public abstract Object calc(ASTree left, ASTree right, Environment env);
}
