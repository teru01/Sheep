package sheep;
import static sheep.Parser.rule;
import javassist.gluonj.Reviser;
import sheep.ast.*;

@Reviser
public class ArrayParser extends FuncParser {
    Parser elements = rule(ArrayLiteral.class)
                        .ast(expr)
                        .repeat(rule().sep(",").ast(expr));
    public ArrayParser() {
        reserved.add("]");
        primary.insertChoice(rule().sep("[").maybe(this.elements).sep("]"));
        postfix.insertChoice(rule(ArrayRef.class).sep("[").ast(expr).sep("]"));
    }
}
