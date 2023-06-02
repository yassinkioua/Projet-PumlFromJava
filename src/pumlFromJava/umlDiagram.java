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
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.print.DocFlavor.STRING;
import javax.sound.sampled.AudioFileFormat.Type;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Locale;
import java.util.Set;


abstract public class umlDiagram {
    protected String uml = "";
    protected Name currentPackage = null;
    protected ArrayList<String> liaison;
    protected ArrayList<Element> classes;

    public umlDiagram(ArrayList<Element> classes) {
        this.liaison = new ArrayList<>();
        this.classes = classes;
    }

    protected String generatePuml(String d, String out) {
        return uml;
    }

    protected String processInsideClass(Element e) {
        return "String";
    }
    
   
    protected String handleModifiers(Element e) {
        try {
            
            String r = "";
            for (Modifier mod : e.getModifiers()) {
                if (mod == Modifier.STATIC) {
                    r += ("{static} ");
                }
                if (mod == Modifier.PRIVATE) {
                    r += ("- ");
                }
                if (mod == Modifier.PUBLIC) {
                    r += ("+ ");
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
            }
            return r;
        } catch (Exception x) {
            return "CATCHED HANDLE MODIFIER ERROR";
        }
        
    }

    protected String processClass(Element e) {
        try {
            String r ="";
            
            
                
            if (e.getKind() == ElementKind.ENUM) {
                r += "enum " + e.getSimpleName().toString() + " <<enum>> { \n";

            } else if (e.getKind() == ElementKind.INTERFACE) {
                // r += "interface " + e.getSimpleName().toString() + " <<interface>> { \n";
                r += handleObjectTitle("interface",e) + "<<interface>>";

            } else if (e.getKind() == ElementKind.CLASS) {
                for (Modifier m : e.getModifiers()) {
                    if (m == Modifier.ABSTRACT) {
                       r += "abstract ";
                    }
                }
               r += handleObjectTitle("class",e);
                    


            }
            return r;
        }

        catch (Exception x) {
            // TODO: handle exception
        }
        return "ERROR";
        
    }
    protected String processLiaison(){
        String r="";
        for (String s : liaison) {
            r += s;
        }
        return r;
    }

    protected String openPackage(Element e){
        return("package " + e.getEnclosingElement().getSimpleName().toString() + "{\n" );
    }

    protected static String getNomSimple(TypeMirror tm) {
        String res = "";

        String[] nom = tm.toString().split("\\.");
        res = nom[nom.length-1];

        if(res.endsWith(">")){
            res = res.substring(0, res.length()-1);
        }
    
        return res;
    }

    protected static String getParams(Element e){
        String params ="";
        ExecutableElement tp = (ExecutableElement) e;

        List<String> test = new ArrayList<String>();
        for (VariableElement paramter : tp.getParameters()) {
            test.add(paramter.toString() + " : "+ getNomSimple(paramter.asType()));
        }
        params = String.join(", ", test);
        return params;
    }

    protected static String getNameWithParams(Element e){
        if(e.getKind() == ElementKind.CONSTRUCTOR){
            return e.getEnclosingElement().getSimpleName().toString() +"("+ getParams(e) +")";
        }
        else if(e.getKind() == ElementKind.METHOD){
            return e.getSimpleName().toString() +"("+ getParams(e) +")";
        }
        return "error";
    }

    protected String handleObjectTitle(String s, Element e){
        String r="";
        String ext ="";
        ArrayList<String> interfArray = new ArrayList<String>();
        TypeElement te = (TypeElement) e;
        if(!getNomSimple(te.getSuperclass()).equals("Object") && !getNomSimple(te.getSuperclass()).equals("none") ){
            ext = " extends " + getNomSimple(te.getSuperclass());
        }

        if(!te.getInterfaces().isEmpty()){
            for (TypeMirror inter : te.getInterfaces()) {
                interfArray.add(getNomSimple(inter));
                
            }

            String interf = String.join(",", interfArray);
            if (interf != null){
                r += s +" "+ e.getSimpleName().toString() + ext + " implements " + interf+   "{ \n";

            }
        }
        else{
            r += s +" "+ e.getSimpleName().toString() + ext + "{ \n";
        }

        return r;
    }

    protected boolean isCustomType(TypeMirror tm){
        boolean r = false;
        for (Element e : classes) {
            if(classes.toString().contains(getNomSimple(tm))){
                r = true;
            }
        }
        return r;
    }

}

