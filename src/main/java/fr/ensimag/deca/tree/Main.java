package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class Main extends AbstractMain {
    
    private final ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insets) {
        Validate.notNull(declVariables);
        Validate.notNull(insets);
        this.declVariables = declVariables;
        this.insts = insets;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        //LOG.debug("verify Main: start");
        // A FAIRE: Appeler méthodes "verify*" de ListDeclVarSet et ListInst.
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).
        EnvironmentExp envExp = new EnvironmentExp(null);
        ClassDefinition classDef = new ClassDefinition(null, Location.BUILTIN, null);
        declVariables.verifyListDeclVariable(compiler, envExp, classDef);
        Type voidT = compiler.environmentType.VOID;
        insts.verifyListInst(compiler, envExp, classDef, voidT);
    }

    @Override
    protected void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        declVariables.ConstantFoldingAndPropagation(compiler);
        insts.ConstantFoldingAndPropagation(compiler);
    }

    @Override
    protected void DeadCodeElimination() {
        insts = insts.DeadCodeElimination();
    }

    /**
     * Overrides the main code generation method for a specific component.
     * Generates instructions for declaring variables and executing main instructions.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        declVariables.codeGenListDeclVar(compiler);
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
