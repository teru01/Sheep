package sheep.chap12;
import java.util.*;
import sheep.ast.*;
import sheep.optimizer.*;
import sheep.optimizer.EnvOptimizer.DefStmntEx;
import sheep.chap12.ObjOptimizer.DefStmntEx2;
import sheep.core.*;
import sheep.object.*;

public class OptClassInfo extends ClassInfo {
    protected Symbols methods, fields;
    // 共有されるインスタンスメソッド
    protected DefStmnt[] methodDefs;
    public OptClassInfo(ClassStmnt cs, Environment env, Symbols methods, Symbols fields) {
        super(cs, env);
        this.methods = methods;
        this.fields = fields;
        this.methodDefs = null;
    }

    public int size() {
        return this.fields.size();
    }

    @Override
    public OptClassInfo superClass() {
        return (OptClassInfo)this.superClass;
    }

    /**
     * ClassStmntがevalされた時に渡されたオブジェクトに
     */
    public void copyTo(Symbols f, Symbols m, ArrayList<DefStmnt> mlist) {
        f.append(this.fields);
        m.append(this.methods);
        for(DefStmnt def: this.methodDefs) {
            mlist.add(def);
        }
    }

    public Integer fieldIndex(String name) {
        return this.fields.find(name);
    }

    public Integer methodIndex(String name) {
        return this.methods.find(name);
    }

    public Object method(OptSheepObject self, int index) {
        DefStmnt def = methodDefs[index];
        return new OptMethod(def.parameters(), def.body(), environment(), ((DefStmntEx2)def).locals(), self);
    }

    public void setMethods(ArrayList<DefStmnt> methods) {
        this.methodDefs = methods.toArray(new DefStmnt[methods.size()]);
    }
}
