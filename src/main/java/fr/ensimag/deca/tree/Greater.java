package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public UnaryInstructionToReg getOperator(GPRegister op) {
        return new SGT(op);
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the greater-than comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        UnaryInstructionToReg branchInstruction = new SGT(
                Register.getR(compiler.getStack().getCurrentRegister())
        );
        codeGenInstGeneral(compiler,branchInstruction);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationOpIn(compiler,true);
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }
}

