package sheep.chap7;
import sheep.ast.*;
import sheep.chap6.Environment;


public class Function {
    protected ParameterList parameters;
    protected FuncBody body;
    protected Environment env;

    public Function(ParameterList parameters, FuncBody body, Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }
    public ParameterList parameters() {
        return this.parameters;
    }

    public FuncBody body() {
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
