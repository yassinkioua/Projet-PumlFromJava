package pumlFromJava;

import java.util.List;
import java.util.regex.Pattern;
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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.print.DocFlavor.STRING;
import javax.sound.sampled.AudioFileFormat.Type;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.util.Set;

abstract public class umlDiagram {
    protected String uml = "";
    protected Name currentPackage = null;
    protected ArrayList<String> liaison;
    protected ArrayList<Element> classes;
    protected Element currentClasse;

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
            ArrayList<Modifier> mods = new ArrayList<Modifier>();

            for (Modifier mod : e.getModifiers()) {
                mods.add(mod);
            }

            r += handleVisibilty(mods);

            if (mods.contains(Modifier.STATIC)) {
                r += ("{static} ");
            }
            if (mods.contains(Modifier.ABSTRACT)) {
                r += ("{abstract} ");
            }

            return r;
        } catch (Exception x) {
            return "CATCHED HANDLE MODIFIER ERROR";
        }
    }

    private String handleVisibilty(ArrayList<Modifier> visi) {
        String r = "";
        if (visi.contains(Modifier.PRIVATE)) {
            r += ("- ");

        } else if (visi.contains(Modifier.PUBLIC)) {
            r += ("+ ");
        } else if (visi.contains(Modifier.PROTECTED)) {
            r += ("# ");
        } else {
            r += ("~ ");
        }
        return r;
    }

    protected String handleFinal(Element e) {
        String r = "";

        ArrayList<Modifier> mods = new ArrayList<Modifier>();

        for (Modifier mod : e.getModifiers()) {
            mods.add(mod);
        }

        if (mods.contains(Modifier.FINAL)) {
            r = "{READ ONLY}";
        }
        return r;
    }

    protected String processClass(Element e) {
        try {
            String r = "";

            if (e.getKind() == ElementKind.ENUM) {
                r += "enum " + e.getSimpleName().toString() + " <<enum>> { \n";

            } else if (e.getKind() == ElementKind.INTERFACE) {
                // r += "interface " + e.getSimpleName().toString() + " <<interface>> { \n";
                r += handleObjectTitle("interface", e) + "<<interface>>";

            } else if (e.getKind() == ElementKind.CLASS) {
                for (Modifier m : e.getModifiers()) {
                    if (m == Modifier.ABSTRACT) {
                        r += "abstract ";
                    }
                }
                r += handleObjectTitle("class", e);

            }
            return r;
        }

        catch (Exception x) {
            // TODO: handle exception
        }
        return "ERROR";

    }

    protected String processLiaison() {
        String r = "";
        for (String s : liaison) {
            r += s;
        }
        return r;
    }

    protected String openPackage(Element e) {
        return ("package " + e.getEnclosingElement().getSimpleName().toString() + "{\n");
    }

    protected String getNomSimple(TypeMirror tm) {

        if (tm instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) tm;
            TypeMirror componentType = arrayType.getComponentType();
            String componentres = getNomSimple(componentType);

            return componentres + "[]";
        } else if (tm instanceof DeclaredType) {
            DeclaredType declaredType = (DeclaredType) tm;
            TypeElement typeElement = (TypeElement) declaredType.asElement();
            String res = typeElement.getSimpleName().toString();

            TypeMirror enclosingType = declaredType.getEnclosingType();

            if (enclosingType.getKind() != TypeKind.NONE) {
                String enclosingres = getNomSimple(enclosingType);
                return enclosingres + "." + res;
            }

            List<? extends TypeMirror> typeArgumentsList = declaredType.getTypeArguments();
            if (!typeArgumentsList.isEmpty()) {
                res += "<";
                boolean first = true;
                for (TypeMirror typeArg : typeArgumentsList) {
                    if (!first) {
                        res += ", ";
                    }
                    res += getNomSimple(typeArg);
                    first = false;
                }
                res += ">";
            }

            return rework_type(removeBracket(res));
        }

        return tm.toString();
    }

    protected String getParams(Element e) {
        String params = "";
        ExecutableElement tp = (ExecutableElement) e;

        List<String> test = new ArrayList<String>();
        for (VariableElement paramter : tp.getParameters()) {
            System.out.println(paramter.asType().getKind());
            if(paramter.asType() instanceof ArrayType || paramter.asType() instanceof DeclaredType){

                test.add(paramter.toString() + " : " + rework_type(removeBracket(getNomSimple(getDeepType(paramter.asType())))) +"[*]");
            }
            else{

                test.add(paramter.toString() + " : " + rework_type(getNomSimple(paramter.asType())));
            }
            
        }
        params = String.join(", ", test);
        return params;
    }

    protected String getNameWithParams(Element e) {
        if (e.getKind() == ElementKind.CONSTRUCTOR) {
            return e.getEnclosingElement().getSimpleName().toString() + "(" + getParams(e) + ")";
        } else if (e.getKind() == ElementKind.METHOD) {
            return e.getSimpleName().toString() + "(" + getParams(e) + ")";
        }
        return "error";
    }

    protected String handleObjectTitle(String s, Element e) {
        String r = "";
        String ext = "";
        ArrayList<String> interfArray = new ArrayList<String>();
        TypeElement te = (TypeElement) e;
        if (!getNomSimple(te.getSuperclass()).equals("Object") && !getNomSimple(te.getSuperclass()).equals("none")) {
            ext = " extends " + getNomSimple(te.getSuperclass());
        }

        if (!te.getInterfaces().isEmpty()) {
            for (TypeMirror inter : te.getInterfaces()) {
                interfArray.add(getNomSimple(inter));

            }

            String interf = String.join(",", interfArray);
            if (interf != null) {
                r += s + " " + e.getSimpleName().toString() + ext + " implements " + interf + "{ \n";

            }
        } else {
            r += s + " " + e.getSimpleName().toString() + ext + "{ \n";
        }

        return r;
    }

    protected boolean isCustomType(TypeMirror tm) {
        boolean r = false;
        for (Element e : classes) {
            if (classes.toString().contains(getNomSimple(tm))) {
                r = true;
            }
        }
        return r;
    }

    protected boolean isComposition(Element e) {
        boolean r = false;
        TypeMirror tm = e.asType();
        String className = e.getEnclosingElement().getSimpleName().toString();
        if (className.contains(getNomSimple(tm))) {
            r = true;
        }
        return r;
    }

    protected String handleLiaison(String source, String target) {
        return "";
    }

    protected boolean isBoxedPrimitiveType(TypeMirror type) {
        return type.toString().equals("java.lang.Long") ||
                type.toString().equals("java.lang.Double") ||
                type.toString().equals("java.lang.Integer") ||
                type.toString().equals("java.lang.Float") ||
                type.toString().equals("java.lang.Short") ||
                type.toString().equals("java.lang.Byte") ||
                type.toString().equals("java.lang.Character") ||
                type.toString().equals("java.lang.Boolean") ||
                false;
    }

    protected TypeMirror getDeepType(TypeMirror tm) {

        TypeMirror r = tm;
        if (tm instanceof ArrayType) {
            ArrayType at = (ArrayType) tm;
            if (at.getComponentType() instanceof ArrayType || at.getComponentType() instanceof DeclaredType) {
                r = getDeepType(at.getComponentType());
            } else {
                r = at;
            }
        } else if (tm instanceof DeclaredType) {
            DeclaredType declaredType = (DeclaredType) tm;
            List<? extends TypeMirror> typeArgumentsList = declaredType.getTypeArguments();
            TypeElement typeElement = (TypeElement) declaredType.asElement();
            boolean first = true;

            if (!typeArgumentsList.isEmpty()) {
                for (TypeMirror typeArg : typeArgumentsList) {
                    if (typeElement.getSimpleName().toString().equals("Map")) {
                        if (!first) {
                            if (typeArg instanceof ArrayType || typeArg instanceof DeclaredType) {
                                r = getDeepType(typeArg);
                            } else {
                                r = typeArg;
                            }

                        } else {
                            first = false;
                        }
                    } else {
                        if (typeArg instanceof ArrayType || typeArg instanceof DeclaredType) {
                            r = getDeepType(typeArg);
                        } else {
                            r = typeArg;
                        }
                    }
                }
            }

        }
        return r;
    }

    protected boolean isMultipleArray(TypeMirror tm) {
        boolean r = false;

        if (tm instanceof ArrayType) {
            ArrayType at = (ArrayType) tm;
            if (at.getComponentType() instanceof ArrayType) {
                r = true;
                System.out.println("| type mirror :" + tm + " | inside tm : " + at.getComponentType()
                        + "|inside is array ? :" + (at.getComponentType() instanceof ArrayType)
                        + " | inside is declared? :" + (at.getComponentType() instanceof DeclaredType)
                        + " | deepType : " + getDeepType(tm) + " | r :" + r);
                System.out.println("deepType is array ?: " + (getDeepType(tm) instanceof ArrayType));
            } else if (at.getComponentType() instanceof DeclaredType) {

                r = isMultipleArray(at.getComponentType());
            }
        } else if (tm instanceof DeclaredType) {
            DeclaredType declaredType = (DeclaredType) tm;
            List<? extends TypeMirror> typeArgumentsList = declaredType.getTypeArguments();
            if (!typeArgumentsList.isEmpty()) {
                for (TypeMirror typeArg : typeArgumentsList) {

                    if (typeArg instanceof ArrayType) {
                        r = true;
                    } else if (typeArg instanceof DeclaredType) {
                        r = true;
                    }
                }
            }

        }

        return r;
    }

    protected String removeBracket(String type) {

        return type.replaceAll("\\[\\]", "");

    }

    protected String rework_type(String type) {
        String r = type;

        r = rework_Real(r);
        r = rework_String(r);
        r = rework_Integer(r);
        return r;
    }

    protected String rework_Integer(String type) {
        String r = type;

        ArrayList<String> needRework = new ArrayList<String>(
                Arrays.asList("int", "Short", "short", "Long", "long", "Byte", "byte"));
        for (String rework : needRework) {

            r = r.replaceAll(rework, "Integer");

        }
        return r;
    }

    protected String rework_Real(String type) {
        String r = type;

        ArrayList<String> needRework = new ArrayList<String>(Arrays.asList("Float", "float", "Double", "double"));
        for (String rework : needRework) {
            if (type.equals(rework)) {

                r = r.replaceAll(rework, "Real");
            }
        }
        return r;
    }

    protected String rework_String(String type) {
        String r = type;
        ArrayList<String> needRework = new ArrayList<String>(Arrays.asList("char", "Char", "Character", "character"));

        for (String rework : needRework) {
            if (type.equals(rework)) {

                r = r.replaceAll(rework, "String");
            }
        }
        return r;
    }

}
