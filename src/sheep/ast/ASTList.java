package sheep.ast;

import java.util.*;


public class ASTList extends ASTree {
    protected List<ASTree> children;

    public ASTList(List<ASTree> list) {
        this.children = list;
    }

    public ASTree child(int i) {
        return this.children.get(i);
    }

    public int numChildren() {
        return this.children.size();
    }

    public Iterator<ASTree> children() {
        return this.children.iterator();
    }

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

    public String location() {
        for (ASTree t: this.children) {
            String s = t.location();
            if (s != null) {
                return s;
            }
        }
        return null;
    }
}