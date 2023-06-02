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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.print.attribute.standard.Sides;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Locale;
import java.util.Set;

public class dccDiagram extends umlDiagram {
    public dccDiagram(ArrayList<Element> classes){
        super(classes);
    }
    @Override
    protected String generatePuml(String d, String out) {

        try {
            super.uml += "@startuml\n";
            super.uml +="'https://plantuml.com/class-diagram \n skinparam classAttributeIconSize 0 \nskinparam classFontStyle Bold\nskinparam style strictuml\nhide empty members\n";
            
            ArrayList<Element> temp = new ArrayList<Element>();
            //parcourt les classe dans la liste classes
            for (Element element : super.classes) {
                //traite la gestion des package
                if(element.getEnclosingElement().getSimpleName() != super.currentPackage){
                    if(super.currentPackage != null){
                        super.uml += "\n}\n";
                    }
                    super.uml += super.openPackage(element);
                    super.currentPackage = element.getEnclosingElement().getSimpleName();
                }

                // traite la classe courante
                super.uml += processClass(element);

                // traite l'interieur de la classe
                temp.addAll(element.getEnclosedElements());
               
                    for (Element e : temp) {
                        
                        super.uml += processInsideClass(e);
                    }
               
            
                super.uml+=("\n } \n");
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
    protected String processInsideClass(Element e) {
        try {
            String r = "";
            if(e.getEnclosingElement().getKind() == ElementKind.ENUM){
                if(e.getKind() == ElementKind.ENUM_CONSTANT){
                    r += e.toString() + "\n"; 
                }


            }
            else if (e.getKind() == ElementKind.FIELD) {
                if (e.asType().getKind().isPrimitive()) {
                    r += handleModifiers(e);
                    r += (e.getSimpleName().toString() + " : " + e.asType().toString() + "\n");
                }
                else {
                    // isCustomType(e.asType());

                    if(isCustomType(e.asType())){
                        super.liaison.add((e.getEnclosingElement().getSimpleName().toString() + " - " + super.getNomSimple(e.asType()) + "\n"));
                        return "";
                    }
                }
                

            }

            else if (e.getKind() == ElementKind.CONSTRUCTOR) {
                r += handleModifiers(e);
                r += ("<<Create>> " + super.getNameWithParams(e) +"\n");
                
               
            }
            
            else if (e.getKind() == ElementKind.METHOD) {
                r += handleModifiers(e);
                
                if (e.asType().toString().endsWith("java.lang.String")) {
                  
                    r += (super.getNameWithParams(e) + " : " + super.getNomSimple(e.asType())+"\n");
                }
                else if(e.asType().toString().endsWith("void")){
                    r += (super.getNameWithParams(e) + "\n" );
                }
                else {
                    ExecutableType execType = (ExecutableType) e.asType();
                    r += (super.getNameWithParams(e) + " : " + super.getNomSimple(execType.getReturnType()) + "\n");
                }
            } 
            return r;
            

        } catch (Exception x) {
            return "CATCHED PROCESS INSIDE CLASS ERROR";
        }
       
    }
}