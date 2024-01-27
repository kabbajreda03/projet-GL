package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    protected AbstractExpr ConstantFoldingAndPropagationOpIn(DecacCompiler compiler, boolean isGreater) {
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        AbstractExpr leftValue = getLeftOperand().ConstantFoldingAndPropagation(compiler);
        if (rightValue instanceof IntLiteral) {
            if (leftValue instanceof IntLiteral) {
                if (isGreater)
                    return new BooleanLiteral(((IntLiteral) rightValue).getValue() < ((IntLiteral) leftValue).getValue());
                else
                    return new BooleanLiteral(((IntLiteral) rightValue).getValue() > ((IntLiteral) leftValue).getValue());
            }
            if (leftValue instanceof FloatLiteral) {
                if (isGreater)
                    return new BooleanLiteral(((IntLiteral) rightValue).getValue() < ((FloatLiteral) leftValue).getValue());
                else
                    return new BooleanLiteral(((IntLiteral) rightValue).getValue() > ((FloatLiteral) leftValue).getValue());
            }
        }
        else if (rightValue instanceof FloatLiteral) {
            if (leftValue instanceof IntLiteral) {
                if (isGreater)
                    return new BooleanLiteral(((FloatLiteral) rightValue).getValue() < ((IntLiteral) leftValue).getValue());
                else
                    return new BooleanLiteral(((FloatLiteral) rightValue).getValue() > ((IntLiteral) leftValue).getValue());
            }
            else if (leftValue instanceof FloatLiteral) {
                if (isGreater)
                    return new BooleanLiteral(((FloatLiteral) rightValue).getValue() < ((FloatLiteral) leftValue).getValue());
                else
                    return new BooleanLiteral(((FloatLiteral) rightValue).getValue() > ((FloatLiteral) leftValue).getValue());
            }
        }
        return null;
    }


    protected AbstractExpr ConstantFoldingAndPropagationOpEq(DecacCompiler compiler, boolean isGreater) {
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        AbstractExpr leftValue = getLeftOperand().ConstantFoldingAndPropagation(compiler);
        if (rightValue instanceof IntLiteral) {
            if (leftValue instanceof IntLiteral) {
                if (isGreater)
                    return new BooleanLiteral(((IntLiteral) rightValue).getValue() <= ((IntLiteral) leftValue).getValue());
                else
                    return new BooleanLiteral(((IntLiteral) rightValue).getValue() >= ((IntLiteral) leftValue).getValue());
            }
            if (leftValue instanceof FloatLiteral) {
                if (isGreater)
                    return new BooleanLiteral(((IntLiteral) rightValue).getValue() <= ((FloatLiteral) leftValue).getValue());
                else
                    return new BooleanLiteral(((IntLiteral) rightValue).getValue() >= ((FloatLiteral) leftValue).getValue());
            }
        }
        else if (rightValue instanceof FloatLiteral) {
            if (leftValue instanceof IntLiteral) {
                if (isGreater)
                    return new BooleanLiteral(((FloatLiteral) rightValue).getValue() <= ((IntLiteral) leftValue).getValue());
                else
                    return new BooleanLiteral(((FloatLiteral) rightValue).getValue() >= ((IntLiteral) leftValue).getValue());
            }
            else if (leftValue instanceof FloatLiteral) {
                if (isGreater)
                    return new BooleanLiteral(((FloatLiteral) rightValue).getValue() <= ((FloatLiteral) leftValue).getValue());
                else
                    return new BooleanLiteral(((FloatLiteral) rightValue).getValue() >= ((FloatLiteral) leftValue).getValue());
            }
        }
        return null;
    }





}
