package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Empty main Deca program
 *
 * @author gl22
 * @date 01/01/2024
 */
public class EmptyMain extends AbstractMain {
    @Override
    protected void verifyMain(DecacCompiler compiler) {
        // nothing to verify
    }

    /**
     * Overrides the main code generation method for a specific component.
     * This implementation does nothing and serves as a placeholder.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // This method does nothing.
    }

    /**
     * Contains no real information => nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        // no main program => nothing
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    protected void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        // Nothing to do
    }

    @Override
    protected void DeadCodeElimination() {
        // Nothing to do
    }
}
