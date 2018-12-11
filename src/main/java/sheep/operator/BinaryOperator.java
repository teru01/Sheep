package sheep.operator;

import sheep.Token;
import sheep.ast.ASTLeaf;
import sheep.ast.ASTree;
import sheep.core.Environment;

public abstract class BinaryOperator extends ASTLeaf {
    public BinaryOperator(Token t) {
        super(t);
    }
    public abstract Object calc(ASTree left, ASTree right, Environment env);
}
