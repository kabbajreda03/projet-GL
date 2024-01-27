package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the inequality comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        UnaryInstructionToReg branchInstruction = new SNE(
                Register.getR(compiler.getStack().getCurrentRegister())
        );
        codeGenInstGeneral(compiler,branchInstruction);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        AbstractExpr leftValue = getLeftOperand().ConstantFoldingAndPropagation(compiler);
        if (rightValue instanceof IntLiteral) {
            if (leftValue instanceof IntLiteral) {
                return new BooleanLiteral(((IntLiteral) rightValue).getValue() != ((IntLiteral) leftValue).getValue());
            }
            if (leftValue instanceof FloatLiteral) {
                return new BooleanLiteral(((IntLiteral) rightValue).getValue() != ((FloatLiteral) leftValue).getValue());
            }
        }
        else if (rightValue instanceof FloatLiteral) {
            if (leftValue instanceof IntLiteral) {
                return new BooleanLiteral(((FloatLiteral) rightValue).getValue() != ((IntLiteral) leftValue).getValue());
            }
            else if (leftValue instanceof FloatLiteral) {
                return new BooleanLiteral(((FloatLiteral) rightValue).getValue() != ((FloatLiteral) leftValue).getValue());
            }
        }
        return null;
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }
    @Override
    public UnaryInstructionToReg getOperator(GPRegister op) {
        return new SNE(op);
    }
}
