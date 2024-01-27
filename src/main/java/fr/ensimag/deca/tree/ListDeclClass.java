package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
//        throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclClass declClass : this.getList()) {
            declClass.verifyClass(compiler);
        }
         LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassMembers: start");
        for(AbstractDeclClass absClass: this.getList()){
            absClass.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers: end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclClass declClass : this.getList()) {
            declClass.verifyClassBody(compiler);
        }
    }

    public void codeGenListDeclClass(DecacCompiler compiler) {
        if (getList().isEmpty()) return;
        compiler.addComment("construction of object method");
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getStack().getAddrCounter(),Register.GB)));
        compiler.getStack().increaseAddrCounter();
        compiler.getStack().increaseCounterTSTO();

        Program.setOperandEquals(compiler);

        for (AbstractDeclClass abstractDeclClass : getList()) {
            DeclClass declClass = (DeclClass) abstractDeclClass;
            compiler.addComment("construction of " + declClass.getClassName().getName() + " method");
            abstractDeclClass.codeGenDeclClass(compiler);
        }
    }

    public void codeGenInitListDeclClass(DecacCompiler compiler){
        if(getList().isEmpty()) return;

        for (AbstractDeclClass abstractDeclClass : getList()){
            abstractDeclClass.codeGenInitListDeclClass(compiler);
        }
    }

    public void codeGenMethods(DecacCompiler compiler) {
        for (AbstractDeclClass declClass : getList()) {
            declClass.codeGenMethods(compiler);
        }
    }

    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        for (AbstractDeclClass abstractDeclClass : getList()) {
            abstractDeclClass.ConstantFoldingAndPropagation(compiler);
        }
    }


    public void DeadCodeElimination() {
        for (AbstractDeclClass abstractDeclClass : getList()) {
            abstractDeclClass.DeadCodeElimination();
        }
    }


}
