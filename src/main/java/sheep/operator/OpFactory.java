package sheep.operator;

import java.util.Arrays;

import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTLeaf;

public class OpFactory {
    public static ASTLeaf createOperator(Token t) {
        String op = t.getText();
        String[] AssignOperators = {"+=", "-=", "*=", "/="};
        if(Arrays.asList(AssignOperators).contains(op))
            return new CompoundAssignOperator(t);
        else if(op.equals("="))
            return new AssignOperator(t);
        else if(op.equals("+"))
            return new PlusOperator(t);
        else if(op.equals("-"))
            return new MinusOperator(t);
        else if(op.equals("*"))
            return new ProductOperator(t);
        else if (op.equals("/"))
            return new QuotientOperator(t);
        else if (op.equals("%"))
            return new SurplusOperator(t);
        else if (op.equals("=="))
            return new EqualityOperator(t);
        else if (op.equals("!="))
            return new NonEqualityOperator(t);
        else if (op.equals(">"))
            return new MoreOperator(t);
        else if (op.equals(">="))
            return new MoreOrEqualOperator(t);
        else if (op.equals("<"))
            return new LessOperator(t);
        else if (op.equals("<="))
            return new LessOrEqualOperator(t);
        else if (op.equals("&&"))
            return new AndOperator(t);
        else if (op.equals("||"))
            return new OrOperator(t);
        throw new SheepException("Unsupported operator", t.getLineNumber());
    }
}
