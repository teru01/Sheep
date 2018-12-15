package sheep.chap12;
import sheep.ast.*;
import sheep.optimizer.*;
import sheep.core.*;

public class OptMethod extends OptFunction {
    OptSheepObject self;
    public OptMethod(ParameterList parameters, BlockStmnt body, Environment env, int memorySize, OptSheepObject self) {
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
