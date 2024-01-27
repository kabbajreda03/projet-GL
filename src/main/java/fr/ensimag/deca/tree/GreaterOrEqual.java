package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * Operator "x >= y"
 * 
 * @author gl22
 * @date 01/01/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public UnaryInstructionToReg getOperator(GPRegister op) {
        return new SGE(op);
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the greater-than or equal comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        UnaryInstructionToReg branchInstruction = new SGE(
                Register.getR(compiler.getStack().getCurrentRegister())
        );
        codeGenInstGeneral(compiler,branchInstruction);
    }


    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationOpEq(compiler,true);
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }


}
