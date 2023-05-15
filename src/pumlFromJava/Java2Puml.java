package pumlFromJava;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.spi.ToolProvider;

public class Java2Puml
{
    public static void main(String[] args)
    {
        boolean arg_juste = false;
        String umltype = "";
        while (!arg_juste)
        {
            System.out.println("Voulez vous un DCA ou un DCC");
            Scanner c = new Scanner(System.in);
            String s = c.nextLine();
            if (s == "DCA" || s == "dca" || s == "DCC" || s == "dcc" )
            {
                umltype = s.toUpperCase();
                arg_juste = true;
            }
            else
            {
                System.out.println("Votre argument n'est pas valable, merci de réessayer !");
            }
        }
        String[] argument = {"-private","-sourcepath", "./exemples","-doclet" ,"pumlFromJava.PumlDoclet","-d","./uml","-out","westernDCC.puml","-type",umltype," -docletpath" ,"","western" } ;
        // String[] argument = {"-private","-sourcepath", "./src","-doclet" ,"pumlFromJava.PumlDoclet","-d","./uml","-docletpath" ,"","pumlFromJava" } ;
        ToolProvider toolProvider = ToolProvider.findFirst("javadoc").get();
        System.out.println(toolProvider.name());
        toolProvider.run(System.out, System.err, argument);
    }
}