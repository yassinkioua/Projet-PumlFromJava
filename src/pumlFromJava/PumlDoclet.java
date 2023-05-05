package pumlFromJava;

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
import java.util.Locale;
import java.util.Set;

public class PumlDoclet implements Doclet {

    @Override
    public void init(Locale locale, Reporter reporter) {  }
    
    @Override
    public String getName() {
        // For this doclet, the name of the doclet is just the
        // simple name of the class. The name may be used in
        // messages related to this doclet, such as in command-line
        // help when doclet-specific options are provided.
        return getClass().getSimpleName();
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        // This doclet does not support any options.
        return Collections.emptySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // This doclet supports all source versions.
        // More sophisticated doclets may use a more
        // specific version, to ensure that they do not
        // encounter more recent language features that
        // they may not be able to handle.
        return SourceVersion.latest();
    }

    @Override
    public boolean run(DocletEnvironment environment) {
        // TODO Auto-generated method stub
        
        ArrayList<String> classNames = new ArrayList<String>();
        ArrayList<Element> classes = new ArrayList<Element>();
        
        for (Element element : environment.getSpecifiedElements()) {
            classes.addAll(element.getEnclosedElements());
            classNames.add(element.getEnclosedElements().toString());
        } 
        ArrayList<Element> temp = new ArrayList<Element>();
        
        for(Element element : classes){
            // System.out.println(element.getEnclosedElements());
            // temp.addAll(element.getEnclosedElements());
            
            // for (Element e : temp) {
                //     System.out.println(e);
                //     System.out.println(e.asType());
                // }
                
            // System.out.println("_____________________");
            
        }
        try {
            FileWriter fw = new FileWriter("PumlDiagramm.puml");
            fw.write("@startuml\n");    
            fw.write("skinparam style strictuml\n");
            
            for (String s : classNames.get(0).split(",")){
                fw.write("class " + s + "\n");
            }
            fw.write("@enduml\n");
            fw.close();
            
        } catch (Exception e) {
            // TODO: handle exception
        }
            
            
        
        
        // System.out.println(classNames);
        System.out.println(classes);
        return true;
    }
    
}
