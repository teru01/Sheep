package sheep.chap12;
import sheep.ast.*;
import sheep.chap11.*;
import sheep.chap6.*;

public class OptMethod extends OptFunction {
    OptSheepObject self;
    public OptMethod(ParameterList parameters, FuncBody body, Environment env, int memorySize, OptSheepObject self) {
        super(parameters, body, env, memorySize);
        this.self = self;
    }

    @Override
    public Environment makeEnv() {
        ArrayEnv e = new ArrayEnv(this.size, this.env);
        e.put(0, 0, self);
        return e;
    }
}
