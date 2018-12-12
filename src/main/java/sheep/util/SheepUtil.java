package sheep.util;
import static sheep.core.BasicEvaluator.*;
public class SheepUtil {
    public static boolean isTrue(Object obj) {
        if(obj == null || (obj instanceof String && obj.equals("")) || (obj instanceof Integer && (Integer)obj == FALSE)) return false;
        return true;
    }
}
