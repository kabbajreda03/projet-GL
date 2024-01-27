package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SLT;

import java.util.HashSet;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public UnaryInstructionToReg getOperator(GPRegister op) {
        return new SLT(op);
    }

    @Override
    protected String getOperatorName() {
        return "<";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for the less-than comparison operation.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        UnaryInstructionToReg branchInstruction = new SLT(
                Register.getR(compiler.getStack().getCurrentRegister())
        );
        codeGenInstGeneral(compiler,branchInstruction);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationOpIn(compiler,false);
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }

    @Override
    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {

    }
}
