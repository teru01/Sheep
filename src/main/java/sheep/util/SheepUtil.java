package sheep.util;
import static sheep.core.BasicEvaluator.*;
public class SheepUtil {
    public static boolean isTrue(Object obj) {
        if(obj == null || obj.equals("") || (obj instanceof Integer && (Integer)obj == TRUE)) return false;
        return true;
    }
}
