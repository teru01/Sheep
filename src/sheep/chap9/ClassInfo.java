package sheep.chap9;
import sheep.*;
import sheep.ast.ClassBody;
import sheep.ast.ClassStmnt;
import sheep.chap6.*;

public class ClassInfo {
    protected ClassStmnt definition;
    protected Environment environment;
    protected ClassInfo superClass;

    public ClassInfo(ClassStmnt cs, Environment env) {
        this.definition = cs;
        this.environment = env;
        Object obj = env.get(cs.superClass());
        if (obj == null) {
            this.superClass = null;
        } else if(obj instanceof ClassInfo) {
            this.superClass = (ClassInfo)obj;
        } else {
            throw new SheepException("unknown super class: " + cs.superClass(), cs);
        }
    }
    public String name() {
        return this.definition.name();
    }
    public ClassInfo superClass() {
        return this.superClass;
    }
    public ClassBody body() {
        return this.definition.body();
    }
    public Environment environment() {
        return this.environment;
    }
    @Override
    public String toString() {
        return "<class " + this.name() + ">";
    }

}
