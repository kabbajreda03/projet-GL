package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private final ListDeclClass classes;
    private final AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        //throw new UnsupportedOperationException("not yet implemented");
        /* Passe 1*/
        classes.verifyListClass(compiler);
        /* Passe 2*/
        classes.verifyListClassMembers(compiler);
        /*Passe 3*/
        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    /**
     * Generates code for the entire program.
     * This method is called during the code generation phase for the main program.
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */

    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        main.ConstantFoldingAndPropagation(compiler);
        classes.ConstantFoldingAndPropagation(compiler);
    }

    @Override
    public void DeadCodeElimination() {
        main.DeadCodeElimination();
        classes.DeadCodeElimination();
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {

        // Initialize the stack with a total stack object (TSTO) value of 1
        // The value is going to be modified at the end to match the actual used stack size
        ImmediateInteger TSTOimmediateInteger = new ImmediateInteger(1);
        compiler.addInstruction(new TSTO(TSTOimmediateInteger));


        // Check for stack overflow and branch to the specified label if overflow occurs
        if (!compiler.getCompilerOptions().getNoCheck())
            compiler.addInstruction(new BOV(compiler.getErrorHandler().addStackOverflowError()));

        // Set the stack pointer (SP) to 0
        ImmediateInteger SPimmediateInteger = new ImmediateInteger(0);
        compiler.addInstruction(new ADDSP(SPimmediateInteger));

        compiler.addComment("--------------------------------------------------");
        compiler.addComment("         Construction of Method Tables            ");
        compiler.addComment("--------------------------------------------------");

        classes.codeGenListDeclClass(compiler);

        compiler.addComment("--------------------------------------------------");
        compiler.addComment("                  Main program                    ");
        compiler.addComment("--------------------------------------------------");
        main.codeGenMain(compiler);

        // Halt the program execution
        compiler.addInstruction(new HALT());

        // Update TSTO and SP values based on stack information
        TSTOimmediateInteger.setValue(Math.max(compiler.getStack().getMaxTSTO(), compiler.getStack().getCounterTSTO()));
        SPimmediateInteger.setValue(compiler.getStack().getAddrCounter()-1);


        // Classes constructors

        compiler.addComment("--------------------------------------------------");
        compiler.addComment("                  Constructors                    ");
        compiler.addComment("--------------------------------------------------");
        classes.codeGenInitListDeclClass(compiler);
        classes.codeGenMethods(compiler);


        // Object.equals
        LOG.info("Generate the code for code.Object.equals ...");
        putObjectDotEquals(compiler);

        // Add error labels and associate them with their corresponding error messages
        compiler.getErrorHandler().putErrors(compiler);
    }



    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }

    private static final  Label equalsLabel = new Label("code.Object.equals");

    public static void setOperandEquals(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(equalsLabel, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getStack().getAddrCounter(),Register.GB)));
        compiler.getStack().increaseAddrCounter();
        compiler.getStack().increaseCounterTSTO();
    }

    private void putObjectDotEquals(DecacCompiler compiler){
        compiler.addLabel(equalsLabel);
        compiler.addInstruction(new LOAD(
                new RegisterOffset(-2, Register.LB),
                Register.R0
        ));
        compiler.addInstruction(new CMP(
                new RegisterOffset(-3,Register.LB),
                Register.R0
        ));
        compiler.addInstruction(new SEQ(Register.R0));
        compiler.addInstruction(new RTS());
    }

    /**
     * Sets the operand method by loading the given method label into a register,
     * storing it in the method block on the stack, and updating the stack's address counter.
     *
     * @param compiler     The DecacCompiler instance for compilation.
     * @param methodLabel  The label representing the method to set as the operand.
     */
    public static void setOperandMethod(DecacCompiler compiler, Label methodLabel){
        compiler.addInstruction(new LOAD(methodLabel, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getStack().getAddrCounter(),Register.GB)));
        compiler.getStack().increaseAddrCounter();
        compiler.getStack().increaseCounterTSTO();
    }

}
