package pumlFromJava;
 
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.lang.model.SourceVersion;
 
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;
 
/**
 * A doclet to illustrate the use of doclet-specific options.
 *
 * The doclet simply prints of the values of the declared options,
 * whether or not given explicitly on the command line.
 */
public class Opt implements Doclet {
    private static final boolean OK = true;
 
    private boolean alpha;
    private String beta;
    private int gamma;
 
    /**
     * A base class for declaring options.
     * Subtypes for specific options should implement
     * the {@link #process(String,List) process} method
     * to handle instances of the option found on the
     * command line.
     */
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
            // An option that takes no arguments.
            new Option("--alpha", false, "a flag", null) {
                @Override
                public boolean process(String option,
                                       List<String> arguments) {
                    alpha = true;
                    return OK;
                }
            },
 
            // An option that takes a single string-valued argument.
            new Option("--beta", true, "an option", "<string>") {
                @Override
                public boolean process(String option,
                                       List<String> arguments) {
                    beta = arguments.get(0);
                    return OK;
                }
            },
 
            // An option that takes a single integer-valued srgument.
            new Option("--gamma", true, "another option", "<int>") {
                @Override
                public boolean process(String option,
                                       List<String> arguments) {
                    String arg = arguments.get(0);
                    try {
                        gamma = Integer.parseInt(arg);
                        return OK;
                    } catch (NumberFormatException e) {
                        // Note: it would be better to use
                        // {@link Reporter} to print an error message,
                        // so that the javadoc tool "knows" that an
                        // error was reported in conjunction\ with
                        // the "return false;" that follows.
                        System.err.println("not an int: " + arg);
                        return false;
                    }
                }
            }
    );
 
 
    @Override
    public void init(Locale locale, Reporter reporter) {  }
 
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
 
    @Override
    public Set<? extends Option> getSupportedOptions() {
        return options;
    }
 
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
 
 
    @Override
    public boolean run(DocletEnvironment environment) {
        System.out.println("alpha: " + alpha);
        System.out.println("beta: " + beta);
        System.out.println("gamma: " + gamma);
        return OK;
    }
}