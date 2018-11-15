package sheep;
import static sheep.Parser.rule;
import sheep.ast.*;

public class ClassParser extends ClosureParser {
    Parser member = rule().or(def, simple);
    Parser class_body = rule(ClassBody.class)
                            .sep("{")
                            .option(member)
                            .repeat(rule().sep(";", Token.EOL).option(member))
                            .sep("}");
    Parser defclass = rule(ClassStmnt.class)
                            .sep("class")
                            .identifier(reserved)
                            .option(rule().sep("extends").identifier(reserved))
                            .ast(this.class_body);
    public ClassParser() {
        postfix.insertChoice(rule(Dot.class).sep(".").identifier(reserved));
        program.insertChoice(this.defclass);
    }
}
