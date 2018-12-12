package sheep.core;

import java.util.Arrays;
import java.util.List;

import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTLeaf;
import sheep.ast.ASTList;
import sheep.ast.ASTree;
import sheep.ast.BinaryExpr;
import sheep.ast.BlockStmnt;
import sheep.ast.IfStmnt;
import sheep.ast.Name;
import sheep.ast.NegativeExpr;
import sheep.ast.NonScopedBlock;
import sheep.ast.NullStmnt;
import sheep.ast.NumberLiteral;
import sheep.ast.StringLiteral;
import sheep.ast.WhileStmnt;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.operator.*;

@Reviser public class BasicEvaluator {
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    @Reviser public interface ASTreeEx extends ASTree {
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
            Object value = env.get(name());
            if (value == null)
                throw new SheepException("undefined name: " + name(), this);
            else
                return value;
        }

        @Override
        public Object assign(Object right, Environment env) {
            env.put(name(), right);
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
            // String[] AssignOperators = {"+=", "-=", "*=", "/=", "="};
            // // 代入を伴わない
            // if(!Arrays.asList(AssignOperators).contains(op)) {
            //     return evalCalcurate(env, op);
            // }
            // Object newValue;
            // if(op.equals("=")) {
            //     newValue = ((ASTreeEx)right()).eval(env);
            // } else {
            //     newValue = computeOp(((ASTreeEx)left()).eval(env), op.substring(0, 1), ((ASTreeEx)right()).eval(env));
            // }
            // try {
            //     return computeAssign(env, newValue);
            // } catch(SheepException e) {
            //     throw new SheepException(e.getMessage(), this);
            // }
        }

        protected Object evalCalcurate(Environment env, String op) {
            Object left = ((ASTreeEx) left()).eval(env);
            Object right = ((ASTreeEx) right()).eval(env);
            return computeOp(left, op, right);
        }

        protected Object computeOp(Object left, String op, Object right) {
            if(left instanceof Integer && right instanceof Integer) {
                return computeNumber((Integer)left, op, (Integer)right);
            } else {
                if(op.equals("+")) {
                    return String.valueOf(left) + String.valueOf(right);
                } else if(op.equals("==")) {
                    if(left == null) {
                        return right == null ? TRUE : FALSE;
                    } else {
                        return left.equals(right) ? TRUE : FALSE;
                    }
                } else if(op.equals("!=")) {
                    if(left == null) {
                        return right != null ? TRUE : FALSE;
                    } else {
                        return left.equals(right) ? FALSE : TRUE;
                    }
                } else {
                    throw new SheepException("bad type", this);
                }
            }
        }

        protected Object computeNumber(Integer left, String op, Integer right) {
            int a = left.intValue();
            int b = right.intValue();
            if (op.equals("+"))
                return a + b;
            else if (op.equals("-"))
                return a - b;
            else if (op.equals("*"))
                return a * b;
            else if (op.equals("/"))
                return a / b;
            else if (op.equals("%"))
                return a % b;
            else if (op.equals("=="))
                return a == b ? TRUE : FALSE;
            else if (op.equals("!="))
                return a != b ? TRUE : FALSE;
            else if (op.equals(">"))
                return a > b ? TRUE : FALSE;
            else if (op.equals("<"))
                return a < b ? TRUE : FALSE;
            else if (op.equals("&&"))
                return (a != 0 && b != 0) ? TRUE : FALSE;
            else if (op.equals("||"))
                return (a != 0 || b != 0) ? TRUE : FALSE;
            else throw new SheepException("bad operator", this);
        }
    }

    @Reviser
    public static class BlockEx extends BlockStmnt {
        public BlockEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            Object result = 0;
            for(ASTree t: this) {
                if(!(t instanceof NullStmnt))
                    result = ((ASTreeEx)t).eval(env);
            }
            return result;
        }
    }

    @Reviser
    public static class NonScopedBlockEx extends NonScopedBlock {
        public NonScopedBlockEx(List<ASTree> c) {
            super(c);
        }

        public Object eval(Environment env) {
            Object result = 0;
            for (ASTree t : this) {
                if (!(t instanceof NullStmnt))
                    result = ((ASTreeEx) t).eval(env);
            }
            return result;
        }
    }

    @Reviser
    public static class IfEx extends IfStmnt {
        public IfEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            // if文条件に合致
            Object c = ((ASTreeEx) condition()).eval(env);
            if (c instanceof Integer && ((Integer) c).intValue() != FALSE) {
                return ((ASTreeEx) thenBlock()).eval(env);
            }
            // elif文条件に合致
            int k = getElseIfNum();
            for(int i = 0; i < k; i++) {
                c = ((ASTreeEx)(elifStmnt().getElifCondition(i))).eval(env);
                if(c instanceof Integer && ((Integer)c).intValue() != FALSE) {
                    return ((ASTreeEx)(elifStmnt().getElifBlock(i))).eval(env);
                }
            }
            // else文に合致
            ASTree b = elseBlock();
            if(b == null) {
                return 0;
            } else {
                return ((ASTreeEx)b).eval(env);
            }
        }
    }

    @Reviser
    public static class WhileEx extends WhileStmnt {
        public WhileEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            Object result = 0;
            // While文を抜ける直前の最後に評価された値を返す
            while(true) {
                Object c = ((ASTreeEx)condition()).eval(env);
                if(c instanceof Integer && ((Integer)c).intValue() == FALSE) {
                    return result;
                } else {
                    result = ((ASTreeEx)body()).eval(env);
                }
            }
        }
    }
}
