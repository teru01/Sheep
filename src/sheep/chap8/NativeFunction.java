package sheep.chap8;
import java.lang.reflect.*;
import sheep.*;
import sheep.ast.*;

public class NativeFunction {
    protected Method method;
    protected String name;
    protected int numParams;

    public NativeFunction(String n, Method m) {
        this.name = n;
        this.method = m;
        this.numParams = m.getParameterTypes().length;
    }

    @Override
    public String toString() {
        return "<native:" + hashCode() + ">";
    }

    public int numOfParameters() {
        return this.numParams;
    }

    public Object invoke(Object[] args, ASTree tree) {
        try {
            return this.method.invoke(null, args);
        } catch(Exception e) {
            throw new SheepException("bad native function call:" + this.name, tree);
        }
    }
}
