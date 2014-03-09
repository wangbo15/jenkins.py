package jenkins.python.pwm;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.QualifiedType;

public abstract class AbstractTypeDeclFinder {
    
    private final String CHARSET = "UTF-8";
    private final File sourceCodeDir;
    private List<List<TypeDeclaration>> wantedTypes;
    
    public AbstractTypeDeclFinder(File sourceCodeDir_) {
        sourceCodeDir = sourceCodeDir_;
    }
    
    /**
     * Returns a list of lists of wanted type declarations and all parent type declarations.
     */
    public List<List<TypeDeclaration>> getAllDeclarations() throws JavaParserException {
        wantedTypes = new LinkedList<List<TypeDeclaration>>();
        searchDir(sourceCodeDir);
        return wantedTypes;
    }
    
    /**
     * Recursively search in the given directory for wanted type declarations.
     */
    private void searchDir(File dir) throws JavaParserException {
        File[] files = dir.listFiles();
        if (files == null) {
            String errStr = "error while scanning directory " + dir.getPath() + " occured";
            throw new JavaParserException(errStr);
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                searchDir(f);
            }
            else if (f.getName().endsWith(".java")) {
                Logger.error("parsing file " + f.getPath());/// TODO verbose
                searchFile(f);
            }
            else {
                Logger.verbose("file " + f.getName() + " ignored");
            }
        }
    }
    
    /**
     * Returns the file as a char array.
     */
    private char[] readFile(File f) throws IOException
    {
        Charset charset = Charset.forName(CHARSET);
        byte[] encoded = Files.readAllBytes(Paths.get(f.getPath()));
        return charset.decode(ByteBuffer.wrap(encoded)).toString().toCharArray();
    }
    
    /**
     * Search in the given file for the wanted type declaration.
     */
    private void searchFile(File f) throws JavaParserException {
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        try {
            parser.setSource(readFile(f));
        }
        catch (IOException e) {
            String errStr = "cannot parse file " + f.getPath() + " caused by " + e.getMessage();
            throw new JavaParserException(errStr);
        }
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        WantedTypeVisitor visitor = new WantedTypeVisitor();
		cu.accept(visitor);
        if (visitor.getFound()) {
            String typeName = NameResolver.resolveName(visitor.getTypeDecl());
            /// TODO get file path for some type or type decl (also inner classes)
            Logger.verbose("wanted type declaration " + typeName + " found");
            List<TypeDeclaration> list = new LinkedList<TypeDeclaration>();
            // add the found type declaration to the list
            list.add(visitor.getTypeDecl());
            /// TODO search for parent classes
            /// TODO save temp parent classes as map fullName: TypeDeclaration
            // add the list (of the type declaration and its parents) to the wanted types list
            wantedTypes.add(list);
        }
    }
    
    /**
     * Determines if a type declaration is wanted by a concrete finder.
     */
    protected abstract boolean isWanted(TypeDeclaration typeDecl);
    
    
    /**
     * Visits all TypeDeclaration nodes and checks if they are wanted.
     * Only the last wanted node in the tree is saved!!
     */
    private class WantedTypeVisitor extends ASTVisitor {
        private boolean found = false;
        private TypeDeclaration typeDecl;
        
        public boolean visit(TypeDeclaration node) {
            if (isWanted(node)) {
                found = true;
                typeDecl = node;
            }
            return true;
        }
        
        public boolean visit(QualifiedType node) {/// TODO remove
            Logger.error("TYPE: " + NameResolver.resolveName(node));///
            return true;///
        }///
        
        public boolean getFound() {
            return found;
        }
        
        public TypeDeclaration getTypeDecl() {
            return typeDecl;
        }
    }
}
