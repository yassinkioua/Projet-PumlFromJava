package pumlFromJava;

import java.util.List;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        
       
        ArrayList<Element> classes = new ArrayList<Element>();
        
        if(d == null){
            d = ".";
        }
        for (Element element : environment.getSpecifiedElements()) {
            if(out == null){
                out = element.getSimpleName().toString() + ".puml";
            }
            classes.addAll(element.getEnclosedElements());
            
        } 
        
        // dcaDiagram dcD = new dcaDiagram();
        String filepath = d + "/" + out;
        dccDiagram dcD = new dccDiagram();
        try {
            FileWriter fw = new FileWriter(filepath);
            fw.write(dcD.generatePuml(classes, d, out));
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // PumlDiagram.generatePuml(classes, d, out);
        return true;
    }
    
}
