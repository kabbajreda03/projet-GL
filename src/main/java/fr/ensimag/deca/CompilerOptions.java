package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.io.IOException;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.syntax.AbstractDecaLexer;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tree.*;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }



    public boolean getParallel() {
        return parallel;
    }

    public boolean getOPTIM() {
        return OPTIM;
    }
    public boolean getOtherThanBOption() {
        return otherThanBOption;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean doChangeRegisterNumber(){
        return changeRegisterNumber;
    }

    public int getReigsterNumberEntered(){
        return numberOfRegistersEntered;
    }

    /**
     * verifie if we should decompile the file or not (option -p)
     * @return
     */
    public boolean getParse() { return parse; }

    /**
     * verifie if we should stop decac after the verification phase or not (option -v)
     * @return
     */
    public boolean getVerification() { return verification; }

    public boolean getNoCheck(){
        return noCheck;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }


    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parse = false;
    private boolean verification = false;
    private boolean noCheck = false;
    private boolean changeRegisterNumber=false;

    private boolean OPTIM = false;
    private boolean otherThanBOption = false;
    private int numberOfRegistersEntered;

    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException, IOException {
        // A FAIRE : parcourir args pour positionner les options correctement.

        // Regular Expression to detect the -X argument with the option -r that represents the Number of Registers
        String numberRegex = "^\\d+$";
        Pattern pattern = Pattern.compile(numberRegex);

        for (String arg : args){
            if (arg.startsWith("-")){
                switch (arg){
                    case "-p":
                        if (this.verification){
                            throw new CLIException("option -v and -p cannot be taken together");
                        }
                        else{
                            this.parse = true;
                            otherThanBOption = true;
                        }
                        break;
                    case "-v":
                        if (this.parse){
                            throw new CLIException("option -p and -v cannot be taken together");
                        }
                        else{
                            this.verification = true;
                            otherThanBOption = true;
                        }
                        break;
                    case "-P":
                        this.parallel = true;
                        otherThanBOption = true;
                        break;
                    case "-b":
                        this.printBanner = true;
                        break;
                    case "-d":
                        this.debug++;
                        otherThanBOption = true;
                        break;
                    case "-n":
                        noCheck=true;
                        otherThanBOption = true;
                        break;
                    case "-r":
                        changeRegisterNumber=true;
                        otherThanBOption = true;
                        break;
                    case "-op":
                        OPTIM = true;
                        break;
                    default:
                        throw new CLIException("the option: " + arg + " does not exist");
                    }
                }
                else{
                        if(pattern.matcher(arg).matches() && doChangeRegisterNumber()){
                            numberOfRegistersEntered=Integer.parseInt(arg);
                        }else{
                            if (arg.endsWith(".deca")) {
                                this.sourceFiles.add(new File(arg));
                                otherThanBOption = true;
                            }
                            else throw new CLIException("the file " + arg + " does not have the extension .deca");
                        }
                }
            }

        if(changeRegisterNumber){
            numberOfRegistersEntered=Integer.parseInt(args[1]);
        }


        Logger logger = Logger.getRootLogger();
        //map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        //throw new UnsupportedOperationException("not yet implemented");

    }

    protected void displayUsage() {
        String optionsDescription = "Usage: decac <options> <source files>\n "+
        "where possible options include:\n"+
        "-b (banner): displays a banner indicating the team name.\n" +
        "-p (parse): stops decac after the tree construction step and displays the decompilation of the tree " +
        "-v (verification): stops decac after the verification step (produces no output in the absence of errors).\n" +
        "-n (no check): removes runtime tests specified in points 11.1 and 11.3 of the Deca semantics.\n" +
        "-r X (registers): limits the available general-purpose registers to R0 ... R{X-1}, with 4 <= X <= 16.\n" +
        "-d (debug): activates debug traces. Repeat the option several times for more traces.\n" +
        "-P (parallel): if there are multiple source files, launches the compilation of files in parallel (to speed up compilation)";

        System.out.println(optionsDescription);
    }
}
