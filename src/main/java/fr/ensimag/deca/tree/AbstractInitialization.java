package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractInitialization extends Tree {
    
    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param t corresponds to the "type" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;



    /**
     * Perform code generation initialization for a specific variable.
     * This method is meant to be implemented by subclasses to handle
     * the initialization of code generation for a particular variable.
     *
     * @param compiler    The DecacCompiler instance managing the compilation process.
     * @param varName     The AbstractIdentifier representing the variable for which
     *                    code generation initialization is to be performed.
     */
    public abstract void codeGenInitialization(DecacCompiler compiler, AbstractIdentifier varName);

    public abstract AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler);

}
