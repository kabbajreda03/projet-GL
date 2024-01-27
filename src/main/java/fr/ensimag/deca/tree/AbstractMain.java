package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Main block of a Deca program.
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractMain extends Tree {

    /**
     * Perform main code generation for a specific component.
     * This method is meant to be implemented by subclasses to handle
     * the main code generation logic for a particular component.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    protected abstract void codeGenMain(DecacCompiler compiler);


    /**
     * Implements non-terminal "main" of [SyntaxeContextuelle] in pass 3 
     */
    protected abstract void verifyMain(DecacCompiler compiler) throws ContextualError;

    protected abstract void ConstantFoldingAndPropagation(DecacCompiler compiler);

    protected abstract void DeadCodeElimination();
}
