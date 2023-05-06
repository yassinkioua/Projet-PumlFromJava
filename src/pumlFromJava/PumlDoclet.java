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
import java.util.Locale;
import java.util.Set;


public class PumlDoclet implements Doclet {
    private String out = null; 
    private String d = null;
    abstract class Option implements Doclet.Option {
        private final String name;
        private final boolean hasArg;
        private final String description;
        private final String parameters;
    
        Option(String name, boolean hasArg,
               String description, String parameters) {
            this.name = name;
            this.hasArg = hasArg;
            this.description = description;
            this.parameters = parameters;
        }
    
        @Override
        public int getArgumentCount() {
            return hasArg ? 1 : 0;
        }
    
        @Override
        public String getDescription() {
            return description;
        }
    
        @Override
        public Kind getKind() {
            return Kind.STANDARD;
        }
    
        @Override
        public List<String> getNames() {
            return List.of(name);
        }
    
        @Override
        public String getParameters() {
            return hasArg ? parameters : "";
        }
    }
    private final Set<Option> options = Set.of(
            
            new Option("-out", true, "sets the name of the puml file", "<string>") {
                @Override
                public boolean process(String option,
                                       List<String> arguments) {
                    out = arguments.get(0);
                    return true;
                }
            },
            new Option("-d", true,"sets the dir to write the puml in", "<string>"){

                @Override
                public boolean process(String option, List<String> arguments){
                    d = arguments.get(0);
                    return true;
                }
            }
    );

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
        return options;
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
        if(d == null){
            d = ".";
        }
        for (Element element : environment.getSpecifiedElements()) {
            if(out == null){
                out = element.getSimpleName().toString() + ".puml";
            }
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
            String filepath = d+"/"+out;
            System.out.println(filepath);
            FileWriter fw = new FileWriter(filepath);
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
        // System.out.println(classes);
        // System.out.println(environment);
        System.out.println(out);
        System.out.println(d);
        return true;
    }
    
}
