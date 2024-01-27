package fr.ensimag.deca.tree;


import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.HashSet;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        this.setType(compiler.environmentType.INT);
        return compiler.environmentType.INT;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates an instruction to read an integer from the input and then
     * calls the superclass method to move the result to an available register.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new BOV(compiler.getErrorHandler().addInvalidIntegerInput()));
        super.moveToRegister(compiler);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return null;
    }

    @Override
    public void checkAliveVariables() {
        //nothing to do
    }

    @Override
    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {
        // nothing to do
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean ex) {
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new WINT());

    }
}
