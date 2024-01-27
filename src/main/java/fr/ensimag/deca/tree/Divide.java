package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public BinaryInstructionDValToReg getOperator(DVal op1, GPRegister op2) {
        if (getType().isFloat()){
            return new DIV(op1, op2);
        }
        return new QUO(op1, op2);
    }

    @Override
    protected boolean isDiv() {
        return true;
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions to perform division operation based on the types of operands.
     * Generates instructions to perform the division operation and updates the
     * compiler's stack accordingly.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int registerDec = compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters() ?
                1 : 0;
        BinaryInstructionDValToReg binaryInstructionDValToReg;

        if(this.getType().isFloat()) {
            binaryInstructionDValToReg = new DIV(Register.getR(
                    compiler.getStack().getCurrentRegister() + registerDec - 1),
                    Register.getR(compiler.getStack().getCurrentRegister() + registerDec)
            );
        }
        else {
            binaryInstructionDValToReg = new QUO(Register.getR(
                    compiler.getStack().getCurrentRegister() + registerDec - 1),
                    Register.getR(compiler.getStack().getCurrentRegister() + registerDec)
            );
        }

        codeGenInstOpArith(compiler, binaryInstructionDValToReg, true, true);
        if (!compiler.getCompilerOptions().getNoCheck())
            compiler.addInstruction(new BOV(compiler.getErrorHandler().addDivisionByZero()));


    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        try {
            AbstractExpr leftValue = getLeftOperand().ConstantFoldingAndPropagation(compiler);
            AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
            if (leftValue != null)
                setLeftOperand(leftValue);
            if (rightValue != null)
                setRightOperand(rightValue);
            if (leftValue != null)
                setLeftOperand(leftValue);
            if (rightValue != null)
                setRightOperand(rightValue);
            if (rightValue instanceof IntLiteral) {
                if (leftValue instanceof IntLiteral) {
                    return new IntLiteral(((IntLiteral) leftValue).getValue() / ((IntLiteral) rightValue).getValue());
                } else if (leftValue instanceof FloatLiteral) {
                    return new FloatLiteral(((FloatLiteral) leftValue).getValue() / ((IntLiteral) rightValue).getValue());
                }
            } else if (rightValue instanceof FloatLiteral) {
                if (leftValue instanceof IntLiteral) {
                    return new FloatLiteral(((IntLiteral) leftValue).getValue() / ((FloatLiteral) rightValue).getValue());
                } else if (leftValue instanceof FloatLiteral) {
                    return new FloatLiteral(((FloatLiteral) leftValue).getValue() / ((FloatLiteral) rightValue).getValue());
                }
            }
        } catch (ArithmeticException e) {
            System.out.println("Division by zero is not allowed. " + getRightOperand().getLocation());
            System.exit(1);
        }
        return null;
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }

    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        // trying to catch the exponent of 2
        int rightExponent = getRightExponent();
        if (rightExponent == -1 || rightExponent > 19) {
            codeGenInst(compiler);
            return;
        }
        boolean isNegative = ((IntLiteral) getRightOperand()).getValue() < 0;
        shift(compiler,rightExponent, getLeftOperand(), isNegative, true);

    }

}
