package sheep.chap9;
import sheep.chap6.*;
import sheep.chap7.*;
import sheep.chap7.FuncEvaluator.EnvEx;

// インスタンスオブジェクトを表す
public class SheepObject {
    public static class AccessException extends Exception {}
    // インスタンスがnewされた時の環境
    protected Environment env;
    public SheepObject(Environment e) { this.env = e; }

    @Override public String toString() {
        return "<object: " + hashCode() + ">";
    }

    public Object read(String member) throws AccessException{
        return this.getEnv(member).get(member);
    }

    public void write(String member, Object value) throws AccessException{
        ((EnvEx)this.getEnv(member)).putNew(member, value);
    }

    protected Environment getEnv(String member) throws AccessException {
        Environment e = ((EnvEx)this.env).where(member);
        if(e != null && e == this.env) {
            return e;
        } else {
            throw new AccessException();
        }
    }
}
