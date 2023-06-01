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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
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
    @Override
    public String generatePuml(ArrayList<Element> classes, String d, String out) {

        try {
            super.uml += ("@startuml\n");
            super.uml += ("'https://plantuml.com/class-diagram \n skinparam classAttributeIconSize 0 \nskinparam classFontStyle Bold\nskinparam style strictuml\nhide empty members\n");

            ArrayList<Element> temp = new ArrayList<Element>();
            for (Element element : classes) {

                if(element.getEnclosingElement().getSimpleName() != super.currentPackage){
                    if(super.currentPackage != null){
                        super.uml += "\n}\n";
                    }
                    super.uml += super.openPackage(element);
                    super.currentPackage = element.getEnclosingElement().getSimpleName();
                }

                super.uml += processClass(element);
                temp.addAll(element.getEnclosedElements());
               
                    for (Element e : temp) {
                        
                        super.uml += processInsideClass(e);
                    }
               
            
                super.uml +=("\n } \n");
                temp = new ArrayList<Element>();

            }
            super.uml += super.processLiaison();
            super.uml +=("}\n@enduml\n");

        } catch (Exception e) {
            // TODO: handle exception
        }
        return super.uml;
    }


    @Override
    protected String processInsideClass(Element e) {
        try {
            String r ="";
            if (e.getKind() == ElementKind.FIELD) {
                if (e.asType().getKind().isPrimitive()) {
                    handleModifiers(e);
                    r += (e.getSimpleName().toString() + " : " + e.asType().toString() + "\n");
                }
                else {
                    if(e.asType().toString().contains(e.getEnclosingElement().getEnclosingElement().getSimpleName().toString())){
                        super.liaison.add((e.getEnclosingElement().getSimpleName().toString() + " - " + super.getNomSimple(e.asType()) + "\n"));
                        return "";
                    }
                }
            
            }
            return r;
        } catch (Exception x) {
            return "CATCHED ERROR IN PROCESS INSIDE CLASS \n";
        }
    }
}
