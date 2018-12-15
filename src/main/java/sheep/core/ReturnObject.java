package sheep.core;
public class ReturnObject {
    private Object value;
    ReturnObject(Object v) {
        this.value = v;
    }

    public Object getValue() {
        return this.value;
    }
}
