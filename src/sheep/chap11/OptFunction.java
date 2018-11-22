package sheep.chap11;

import sheep.ast.BlockStmnt;
import sheep.ast.ParameterList;
import sheep.chap6.Environment;
import sheep.chap7.Function;

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
