package sheep.ast;

import java.util.ArrayList;
import java.util.Iterator;

import sheep.*;
import sheep.core.Environment;

public class ASTLeaf extends ASTree {
    private static ArrayList<ASTree> empty = new ArrayList<>();
    protected Token token;

    public ASTLeaf(Token t) {
        this.token = t;
    }

    @Override
    public ASTree child(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int numChildren() {
        return 0;
    }

    @Override
    public Iterator<ASTree> iterator() {
        return empty.iterator();
    }

    public String toString() {
        return token.getText();
    }

    @Override
    public String location() {
        return "at line " + this.token.getLineNumber();
    }

    public Token getToken() {
        return this.token;
    }

    @Override
    public Object assign(Object right, Environment e) {
        throw new SheepException("bad assignment.", this);
    }
}
