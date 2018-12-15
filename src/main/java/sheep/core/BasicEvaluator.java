package sheep.core;

import static sheep.util.SheepUtil.isTrue;

import java.util.List;

import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTLeaf;
import sheep.ast.ASTList;
import sheep.ast.ASTree;
import sheep.ast.BinaryExpr;
import sheep.ast.BlockStmnt;
import sheep.ast.BreakStmnt;
import sheep.ast.ContinueStmnt;
import sheep.ast.IfStmnt;
import sheep.ast.Name;
import sheep.ast.NegativeExpr;
import sheep.ast.NullStmnt;
import sheep.ast.NumberLiteral;
import sheep.ast.ReturnStmnt;
import sheep.ast.StringLiteral;
import sheep.ast.WhileStmnt;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.function.NestedEnv;
import sheep.operator.AssignOperator;
import sheep.operator.BinaryOperator;
import sheep.util.Statements;

@Reviser public class BasicEvaluator {
    @Reviser public abstract static class ASTreeEx extends ASTree {
        public abstract Object eval(Environment env);
        public abstract Object evalForAnotherScope(Environment currentScope, Environment anotherScope);
    }

    @Reviser public static class ASTListEx extends ASTList {
        public ASTListEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            throw new SheepException("cannot eval: " + toString(), this);
        }
    }

    @Reviser public static class ASTLeafEx extends ASTLeaf {
        public ASTLeafEx(Token t) { super(t); }
        public Object eval(Environment env) {
            throw new SheepException("cannot eval: " + toString(), this);
        }

        public Object evalForAnotherScope(Environment currentScope, Environment anotherScope) {
            throw new SheepException("cannot eval: " + toString(), this);
        }
    }

    @Reviser
    public static class NumberEx extends NumberLiteral {
        public NumberEx(Token t) {
            super(t);
        }

        public Object eval(Environment e) {
            return value();
        }
    }

    @Reviser
    public static class StringEx extends StringLiteral {
        public StringEx(Token t) {
            super(t);
        }

        public Object eval(Environment e) {
            return value();
        }
    }

    // 式の中に含まれる変数を表す。環境を調べてその名前のついた値を取り出す
    @Reviser
    public static class NameEx extends Name {
        public NameEx(Token t) {
            super(t);
        }

        public Object eval(Environment env) {
            return env.get(name());
        }

        @Override
        public Object assign(Object right, Environment env) {
            try {
                env.put(name(), right);
            } catch(SheepException e) {
                throw new SheepException(e.getMessage(), this);
            }
            return right;
        }
    }

    @Reviser
    public static class NegativeEx extends NegativeExpr {
        public NegativeEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            Object v = ((ASTreeEx) operand()).eval(env);
            if (v instanceof Integer)
                return Integer.valueOf(-((Integer) v).intValue());
            else
                throw new SheepException("bad type for -", this);
        }
    }

    // 2項演算式
    @Reviser
    public static class BinaryEx extends BinaryExpr {
        public BinaryEx(List<ASTree> c) { super(c); }

        public Object eval(Environment env) {
            ASTLeaf op = operator();
            if(op instanceof BinaryOperator) {
                return ((BinaryOperator)op).calc(left(), right(), env);
            } else if(op instanceof AssignOperator) {
                return ((AssignOperator)op).assignTree(left(), right(), env);
            }
            throw new SheepException("bad operator", this);
        }
    }

    @Reviser
    public static class BlockEx extends BlockStmnt {
        public BlockEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            for(ASTree t: this) {
                if (t instanceof NullStmnt) {
                    continue;
                }
                Object r = ((ASTreeEx) t).eval(env);
                if(r instanceof ReturnObject || r == Statements.CONTINUE || r == Statements.BREAK) {
                    return r;
                }
            }
            return null;
        }
    }

    @Reviser static class ContinueEx extends ContinueStmnt {
        public ContinueEx(List<ASTree> c) {
            super(c);
        }
        public Object eval(Environment env) {
            return Statements.CONTINUE;
        }
    }

    @Reviser static class BreakEx extends BreakStmnt {
        public BreakEx(List<ASTree> c) {
            super(c);
        }
        public Object eval(Environment env) {
            return Statements.BREAK;
        }
    }

    @Reviser
    public static class ReturnEx extends ReturnStmnt {
        public ReturnEx(List<ASTree> c) {
            super(c);
        }
        public Object eval(Environment env) {
            return new ReturnObject(((ASTreeEx)returnValue()).eval(env));
        }
    }

    @Reviser
    public static class IfEx extends IfStmnt {
        public IfEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            // if文条件に合致
            Object c = ((ASTreeEx) condition()).eval(env);
            if (isTrue(c)) {
                return ((ASTreeEx) thenBlock()).eval(new NestedEnv(env));
            }
            // elif文条件に合致
            int k = getElseIfNum();
            for(int i = 0; i < k; i++) {
                c = ((ASTreeEx)(elifStmnt().getElifCondition(i))).eval(env);
                if(isTrue(c)) {
                    return ((ASTreeEx)(elifStmnt().getElifBlock(i))).eval(new NestedEnv(env));
                }
            }
            // else文に合致
            ASTree b = elseBlock();
            if(b == null) {
                return 0;
            } else {
                return ((ASTreeEx)b).eval(new NestedEnv(env));
            }
        }
    }

    /**
     * while文。デフォルトでnullを返却。
     */
    @Reviser
    public static class WhileEx extends WhileStmnt {
        public WhileEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            Object result = 0;
            while(true) {
                Object c = ((ASTreeEx)condition()).eval(env);
                if(!isTrue(c)) {
                    return null;
                } else {
                    result = ((ASTreeEx)body()).eval(new NestedEnv(env));
                    if(result == Statements.CONTINUE) {
                        continue;
                    } else if(result == Statements.BREAK) {
                        return null;
                    } else if(result instanceof ReturnObject) {
                        return result;
                    }
                }
            }
        }
    }
}
