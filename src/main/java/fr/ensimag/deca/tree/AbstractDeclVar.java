package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Variable declaration
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractDeclVar extends Tree {
    
    /**
     * Implements non-terminal "decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the synthetized attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */    
    protected abstract void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;



    /**
     * Perform code generation for the declaration of a variable.
     * This method is meant to be implemented by subclasses to handle
     * the code generation logic for declaring a variable.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    public abstract void codeGenDeclVar(DecacCompiler compiler);

    public abstract void ConstantFoldingAndPropagation(DecacCompiler compiler);

    public abstract void codeGenMethods(DecacCompiler compiler);

}
