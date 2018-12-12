package sheep.operator;

import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTLeaf;
import sheep.ast.ASTree;
import sheep.core.Environment;

public abstract class BinaryOperator extends ASTLeaf {
    public BinaryOperator(Token t) {
        super(t);
    }

    public static BinaryOperator createOperator(Token t) {
        String op = t.getText();
        if(op.equals("+"))
            return new PlusOperator(t);
        else if(op.equals("-"))
            return new MinusOperator(t);
        else if(op.equals("*"))
            return new ProductOperator(t);
        else if (op.equals("/"))
            return new QuotientOperator(t);
        else if (op.equals("%"))
            return new SurplusOperator(t);
        // else if (op.equals("=="))
        //     return a == b ? TRUE : FALSE;
        // else if (op.equals("!="))
        //     return a != b ? TRUE : FALSE;
        // else if (op.equals(">"))
        //     return a > b ? TRUE : FALSE;
        // else if (op.equals("<"))
        //     return a < b ? TRUE : FALSE;
        // else if (op.equals("&&"))
        //     return (a != 0 && b != 0) ? TRUE : FALSE;
        // else if (op.equals("||"))
        //     return (a != 0 || b != 0) ? TRUE : FALSE;
        throw new SheepException("Unsupported operator", t.getLineNumber());
    }
    public abstract Object calc(ASTree left, ASTree right, Environment env);
}
