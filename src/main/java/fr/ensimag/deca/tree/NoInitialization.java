package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

import java.io.PrintStream;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x = 42;").
 *
 * @author gl22
 * @date 01/01/2024
 */
public class NoInitialization extends AbstractInitialization {

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass) {
        // nothing to verify
    }



    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // nothing
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
     * Overrides the code generation initialization method for a specific variable.
     * This implementation does nothing and serves as a placeholder.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     * @param varName  The AbstractIdentifier representing the variable for which
     *                 code generation initialization is to be performed.
     */
    @Override
    public void codeGenInitialization(DecacCompiler compiler, AbstractIdentifier varName) {
        // This method does nothing.
    }

    @Override
    public AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return new IntLiteral(42);
    }


}
