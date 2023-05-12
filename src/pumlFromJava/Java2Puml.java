package pumlFromJava;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.spi.ToolProvider;

public class Java2Puml
{
    public static void main(String[] args)
    {
        //String[] argument = {"-private","-sourcepath", "./exemples","-doclet" ,"pumlFromJava.PumlDoclet","-d","./uml","-docletpath" ,"","western" } ;
        String[] argument = {"-private","-sourcepath", "./src","-doclet" ,"pumlFromJava.PumlDoclet","-d","./uml","-docletpath" ,"","pumlFromJava" } ;
        ToolProvider toolProvider = ToolProvider.findFirst("javadoc").get();
        System.out.println(toolProvider.name());
        toolProvider.run(System.out, System.err, argument);
    }
}