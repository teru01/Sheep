package sheep.chap12;

import java.io.FileNotFoundException;

import sheep.ClassParser;
import sheep.ParseException;
import sheep.chap11.EnvOptInterpreter;
import sheep.chap11.ResizableArrayEnv;
import sheep.chap8.Natives;

public class ObjOptInterpreter extends EnvOptInterpreter {
    public static void main(String[] args) throws ParseException, FileNotFoundException{
        String fileName = args[0];
        run(new ClassParser(), new Natives().environment(new ResizableArrayEnv()), fileName);
    }
}