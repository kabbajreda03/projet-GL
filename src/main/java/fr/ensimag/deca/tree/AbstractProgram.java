package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author gl22
 * @date 01/01/2024
 *
 */
public abstract class AbstractProgram extends Tree {
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError;

    public abstract void ConstantFoldingAndPropagation(DecacCompiler compiler);

    public abstract void DeadCodeElimination();

    public abstract void codeGenProgram(DecacCompiler compiler) ;

}
