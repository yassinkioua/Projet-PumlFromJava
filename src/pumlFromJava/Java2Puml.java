package pumlFromJava;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.spi.ToolProvider;

public class Java2Puml
{
    public static void main(String[] args)
    {
        String[] argument = {"-private","-sourcepath", "./exemples","-doclet" ,"pumlFromJava.FirstDoclet","-docletpath" ,"","western" } ;
        ToolProvider toolProvider = ToolProvider.findFirst("javadoc").get();
        System.out.println(toolProvider.name());

/*
    javadoc -private -sourcepath <src> -doclet pumlFromJava.FirstDoclet -docletpath out/production/<projet>
      <package> ... <fichiers>
 */
        toolProvider.run(System.out, System.err, argument);
    }
}