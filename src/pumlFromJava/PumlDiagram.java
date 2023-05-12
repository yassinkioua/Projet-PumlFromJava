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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Locale;
import java.util.Set;

public class PumlDiagram {

    public static void generatePuml(ArrayList<Element> classes, String d, String out) {

        try {

            String filepath = d + "/" + out;

            FileWriter fw = new FileWriter(filepath);
            fw.write("@startuml\n");
            fw.write("'https://plantuml.com/class-diagram \nskinparam classAttributeIconSize 0 \nskinparam classFontStyle Bold\nskinparam style strictuml\nhide empty members\n");

            

            ArrayList<Element> temp = new ArrayList<Element>();

            for (Element element : classes) {
                processClass(fw, element);
                temp.addAll(element.getEnclosedElements());

                for (Element e : temp) {
                    // System.out.println(e);
                    // fw.write(e.toString() + "\n");
                    attributeWrite(fw, e);
                    // System.out.println(e.asType());
                    // System.out.println(e.getSimpleName()); donne le nom de la methode sans
                    // parenthese et sans les param
                    // System.out.println(e.getModifiers()); donne les info du genre public ou
                    // private et final
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

    private static void attributeWrite(FileWriter fw, Element e) {
        try {
            if (e.getKind() == ElementKind.FIELD) {
                processInsideClass(fw, e);
                fw.write(e.toString() + "\n");
            } else if (e.getKind() == ElementKind.CONSTRUCTOR) {
                processInsideClass(fw, e);
                fw.write("<<Create>> " + e.toString() + "\n");
            }

            else if (e.getKind() == ElementKind.METHOD) {
                processInsideClass(fw, e);
                fw.write(e.toString() + "\n");
            }

            // System.out.println(e.asType().toString());
            // System.out.println(e.getKind());
            // System.out.println(e.getModifiers());

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    static private void processInsideClass(FileWriter fw, Element e) {
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