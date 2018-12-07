package sheep.chap12;
import java.util.*;
import sheep.*;
import sheep.ast.*;
import sheep.chap6.*;
import javassist.gluonj.*;

@Require(ObjOptimizer.class)
@Reviser
public class InlineCache {
    @Reviser
    public static class DotEx2 extends ObjOptimizer.DotEx {
        protected OptClassInfo savedClassInfo = null;
        protected boolean isField;
        protected int index;

        public DotEx2(List<ASTree> c) {
            super(c);
        }

        @Override
        public Object eval(Environment env, Object value) {
            if(value instanceof OptSheepObject) {
                OptSheepObject target = (OptSheepObject)value;
                if(target.classInfo() != this.savedClassInfo) {
                    updateCache(target);
                }
                if(this.isField) {
                    return target.read(this.index);
                } else {
                    return target.method(this.index);
                }
            } else {
                return super.eval(env, value);
            }
        }

        protected void updateCache(OptSheepObject target) {
            String member = name();
            this.savedClassInfo = target.classInfo();
            Integer i = this.savedClassInfo.fieldIndex(member);
            if(i != null) {
                this.isField = true;
                this.index = i;
                return;
            }
            i = this.savedClassInfo.methodIndex(member);
            if(i != null) {
                this.isField = false;
                this.index = i;
                return;
            }
            throw new SheepException("bad member access", this);
        }
    }
    @Reviser public static class AssignEx2 extends ObjOptimizer.AssignEx {
        protected OptClassInfo savedClassInfo = null;
        protected int index;

        public AssignEx2(List<ASTree> c) {
            super(c);
        }
        @Override
        protected Object setField(OptSheepObject obj, Dot expr, Object rvalue) {
            if(obj.classInfo() != this.savedClassInfo) {
                String member = expr.name();
                this.savedClassInfo = obj.classInfo();
                Integer i = this.savedClassInfo.fieldIndex(member);
                if(i == null) {
                    throw new SheepException("bad member access", this);
                }
                this.index = i;
            }
            obj.write(index, rvalue);
            return rvalue;
        }
    }
}
