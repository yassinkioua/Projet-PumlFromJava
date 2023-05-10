package pumlFromJava;

import java.util.List;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import java.io.File;
import java.io.FileWriter;


import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Locale;
import java.util.Set;

public class PumlDiagram{

    public static void generatePuml(ArrayList<Element> classes,String d, String out){

        try {

            String filepath = d+"/"+out;

            FileWriter fw = new FileWriter(filepath);
            fw.write("@startuml\n");    
            fw.write("skinparam style strictuml\n");
            
            ArrayList<Element> temp = new ArrayList<Element>();
            
            for(Element element : classes){
                fw.write("class " + element.getSimpleName() + "{ \n");
                // System.out.println(element.getEnclosedElements());
                    temp.addAll(element.getEnclosedElements());
                    
                   
                    
                    for (Element e : temp) {
                        // System.out.println(e);
                        fw.write(e.toString() + "\n");
                        // System.out.println(e.asType());
                        // System.out.println(e.getSimpleName()); donne le nom de la methode sans parenthese et sans les param
                        // System.out.println(e.getModifiers()); donne les info du genre public ou private et final 
                    }
                    fw.write("\n } \n");
                    temp = new ArrayList<Element>();
        
                }
            
                
            fw.write("@enduml\n");
            fw.close();
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    @Override
    public String toString() {
        return "PumlDiagram []";
    }
}