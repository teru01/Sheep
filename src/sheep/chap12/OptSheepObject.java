package sheep.chap12;

public class OptSheepObject {
    public static class AccessException extends Exception {
        AccessException() {
            this(null);
        }
        AccessException(String m) {
            super(m);
        }
    }
    protected OptClassInfo classInfo;
    // インスタンスが持つのはフィールドのみ。メソッドは持たない。
    protected Object[] fields;

    public OptSheepObject(OptClassInfo ci, int size) {
        this.classInfo = ci;
        this.fields = new Object[size];
    }

    public OptClassInfo classInfo() { return this.classInfo; }

    /**
     * フィールド、メソッドの順に探して返す。（名前でインデックス検索）
     */
    public Object read(String name) throws AccessException {
        Integer i = classInfo.fieldIndex(name);
        if(i != null) {
            return this.fields[i];
        } else {
            i = classInfo.methodIndex(name);
            if(i != null) {
                return method(i);
            }
        }
        return new AccessException();
    }

    public void write(String name, Object value) throws AccessException {
        Integer i = this.classInfo.fieldIndex(name);
        if(i == null) {
            throw new AccessException();
        } else {
            this.fields[i] = value;
        }
    }

    public Object read(int index) {
        return this.fields[index];
    }

    public void write(int i, Object obj) {
        this.fields[i] = obj;
    }

    public Object method(int i) {
        return this.classInfo.method(this, i);
    }

}
