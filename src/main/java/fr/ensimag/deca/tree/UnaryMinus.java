package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.OPP;

import java.util.HashSet;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        Type type1 = this.getOperand().verifyExpr(compiler, localEnv,currentClass);
        if (type1.isInt()){
            this.setType(compiler.environmentType.INT);
            return compiler.environmentType.INT;
        } else if (type1.isFloat()) {
            this.setType(compiler.environmentType.FLOAT);
            return compiler.environmentType.FLOAT;
        }
        throw new ContextualError(this.getOperatorName() + " unary operation cannot occur with " + type1 + " !", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }


    /**
     * Generates code for the negation (opposite) of an operand.
     * If the operand is a constant, the negation is performed directly.
     * If the operand is not a constant, its value is loaded into a register and then negated.
     *
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal dVal = getDval(getOperand());

        // Check if the operand is a constant (DVal)
        if ( dVal != null) {
            // Perform the negation of the constant directly
            compiler.addInstruction(new OPP(
                    dVal,
                    Register.getR(compiler.getStack().getCurrentRegister())
            ));
            compiler.getStack().increaseRegister();
        }
        else {
            // Operand is not a constant, generate code for the operand
            if (compiler.getCompilerOptions().getOPTIM())
                getOperand().codeGenInstOP(compiler);
            else
                getOperand().codeGenInst(compiler);
            // Load the value from the operand into a register and then perform the negation
            compiler.addInstruction(new OPP(
                    Register.getR(compiler.getStack().getCurrentRegister()-1),
                    Register.getR(compiler.getStack().getCurrentRegister()-1)
            ));
        }
    }


    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        if(!isVariable(compiler)){
            codeGenInst(compiler);
            return;
        }
        compiler.getStack().increaseRegister();
        // Load the value from the operand into a register and then perform the negation
        compiler.addInstruction(new OPP(
                compiler.getRegister((AbstractIdentifier) getOperand()),
                Register.getR(compiler.getStack().getCurrentRegister() - 1)
        ));

    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        AbstractExpr value = getOperand().ConstantFoldingAndPropagation(compiler);
        if (value instanceof IntLiteral) {
            return new IntLiteral(-((IntLiteral) value).getValue());
        }
        else if (value instanceof FloatLiteral) {
            return new FloatLiteral(-((FloatLiteral) value).getValue());
        }
        else {
            return null;
        }
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }

    @Override
    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {
        if (getOperand() instanceof Identifier)
            liveVariable.add((Identifier) getOperand());
    }
}
