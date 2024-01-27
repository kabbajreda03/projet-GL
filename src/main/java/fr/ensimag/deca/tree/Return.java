package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Return extends AbstractInst {
    private AbstractExpr returnedExpr;

    public Return(AbstractExpr exp){
        this.returnedExpr=exp;
    }
    public void setExpression(AbstractExpr returnedExpr) {
        this.returnedExpr = returnedExpr;
    }
    
       protected  void verifyInst(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError{
            if(returnType.isVoid()){
                throw new ContextualError("Return Type should not be void !", this.getLocation());
            }
            AbstractExpr expression = returnedExpr.verifyRValue(compiler, localEnv, currentClass, returnType);
            this.setExpression(expression);
        }

    /**
     * Generates assembly code for the current instruction.
     * This method is called during the code generation phase.
     *
     * @param compiler The DecacCompiler instance for compilation.
     */
    @Override
    protected  void codeGenInst(DecacCompiler compiler){
        returnedExpr.codeGenInst(compiler);
        compiler.addInstruction(new LOAD(
                Register.getR(compiler.getStack().getCurrentRegister()-1),
                Register.R0
                ));
        compiler.addInstruction(new BRA(new Label(
                "end." + compiler.getMethod()
        )));
    }


    /**
     * Decompile the tree, considering it as an instruction.
     * In most case, this simply calls decompile(), but it may add a semicolon if needed
     */
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        AbstractExpr expr = returnedExpr.ConstantFoldingAndPropagation(compiler);
        if (expr != null)
            setReturnedExpr(expr);
        return null;
    }

    @Override
    public void checkAliveVariables() {

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        returnedExpr.decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnedExpr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnedExpr.prettyPrint(s, prefix,true);
    }

    public void setReturnedExpr(AbstractExpr returnedExpr) {
        this.returnedExpr = returnedExpr;
    }
}
