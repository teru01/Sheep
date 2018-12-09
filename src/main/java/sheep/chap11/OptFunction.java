package sheep.chap11;

import sheep.ast.BlockStmnt;
import sheep.ast.NonScopedBlock;
import sheep.ast.*;
import sheep.chap6.Environment;

public class OptFunction extends Function {
    protected int size;

    public OptFunction(ParameterList parameters, NonScopedBlock body, Environment env, int memorySize) {
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
