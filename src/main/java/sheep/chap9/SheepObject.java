package sheep.chap9;
import sheep.chap6.*;
import sheep.chap7.FuncEvaluator.EnvEx;

// インスタンスオブジェクトを表す
public class SheepObject {
    @SuppressWarnings("serial")
    public static class AccessException extends Exception {}
    // インスタンスフィールドの環境
    protected Environment env;
    public SheepObject(Environment e) { this.env = e; }

    @Override public String toString() {
        return "<object: " + hashCode() + ">";
    }

    public Object read(String member) throws AccessException{
        return getEnv(member).get(member);
    }

    public void write(String member, Object value) throws AccessException{
        ((EnvEx)getEnv(member)).putInCurrentEnv(member, value);
    }

    /**
     * read, writeの時にインスタンスフィールドしか参照できないようにする。
     * @param member
     * @return
     * @throws AccessException
     */
    protected Environment getEnv(String member) throws AccessException {
        Environment e = ((EnvEx)this.env).where(member);
        // e == this.envを確認しないと大域変数もp.hogeの形で参照できてしまう
        if(e != null && e == this.env) {
            return e;
        } else {
            throw new AccessException();
        }
    }
}
