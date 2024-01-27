package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl22
 * @date 01/01/2024
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {


    /**
     * Generate code for a list of variable declarations.
     * Iterates through the list of variable declarations, calling the
     * code generation method for each declaration and updating the
     * compiler's stack counter accordingly.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */

    public void codeGenListDeclVar(DecacCompiler compiler) {
        for (AbstractDeclVar abstractDeclVar : getList()) {
            abstractDeclVar.codeGenDeclVar(compiler);
            compiler.getStack().increaseCounterTSTO();
        }
    }

    public void codeGenMethods(DecacCompiler compiler) {
        for (AbstractDeclVar abstractDeclVar : getList()) {
            abstractDeclVar.codeGenMethods(compiler);
            compiler.getStack().increaseCounterTSTO();
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclVar exp : this.getList()){
            exp.decompile(s);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclVar exp : this.getList()) {
            exp.verifyDeclVar(compiler, localEnv, currentClass);
        }
    }

    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        for (AbstractDeclVar abstractDeclVar : getList()) {
            abstractDeclVar.ConstantFoldingAndPropagation(compiler);
        }
    }


}
