package sheep.ast;

import java.util.ArrayList;
import java.util.Iterator;

import sheep.*;

public class ASTLeaf extends ASTree {
    private static ArrayList<ASTree> empty = new ArrayList<>();
    protected Token token;
    
    public ASTLeaf(Token t) {
        this.token = t; 
    }

    public ASTree child(int i) {
        throw new IndexOutOfBoundsException(); 
    }

    public int numChildren() {
        return 0; 
    }
    
    public Iterator<ASTree> children() {
        return empty.iterator();
    }

    public String toString() {
        return token.getText();
    }

    public String location() {
        return "at line " + this.token.getLineNumber();
    }

    public Token getToken() {
        return this.token;
    }
}