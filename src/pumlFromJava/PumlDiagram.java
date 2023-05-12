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
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Locale;
import java.util.Set;

public class PumlDiagram {

    public static void generatePuml(ArrayList<Element> classes, String d, String out, PumlType UmlType) {

        try {

            String filepath = d + "/" + out;

            FileWriter fw = new FileWriter(filepath);
            fw.write("@startuml\n");
            fw.write(
                    "'https://plantuml.com/class-diagram \n skinparam classAttributeIconSize 0 \nskinparam classFontStyle Bold\nskinparam style strictuml\nhide empty members\n");

            ArrayList<Element> temp = new ArrayList<Element>();

            for (Element element : classes) {

                processClass(fw, element);
                temp.addAll(element.getEnclosedElements());
               
                    for (Element e : temp) {
                        processInsideClass(fw, e,UmlType);
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

    static private void processInsideClass(FileWriter fw, Element e, PumlType umlType) {
        try {

            if (e.getKind() == ElementKind.FIELD) {
                if (e.asType().getKind().isPrimitive()) {
                    handleModifiers(fw, e);
                    fw.write(e.getSimpleName().toString() + " : " + e.asType().toString() + "\n");
                }

            } else if (e.getKind() == ElementKind.CONSTRUCTOR && umlType == PumlType.DCC) {
                handleModifiers(fw, e);
                fw.write("<<Create>> " + e.toString() + "\n");
            } else if (e.getKind() == ElementKind.METHOD && umlType == PumlType.DCC) {
                handleModifiers(fw, e);
                System.out.println(e.asType());
                if (e.asType().toString().endsWith("java.lang.String")) {
                    fw.write(e.toString() + " : String \n");
                } else {
                    ExecutableType execType = (ExecutableType) e.asType();
                    fw.write(e.toString() + " : " + execType.getReturnType() + "\n");
                }

            }

        } catch (Exception x) {
            // TODO: handle exception
        }
    }

    private static void handleModifiers(FileWriter fw, Element e) {
        try {
            for (Modifier mod : e.getModifiers()) {
                if (mod == Modifier.PRIVATE) {
                    fw.write("- ");
                }
                if (mod == Modifier.PUBLIC) {
                    fw.write("+ ");
                }
                if (mod == Modifier.STATIC) {
                    fw.write("{static} ");
                }
                if (mod == Modifier.FINAL) {
                    fw.write("final ");
                }
                if (mod == Modifier.ABSTRACT) {
                    fw.write("{abstract} ");
                }
                if (mod == Modifier.PROTECTED) {
                    fw.write("# ");
                }
            }
        } catch (Exception x) {
            // TODO: handle exception
        }
    }

    private static void processClass(FileWriter fw, Element e) {
        try {
            if (e.getKind() == ElementKind.ENUM) {
                fw.write("enum " + e.getSimpleName().toString() + " <<enum>> { \n");

            } else if (e.getKind() == ElementKind.INTERFACE) {
                fw.write("interface " + e.getSimpleName().toString() + " <<interface>> { \n");

            } else if (e.getKind() == ElementKind.CLASS) {
                for (Modifier m : e.getModifiers()) {
                    if (m == Modifier.ABSTRACT) {
                        fw.write("abstract ");
                    }
                }

                fw.write("class " + e.getSimpleName().toString() + "{ \n");

            }
        }

        catch (Exception x) {
            // TODO: handle exception
        }
    }

    @Override
    public String toString() {
        return "PumlDiagram []";
    }
}