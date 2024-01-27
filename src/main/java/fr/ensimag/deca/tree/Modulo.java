package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        AbstractExpr left = this.getLeftOperand();
        AbstractExpr right = this.getRightOperand();
        Type type1 = left.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = right.verifyExpr(compiler, localEnv, currentClass);
        if ((type1.isInt()) && (type2.isInt())) {
            this.setType(compiler.environmentType.INT);
            return compiler.environmentType.INT;
        }
        else {
            throw new ContextualError( "Modulo operation cannot occur between " + type1 + " and " + type2 + " !", this.getLocation());
        }
    }

    @Override
    public BinaryInstructionDValToReg getOperator(DVal op1, GPRegister op2) {
        return new REM(op1, op2);
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }


    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions to perform remainder (modulus) operation based on the types of operands.
     * Generates instructions to perform the remainder operation and updates the
     * compiler's stack accordingly.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int registerDec = compiler.getStack().getCurrentRegister() + 1 < compiler.getStack().getNumberOfRegisters() ?
                1 : 0;
        BinaryInstructionDValToReg binaryInstructionDValToReg = new REM(Register.getR(
                compiler.getStack().getCurrentRegister() + registerDec -1),
                Register.getR(compiler.getStack().getCurrentRegister() + registerDec)
        );

        codeGenInstOpArith(compiler,binaryInstructionDValToReg, true, true);
        if (!compiler.getCompilerOptions().getNoCheck())
            compiler.addInstruction(new BOV(compiler.getErrorHandler().addModuloByZero()));
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
            if (rightValue instanceof IntLiteral) {
                if (leftValue instanceof IntLiteral) {
                    return new IntLiteral(((IntLiteral) leftValue).getValue() % ((IntLiteral) rightValue).getValue());
                } else if (leftValue instanceof FloatLiteral) {
                    return new FloatLiteral(((FloatLiteral) leftValue).getValue() % ((IntLiteral) rightValue).getValue());
                }
            } else if (rightValue instanceof FloatLiteral) {
                if (leftValue instanceof IntLiteral) {
                    return new FloatLiteral(((IntLiteral) leftValue).getValue() % ((FloatLiteral) rightValue).getValue());
                } else if (leftValue instanceof FloatLiteral) {
                    return new FloatLiteral(((FloatLiteral) leftValue).getValue() % ((FloatLiteral) rightValue).getValue());
                }
            }
        } catch (ArithmeticException e) {
            System.out.println("Modulo by zero is not allowed. " + getRightOperand().getLocation());
            System.exit(1);
        }
        return null;
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }

    @Override
    protected boolean isMod() {
        return true;
    }
}
