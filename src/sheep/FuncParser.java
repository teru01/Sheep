package sheep;
import static sheep.Parser.rule;
import sheep.ast.ParameterList;

public class FuncParser extends BasicParser {
    Parser param = rule().identifier(reserved);
    Parser params = rule(ParameterList.class)
                        .ast(param)
                        .repeat(rule().sep(",").ast(param));
             
}
