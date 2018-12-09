package sheep.ast;
import sheep.core.Environment;
import sheep.function.*;

public class Function {
    protected ParameterList parameters;
    protected NonScopedBlock body;
    protected Environment env;

    public Function(ParameterList parameters, NonScopedBlock body, Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }
    public ParameterList parameters() {
        return this.parameters;
    }

    public NonScopedBlock body() {
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
