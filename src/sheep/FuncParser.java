package sheep;
import static sheep.Parser.rule;
import sheep.ast.*;
import sheep.ast.ParameterList;

public class FuncParser extends BasicParser {
    Parser param = rule().identifier(this.reserved);
    Parser params = rule(ParameterList.class)
                        .ast(this.param)
                        .repeat(rule().sep(",").ast(this.param));
    Parser paramList = rule().sep("(").maybe(this.params).sep(")");
    Parser funcBody = rule(NonScopedBlock.class)
                        .sep("{")
                        .option(this.statement0)
                        .repeat(rule().sep(";", Token.EOL).option(this.statement0))
                        .sep("}");
    Parser def = rule(DefStmnt.class)
                    .sep("def").identifier(this.reserved)
                    .ast(this.paramList)
                    .ast(this.funcBody);
    Parser args = rule(Arguments.class).ast(expr).repeat(rule().sep(",").ast(expr));
    Parser postfix = rule().sep("(").maybe(this.args).sep(")");

    public FuncParser() {
        this.reserved.add(")");
        this.primary.repeat(this.postfix);
        this.simple.option(this.args);
        this.program.insertChoice(this.def);
    }
}
