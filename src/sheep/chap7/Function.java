package sheep.chap7;
import sheep.ast.BlockStmnt;
import sheep.ast.ParameterList;
import sheep.chap6.Environment;

public class Function {
    protected ParameterList parameters;
    protected BlockStmnt body;
    protected Environment env;

    public Function(ParameterList parameters, BlockStmnt body, Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }
    public ParameterList parameters() {
        return this.parameters;
    }

    public BlockStmnt body() {
        return body;
    }

    public Environment makeEnv() {
        return new NestedEnv(this.env);
    }

    @Override
    public String toString() {
        return "<fun:" + hashCode() + ">";
    }
}
