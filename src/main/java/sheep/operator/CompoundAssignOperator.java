package sheep.operator;
import sheep.SheepException;
import sheep.Token;
import sheep.core.Environment;
import sheep.ast.*;

public class CompoundAssignOperator extends AssignOperator {
    public CompoundAssignOperator(Token t) {
        super(t);
    }

    @Override
    public Object assignTree(ASTree left, ASTree right, Environment env) {
        String operator = this.token.getText().substring(0, 1);
        ASTLeaf bo = OpFactory.createOperator(new Token(this.token.getLineNumber()) {
            @Override
            public String getText() {
                return operator;
            }
        });
        if(!(bo instanceof BinaryOperator)) {
            throw new SheepException("bad compound operator", this.token.getLineNumber());
        }
        Object rightObj = ((BinaryOperator)bo).calc(left, right, env);
        return assignObject(left, rightObj, env);
    }
}
