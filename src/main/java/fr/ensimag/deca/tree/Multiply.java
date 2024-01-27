package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public BinaryInstructionDValToReg getOperator(DVal op1, GPRegister op2) {
        return new MUL(op1, op2);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions to perform multiplication operation based on the types of operands.
     * If the right operand is a constant or variable, it directly multiplies it with the left operand.
     * If the right operand is not a constant or variable, it generates instructions to perform
     * the multiplication operation and updates the compiler's stack accordingly.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal dVal = getDval(getRightOperand());
        if (dVal != null) {
            if (compiler.getCompilerOptions().getOPTIM())
                getLeftOperand().codeGenInstOP(compiler);
            else
                getLeftOperand().codeGenInst(compiler);
            compiler.addInstruction(new MUL(dVal,
                    Register.getR(compiler.getStack().getCurrentRegister()-1)));
            if(this.getType().isFloat())
                if (!compiler.getCompilerOptions().getNoCheck())
                    compiler.addInstruction(new BOV(compiler.getErrorHandler().addOverflow()));

        }
        else {
            int registerDec = compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters() ?
                    1 : 0;
            BinaryInstructionDValToReg binaryInstructionDValToReg = new MUL(
                    Register.getR(compiler.getStack().getCurrentRegister() +
                            registerDec -1
                    ),
                    Register.getR(compiler.getStack().getCurrentRegister() + registerDec));
            codeGenInstOpArith(compiler,binaryInstructionDValToReg, false, true);
        }

    }

    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {

        // trying to catch the exponent of 2
        int rightExponent = getRightExponent();
        int leftExponent = getLeftExponent();
        if (rightExponent == -1 && leftExponent == -1
                || (rightExponent > 9 && leftExponent > 9)) {
            codeGenInst(compiler);
            return;
        }

        if (rightExponent != -1 ) {
            if (leftExponent != -1) {
                if (rightExponent > leftExponent) {
                    boolean isNegative = ((IntLiteral) getLeftOperand()).getValue() < 0;
                    shift(compiler, leftExponent,getRightOperand(), isNegative, false);
                }
                else {
                    boolean isNegative = ((IntLiteral) getRightOperand()).getValue() < 0;
                    shift(compiler, rightExponent, getLeftOperand(), isNegative, false);
                }
            } else {
                boolean isNegative = ((IntLiteral) getRightOperand()).getValue() < 0;
                shift(compiler,rightExponent, getLeftOperand(), isNegative, false);
            }
        } else {
            boolean isNegative = ((IntLiteral) getLeftOperand()).getValue() < 0;
            shift(compiler,leftExponent, getRightOperand(), isNegative, false);
        }

    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {

        AbstractExpr leftValue = getLeftOperand().ConstantFoldingAndPropagation(compiler);
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        if (leftValue != null)
            setLeftOperand(leftValue);
        if (rightValue != null)
            setRightOperand(rightValue);
        if (rightValue instanceof IntLiteral) {
            if (leftValue instanceof IntLiteral) {
                return new IntLiteral(((IntLiteral) leftValue).getValue()*((IntLiteral) rightValue).getValue());
            } else if (leftValue instanceof FloatLiteral) {
                return new FloatLiteral(((FloatLiteral) leftValue).getValue() * ((IntLiteral) rightValue).getValue());
            }
        } else if (rightValue instanceof FloatLiteral) {
            if (leftValue instanceof IntLiteral) {
                return new FloatLiteral(((IntLiteral) leftValue).getValue() * ((FloatLiteral) rightValue).getValue());
            }
            else if (leftValue instanceof FloatLiteral) {
                return new FloatLiteral(((FloatLiteral) leftValue).getValue()*((FloatLiteral) rightValue).getValue());
            }
        }
        return null;
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }
}