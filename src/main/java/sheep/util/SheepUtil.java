package sheep.util;

public class SheepUtil {
    public static boolean isTrue(Object obj) {
        if(obj == null
        || (obj instanceof Boolean && (boolean)obj == false)
        || (obj instanceof Integer && (Integer)obj == 0)
        || (obj instanceof String && obj.equals("")))
        {
                return false;
        }
        return true;
    }
}
