package sheep.ast;

import java.util.*;

import sheep.SheepException;
import sheep.core.Environment;


public class ASTList implements ASTree {
    protected List<ASTree> children;

    public ASTList(List<ASTree> list) {
        this.children = list;
    }

    @Override
    public ASTree child(int i) {
        return this.children.get(i);
    }

    @Override
    public int numChildren() {
        return this.children.size();
    }

    @Override
    public Iterator<ASTree> iterator() {
        return this.children.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        String sep = "";
        for (ASTree t: this.children) {
            sb.append(sep);
            sep = " ";
            sb.append(t.toString());
        }
        return sb.append(')').toString();
    }

    @Override
    public String location() {
        for (ASTree t: this.children) {
            String s = t.location();
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    @Override
    public Object computeAssign(Object right, Environment env) {
        throw new SheepException("bad assignment", this);
    }
}
