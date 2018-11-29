package sheep.chap12;

import sheep.ClassParser;
import sheep.ParseException;
import sheep.chap11.EnvOptInterpreter;
import sheep.chap11.ResizableArrayEnv;
import sheep.chap8.Natives;

public class ObjOptInterpreter extends EnvOptInterpreter {
    public static void main(String[] args) throws ParseException {
        run(new ClassParser(), new Natives().environment(new ResizableArrayEnv()));
    }
}
