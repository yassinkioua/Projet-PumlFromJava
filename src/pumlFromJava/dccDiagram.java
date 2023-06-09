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
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
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
    public dccDiagram(ArrayList<Element> classes) {
        super(classes);
    }

    @Override
    protected String generatePuml(String d, String out) {

        try {
            super.uml += "@startuml\n";
            super.uml += "'https://plantuml.com/class-diagram \n skinparam classAttributeIconSize 0 \nskinparam classFontStyle Bold\nskinparam style strictuml\nhide empty members\n";

            ArrayList<Element> temp = new ArrayList<Element>();
            // parcourt les classe dans la liste classes
            for (Element element : super.classes) {
                // traite la gestion des package
                if (element.getEnclosingElement().getSimpleName() != super.currentPackage) {
                    if (super.currentPackage != null) {
                        super.uml += "\n}\n";
                    }
                    super.uml += super.openPackage(element);
                    super.currentPackage = element.getEnclosingElement().getSimpleName();
                }

                // traite la classe courante
                super.currentClasse = element;
                super.uml += processClass(element);

                // traite l'interieur de la classe
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
    protected String processInsideClass(Element e) {
        Element elementClass = e.getEnclosingElement();
        TypeMirror elementType = e.asType();
        TypeMirror elementDeepType = getDeepType(elementType);
        ElementKind elementKind = e.getKind();
        

        try {
            String r = "";
            
            if (elementClass.getKind() == ElementKind.ENUM) {
                if (elementKind == ElementKind.ENUM_CONSTANT) {
                    r += e.toString() + "\n";
                }

            } 
            else if (elementKind == ElementKind.FIELD) {
                if (elementType.getKind().isPrimitive() || isBoxedPrimitiveType(elementType)) {
                    r += handleModifiers(e);
                    r += (e.getSimpleName().toString() + " : " + rework_type(getNomSimple(elementType)) + handleFinal(e)+ "\n");
                } 
                
                else if (elementType instanceof ArrayType) {

                    if(isCustomType(elementDeepType)){
                        liaison.add(handleLiaison(currentClasse.getSimpleName().toString(), getNomSimple(elementDeepType),e.getSimpleName().toString(),"*"));
                    }
                                          
                    else{
                        r += handleModifiers(e);
                        r += (e.getSimpleName().toString() + " : " + rework_type(removeBracket(getNomSimple(elementDeepType))) +"[*]"+ handleFinal(e)+"\n");
                    
                        
                    }

                } 

                else if (elementType instanceof DeclaredType) {
                    if (isCustomType(elementDeepType)) {
                        liaison.add(handleLiaison(currentClasse.getSimpleName().toString(), getNomSimple(elementDeepType),e.getSimpleName().toString(),"*"));
                    }
                    else{
                        r += handleModifiers(e);
                        r += (e.getSimpleName().toString() + " : " + rework_type(removeBracket(getNomSimple(elementDeepType))) +"[*]"+handleFinal(e)+ "\n");

                    }
                 
                } 

                else if (isCustomType(elementType)) {
                        super.liaison.add(handleLiaison(e.getEnclosingElement().getSimpleName().toString(),
                                getNomSimple(elementType),e.getSimpleName().toString(),""));
                        return "";
                    }
                }


            else if (elementKind == ElementKind.CONSTRUCTOR) {
                r += handleModifiers(e);
                r += ("<<Create>> " + super.getNameWithParams(e) + "\n");

            }

            else if (elementKind == ElementKind.METHOD) {
                r += handleModifiers(e);

          
                    
                    ExecutableType execType = (ExecutableType) elementType;
                    if(execType.getReturnType() instanceof ArrayType || execType.getReturnType() instanceof DeclaredType ){
                        r += (super.getNameWithParams(e) + " : " + rework_type(removeBracket(getNomSimple(getDeepType(execType.getReturnType())))) + "[*]"+"\n");
                    }
                    else{
                        r += (super.getNameWithParams(e) + " : " + rework_type(getNomSimple(execType.getReturnType())) + "\n");

                    }
                
            }
            return r;

        } catch (Exception x) {
            return "CATCHED PROCESS INSIDE CLASS ERROR";
        }

    }

    @Override
    protected String handleLiaison(String source, String target,String label,String card) {
        String r="";
        
        r += source + " --o" +" \" " +card+ " "+ label+" \""+ target + "\n";
        //  A --o " * \t caca" B
        
            

        return r;
    }
}