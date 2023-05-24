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


abstract public class umlDiagram {
    protected String uml = "";

    protected String generatePuml(ArrayList<Element> classes, String d, String out) {
        return uml;
    }

    protected String processInsideClass(Element e) {
        return "String";
    }

    protected String handleModifiers(Element e) {
        try {
            String r = "";
            for (Modifier mod : e.getModifiers()) {
                if (mod == Modifier.PRIVATE) {
                    r += ("- ");
                }
                if (mod == Modifier.PUBLIC) {
                    r += ("+ ");
                }
                if (mod == Modifier.STATIC) {
                    r += ("{static} ");
                }
                if (mod == Modifier.FINAL) {
                    r += ("final ");
                }
                if (mod == Modifier.ABSTRACT) {
                    r += ("{abstract} ");
                }
                if (mod == Modifier.PROTECTED) {
                    r += ("# ");
                }
                return r;
            }
        } catch (Exception x) {
            return "CATCHED ERROR";
        }
        return "ERROR";
    }

    protected String processClass(Element e) {
        try {
            String r ="";
            if (e.getKind() == ElementKind.ENUM) {
                r += "enum " + e.getSimpleName().toString() + " <<enum>> { \n";

            } else if (e.getKind() == ElementKind.INTERFACE) {
                r += "interface " + e.getSimpleName().toString() + " <<interface>> { \n";

            } else if (e.getKind() == ElementKind.CLASS) {
                for (Modifier m : e.getModifiers()) {
                    if (m == Modifier.ABSTRACT) {
                       r += "abstract ";
                    }
                }

                r += "class " + e.getSimpleName().toString() + "{ \n";

            }
            return r;
        }

        catch (Exception x) {
            // TODO: handle exception
        }
        return "ERROR";
        
    }
}
