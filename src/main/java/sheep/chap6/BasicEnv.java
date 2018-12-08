package sheep.chap6;
import java.util.HashMap;

public class BasicEnv implements Environment {
    protected HashMap<String, Object> values;
    public BasicEnv() {
        this.values = new HashMap<>();
    }

    @Override
    public void put(String name, Object value) {
        this.values.put(name, value);
    }

    @Override
    public Object get(String name) {
        return this.values.get(name);
    }
}
