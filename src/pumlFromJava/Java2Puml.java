package pumlFromJava;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.spi.ToolProvider;

public class Java2Puml
{
    private static String umlType;
    public static void main(String[] args)
    {   
        // String umlType = "DCA";
        String umlType = "DCC";

       
        String[] argument = {"-private","-sourcepath", "./exemples","-doclet" ,"pumlFromJava.PumlDoclet","-d","./uml","-out","westernDCA.puml","-umlType",umlType,"-docletpath" ,"","western" } ;
        //String[] argument = {"-private","-sourcepath", "./src","-doclet" ,"pumlFromJava.PumlDoclet","-d","./uml","-docletpath" ,"","pumlFromJava" } ;
        ToolProvider toolProvider = ToolProvider.findFirst("javadoc").get();
        System.out.println(toolProvider.name());
        toolProvider.run(System.out, System.err, argument);
    }
}