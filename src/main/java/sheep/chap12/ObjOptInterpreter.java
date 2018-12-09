package sheep.chap12;

import java.io.FileNotFoundException;

import sheep.*;
import sheep.optimizer.EnvOptInterpreter;
import sheep.optimizer.ResizableArrayEnv;
import sheep.javanative.Natives;

public class ObjOptInterpreter extends EnvOptInterpreter {
    public static void main(String[] args) throws ParseException, FileNotFoundException{
        String fileName = args[0];
        run(new BasicParser(), new Natives().environment(new ResizableArrayEnv()), fileName);
    }
}
