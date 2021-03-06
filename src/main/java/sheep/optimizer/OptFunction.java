package sheep.optimizer;

import sheep.ast.BlockStmnt;
import sheep.ast.*;
import sheep.core.Environment;

public class OptFunction extends Function {
    protected int size;

    public OptFunction(ParameterList parameters, BlockStmnt body, Environment env, int memorySize) {
        super(parameters, body, env);
        this.size = memorySize;
    }

    /**
     * Argumentのeval()で作成される.
     * 呼出される関数用に作られる環境を返す。
     */
    @Override
    public Environment makeEnv() {
        return new ArrayEnv(this.size, env);
    }
}
