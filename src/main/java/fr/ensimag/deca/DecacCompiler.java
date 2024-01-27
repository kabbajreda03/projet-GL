package fr.ensimag.deca;

import fr.ensimag.deca.codegen.ErrorHandler;
import fr.ensimag.deca.codegen.Stack;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl22
 * @date 01/01/2024
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;

        this.stack = new Stack();
        this.errorHandler = new ErrorHandler();
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private IMAProgram program = new IMAProgram();
 

    /** The global environment for types (and the symbolTable) */
    public final SymbolTable symbolTable = new SymbolTable();
    public final EnvironmentType environmentType = new EnvironmentType(this);

    public Symbol createSymbol(String name) {
//        return null; // A FAIRE: remplacer par la ligne en commentaire ci-dessous
        return symbolTable.create(name);
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String fileName=sourceFile.substring(0, sourceFile.length()-5);
        String destFile = fileName+".ass";

        // A FAIRE: calculer le nom du fichier .ass à partir du nom du
        // A FAIRE: fichier .deca.
        PrintStream err = System.err;
        PrintStream out = System.out;

        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        if(getCompilerOptions().getNoCheck()){}
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }

    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {

        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }

        if(getCompilerOptions().getParse()){
            prog.decompile(out);
            System.exit(0);
        }

        try {
            prog.verifyProgram(this);
        } catch (ContextualError e) {
            e.display(System.err);
            return true;
        }
        if(getCompilerOptions().getVerification()){ System.exit(0);}
        assert(prog.checkAllDecorations());

        if(getCompilerOptions().getParse()){
            prog.decompile(out);
            System.exit(1);
        }
        if (compilerOptions.getOPTIM()) {
            // pass 1 of OPTIM
            prog.ConstantFoldingAndPropagation(this);

            // pass 2 of OPTIM
            prog.DeadCodeElimination();

            // pass 3 of OPTIM
            isPass3 = true;
            prog.ConstantFoldingAndPropagation(this);
        }


        if(getCompilerOptions().doChangeRegisterNumber()){
            stack.setNumberOfRegisters(getCompilerOptions().getReigsterNumberEntered());
        }

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    private final Stack stack;
    /**
     * Gets stack of the compiler
     * @return stack
     */
    public Stack getStack() {
        return stack;
    }

    private final ErrorHandler errorHandler;

    /**
     * Gets errorHandler of the compiler
     * @return errorHandler
     */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    private boolean isCritical;

    public boolean getIsCritical() {
        return isCritical;
    }

    public void setIsCritical(boolean isCritical) {
        this.isCritical = isCritical;
    }

    public HashMap<AbstractIdentifier, AbstractExpr> ifManager = new HashMap<>();

    public HashMap<AbstractIdentifier, AbstractExpr> getIfManager() {
        return ifManager;
    }

    public void setIfManager(HashMap<AbstractIdentifier, AbstractExpr> ifManager) {
        this.ifManager = ifManager;
    }

    private final HashMap<AbstractIdentifier, DeclClass> classManager = new HashMap<>();

    public HashMap<AbstractIdentifier, DeclClass> getClassManager() {
        return classManager;
    }

    String method = null;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public IMAProgram getProgram() {
        return program;
    }

    public void setProgram(IMAProgram program) {
        this.program = program;
    }

    private boolean isPass3 = false;

    public boolean isPass3() {
        return isPass3;
    }


    private Boolean inWhile = false;

    public Boolean isInWhile(){
        return inWhile;
    }

    public void getInWhile(){
        inWhile = true;
    }

    public void getOutWhile(){
        inWhile = false;
    }

    private HashMap<AbstractIdentifier, GPRegister> variablesDict = new HashMap<>();

    public Set<AbstractIdentifier> getVariables() {
        return variablesDict.keySet();
    }

    public void initVariablesDict(){
        variablesDict = new HashMap<>();
    }

    public void addToVariablesDict(AbstractIdentifier variable, GPRegister register){
        variablesDict.putIfAbsent(variable, register);
    }

    public GPRegister getRegister(AbstractIdentifier variable){
        return variablesDict.get(variable);
    }

    public Boolean isVariableInDict(AbstractIdentifier variable){
        return variablesDict.containsKey(variable);
    }

}
