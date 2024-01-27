package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractOpBool.class);


    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        List<String> opList = new ArrayList<String>();
        opList.add("&&");
        opList.add("||");

        AbstractExpr left = this.getLeftOperand();
        AbstractExpr right = this.getRightOperand();
        Type type1 = left.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = right.verifyExpr(compiler, localEnv, currentClass);

        if( (type1.isBoolean() && type2.isBoolean()) && opList.contains(this.getOperatorName()) ){
            this.setType(compiler.environmentType.BOOLEAN);
            return compiler.environmentType.BOOLEAN;
        }
        throw new ContextualError(this.getOperatorName() + " operation cannot occur between " + type1 + " and " + type2 + " !", this.getLocation());
    }



    /**
     * Generated the assembly code that compares the given integer value with the content of the specified register.
     * If they are equal {@code BEQ}, jumps to the provided label.
     *
     * @param val The integer value to compare.
     * @param registerNumber The register number whose content is compared.
     * @param label The label to jump to if the comparison is true.
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */
    protected void compareAndJump(int val, int registerNumber, Label label, DecacCompiler compiler) {
        // Compare the specified value with the content of the specified register
        compiler.addInstruction(new CMP(val, Register.getR(registerNumber)));
        // Branch to the specified label if the comparison is true (BEQ)
        compiler.addInstruction(new BEQ(label));
    }

    protected void compareAndJump(int val, GPRegister register, Label label, DecacCompiler compiler) {
        // Compare the specified value with the content of the specified register
        compiler.addInstruction(new CMP(val, register));
        // Branch to the specified label if the comparison is true (BEQ)
        compiler.addInstruction(new BEQ(label));
    }

    /**
     * Generated the assembly code that adds a label and instructions for branching based on a boolean value.
     *
     * @param val The boolean value to load into a register.
     * @param label The label to add before the branch instruction.
     * @param endlabel The label to jump to if the boolean value is true.
     * @param compiler The {@link DecacCompiler} instance managing the compilation process.
     */
    protected void boolLabel(int val, Label label, Label endlabel, DecacCompiler compiler) {
        // Add the specified label
        compiler.addLabel(label);
        // Load the boolean value into the current register
        compiler.addInstruction(new LOAD(val,Register.getR(compiler.getStack().getCurrentRegister()-1)));
        // Unconditionally branch to the endlabel
        compiler.addInstruction(new BRA(endlabel));
    }


    protected AbstractExpr ConstantFoldingAndPropagationBool(DecacCompiler compiler, boolean isAnd) {
        AbstractExpr leftValue = getLeftOperand().ConstantFoldingAndPropagation(compiler);
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        if (leftValue instanceof BooleanLiteral && rightValue instanceof BooleanLiteral) {
            if (isAnd)
            return new BooleanLiteral(((BooleanLiteral) leftValue).getValue() && ((BooleanLiteral) rightValue).getValue());
            else {
                return new BooleanLiteral(((BooleanLiteral) leftValue).getValue() || ((BooleanLiteral) rightValue).getValue());
            }
        }
        else {
            return null;
        }
    }

    public abstract int isOr();

    private static int counter = 0;

    public void increaseCounter() {
        counter++;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i = counter;
        increaseCounter();
        // Create labels for the end of the AND operation, true condition, and false condition
        Label isFalse = new Label("is_false_"+i);
        Label isTrue = new Label("is_true_"+i);
        Label endAnd = new Label("end_and_"+i);

        // Generate code for the left operand
        if (compiler.getCompilerOptions().getOPTIM())
            getLeftOperand().codeGenInstOP(compiler);
        else
            getLeftOperand().codeGenInst(compiler);

        // Compare the result of the left operand with 0 and jump to endAnd if equal (false)
        compareAndJump(isOr(), compiler.getStack().getCurrentRegister() - 1, endAnd, compiler);

        // Decrease the register count for the left operand
        compiler.getStack().decreaseRegister();

        // Generate code for the right operand
        if (compiler.getCompilerOptions().getOPTIM())
            getRightOperand().codeGenInstOP(compiler);
        else
            getRightOperand().codeGenInst(compiler);

        // Compare the result of the right operand with 0 and jump to isFalse if equal (false)
        compareAndJump(isOr(), compiler.getStack().getCurrentRegister() - 1, isFalse, compiler);

        // If both operands are true, jump to isTrue
        boolLabel(1 - isOr(), isTrue, endAnd, compiler);

        // If either operand is false, jump to isFalse
        boolLabel(isOr(), isFalse, endAnd, compiler);

        // Add the label for the end of the AND operation
        compiler.addLabel(endAnd);
    }

    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        String vars = extractVariable(compiler);
        int i = counter;
        increaseCounter();
        // Create labels for the end of the AND operation, true condition, and false condition
        Label isFalse = new Label("is_false_"+i);
        Label isTrue = new Label("is_true_"+i);
        Label endAnd = new Label("end_and_"+i);


        switch (vars){
            case "both":
                compareAndJump(isOr(), compiler.getRegister((AbstractIdentifier) getLeftOperand()), endAnd, compiler);
                compareAndJump(isOr(), compiler.getRegister((AbstractIdentifier) getRightOperand()), endAnd, compiler);
                compiler.getStack().increaseRegister();
                break;

            case "left":
                compareAndJump(isOr(), compiler.getRegister((AbstractIdentifier) getLeftOperand()), endAnd, compiler);
                getRightOperand().codeGenInstOP(compiler);
                compareAndJump(isOr(), compiler.getStack().getCurrentRegister() - 1, endAnd, compiler);
                break;

            case "right":
                compareAndJump(isOr(), compiler.getRegister((AbstractIdentifier) getRightOperand()), endAnd, compiler);
                getLeftOperand().codeGenInstOP(compiler);
                compareAndJump(isOr(), compiler.getStack().getCurrentRegister() - 1, endAnd, compiler);
                break;

            default:
                codeGenInst(compiler);
                return;
        }

        boolLabel(1 - isOr(), isTrue, endAnd, compiler);

        // If either operand is false, jump to isFalse
        boolLabel(isOr(), isFalse, endAnd, compiler);

        // Add the label for the end of the AND operation
        compiler.addLabel(endAnd);
    }


}
