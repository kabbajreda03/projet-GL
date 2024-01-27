package fr.ensimag.deca.tree;


import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        AbstractExpr expConv = expression.verifyRValue(compiler, localEnv, currentClass, t);
        this.setExpression(expConv);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        String st=" = ";
        s.print(st);
        this.getExpression().decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }


    /**
     * Overrides the code generation initialization method for a specific variable.
     * Generates instructions to initialize the given variable with the value of the
     * associated expression.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     * @param varName  The AbstractIdentifier representing the variable for which
     *                 code generation initialization is to be performed.
     */
    @Override
    public void codeGenInitialization(DecacCompiler compiler, AbstractIdentifier varName) {
        if(compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {
            expression.codeGenInst(compiler);
            compiler.addInstruction(
                    new STORE(Register.getR(compiler.getStack().getCurrentRegister()-1),
                            varName.getExpDefinition().getOperand()
                    )
            );
            compiler.getStack().decreaseRegister();
        }
        else{
            compiler.getStack().pushRegister(compiler);
            codeGenInitialization(compiler, varName);
            compiler.getStack().popRegister(compiler);
        }
    }

    @Override
    public AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        AbstractExpr value = expression.ConstantFoldingAndPropagation(compiler);
        if (value != null) {
            expression = value;
        }
        return value;
    }

}
