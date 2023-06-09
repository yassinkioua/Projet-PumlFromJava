package pumlFromJava;

import java.util.List;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;
import javax.lang.model.type.ArrayType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Locale;
import java.util.Set;
import java.io.FileWriter;

class dcaDiagram extends umlDiagram {
    public dcaDiagram(ArrayList<Element> classes) {
        super(classes);
    }

    @Override
    public String generatePuml(String d, String out) {

        try {

            super.uml += ("@startuml\n");
            super.uml += ("'https://plantuml.com/class-diagram \n skinparam classAttributeIconSize 0 \nskinparam classFontStyle Bold\nskinparam style strictuml\nhide empty members\n");

            ArrayList<Element> temp = new ArrayList<Element>();
            for (Element element : classes) {

                if (element.getEnclosingElement().getSimpleName() != super.currentPackage) {
                    if (super.currentPackage != null) {
                        super.uml += "\n}\n";
                    }
                    super.uml += super.openPackage(element);
                    super.currentPackage = element.getEnclosingElement().getSimpleName();
                }
                super.currentClasse = element;
                super.uml += processClass(element);
                temp.addAll(element.getEnclosedElements());

                for (Element e : temp) {

                    super.uml += processInsideClass(e);
                }

                super.uml += ("\n } \n");
                temp = new ArrayList<Element>();

            }
            super.uml += super.processLiaison();
            super.uml += ("}\n@enduml\n");

        } catch (Exception e) {
            // TODO: handle exception
        }
        return super.uml;
    }

    @Override
    protected String handleLiaison(String source, String target) {
        String r = "";
        String label = "";

        r += source + "-" + target + "\n";

        return r;
    }

    @Override
    protected String processInsideClass(Element e) {
        try {
            String r = "";
            if (e.getKind() == ElementKind.FIELD) {

                // System.out.println("e : " + e + " | " + " type : " + getNomSimple(e.asType()) + " typeKind : "
                //         + e.asType().getKind() + " | " + "\n");
                // System.out.println(currentClasse.getSimpleName() +" " +e.getSimpleName()+ " "+e.asType().getKind() + " " + e.asType().getKind().isPrimitive());
                if (e.asType().getKind().isPrimitive() || isBoxedPrimitiveType(e.asType())) {
                    
                    r += (e.getSimpleName().toString() + "\n");
                } 

                else if (e.asType() instanceof ArrayType) {

                    if(isCustomType(getDeepType(e.asType()))){
                        liaison.add(handleLiaison(currentClasse.getSimpleName().toString(), getNomSimple(getDeepType(e.asType()))));
                    }
                    else{                        
                        r += (e.getSimpleName().toString() + "\n");
                    }
                } 

                else if (e.asType() instanceof DeclaredType) {
                    if(getDeepType(e.asType()).getKind().isPrimitive() || isBoxedPrimitiveType(getDeepType(e.asType()))){
                        r += (e.getSimpleName().toString() + "\n");
                     }
                     else if (isCustomType(getDeepType(e.asType()))) {
                        liaison.add(handleLiaison(currentClasse.getSimpleName().toString(), getNomSimple(getDeepType(e.asType()))));
                     }
                 
                } 
                else if (isCustomType(e.asType())) {
                        super.liaison.add(handleLiaison(e.getEnclosingElement().getSimpleName().toString(),
                                getNomSimple(e.asType())));
                        return "";
                    }

                

            }
            return r;
        } catch (Exception x) {
            return "CATCHED ERROR IN PROCESS INSIDE CLASS \n";
        }
    }
}
