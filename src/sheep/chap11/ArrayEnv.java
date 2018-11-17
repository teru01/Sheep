package sheep.chap11;
import javassist.compiler.ast.Symbol;
import sheep.SheepException;
import sheep.chap11.EnvOptimizer.EnvEx2;
import sheep.chap6.Environment;

public class ArrayEnv implements Environment {
    protected Object[] values;
    protected Environment outer;

    public ArrayEnv(int size, Environment out) {
        this.values = new Object[size];
        this.outer = out;
    }

    public Symbols symbols() {
        throw new SheepException("no symbols");
    }
}
