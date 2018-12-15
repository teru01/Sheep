package sheep.util;
public enum Statements {
    BREAK("break"),CONTINUE("continue"),RETURN("return");

    private final String value;
    private Statements(String s) {
        this.value = s;
    }
    public String getValue() {
        return this.value;
    }
}
