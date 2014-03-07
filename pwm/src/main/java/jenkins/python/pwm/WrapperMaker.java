package jenkins.python.pwm;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Main class of the pwm tool.
 */
public class WrapperMaker
{
    public static void main(String args[]){
        try {
            File inputDir = getInputDir(args);
            File outputDir = getOutputDir(args);
            List<List<TypeDeclaration>> expoints = findAllExpoints(inputDir);
            Logger.info(new Integer(expoints.size()) + " extension points found");
            List<List<TypeDeclaration>> descriptors = findAllDescriptors(inputDir);
            makeExpointWrappers(expoints, outputDir);
            makeDescrWrappers(descriptors, outputDir);
            Logger.info("WRAPPERS HAVE BEEN SUCCESSFULLY CREATED");
        }
        catch (WrapperMakerException e) {
            Logger.error(e.getMessage());
        }
	}
    
    /**
     * Checks and returns a path to the Jenkins source code directory.
     */
    private static File getInputDir(String args[]) {
        return new File("/home/tomas/repos/jenkins");
    }
    
    /**
     * Checks and returns a path to the python-wrapper plugin source code directory.
     */
    private static File getOutputDir(String args[]) {
        return new File("/");
    }
    
    private static List<List<TypeDeclaration>> findAllExpoints(File srcDir) throws JavaParserException  {
        ExtensionPointFinder expointFinder = new ExtensionPointFinder(srcDir);
        List<List<TypeDeclaration>> expoints = expointFinder.getAllDeclarations();
        return expoints;
    }
    
    private static List<List<TypeDeclaration>> findAllDescriptors(File srcDir) {
        return new LinkedList<List<TypeDeclaration>>();
    }
    
    private static void makeExpointWrappers(List<List<TypeDeclaration>> expoints, File outputDir) {
        
    }
    
    private static void makeDescrWrappers(List<List<TypeDeclaration>> descriptors, File outputDir) {
        
    }
}
