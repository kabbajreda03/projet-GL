package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    private static final Logger LOG = Logger.getLogger(AbstractOpCmp.class);




    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
      //  throw new UnsupportedOperationException("not yet implemented");
        String opName = this.getOperatorName();

        AbstractExpr left = this.getLeftOperand();
        AbstractExpr right = this.getRightOperand();
        Type type1 = left.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = right.verifyExpr(compiler, localEnv, currentClass);
        Type syntType = verifyCmpOp(compiler, opName, type1, type2, localEnv, currentClass);
        this.setType(syntType);
        return syntType;
    }

    public Type verifyCmpOp(DecacCompiler compiler, String op, Type type1, Type type2, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        List<String> opList = new ArrayList<String>();
        opList.add("==");
        opList.add("!=");
        opList.add("<");
        opList.add("<=");
        opList.add(">");
        opList.add(">=");


        //here we verify if type1 and type2 are in dom(type_arith_op) and if op is in the opList
        if( (opList.contains(op) && verifyDomTypeArithOp(type1) && verifyDomTypeArithOp(type2))
        //here we verify if type1 and type2 are both boolean and if the op is eq or neq
        || (( (type1.isBoolean() && type2.isBoolean()) || (type1.isClassOrNull() && type2.isClassOrNull()) ) && (op.equals("==") || op.equals("!=")) ) ) {
            if ((type1.isInt() && type2.isFloat())) {
                AbstractExpr leftOp = new ConvFloat(this.getLeftOperand());
                this.setLeftOperand(leftOp);
                leftOp.verifyExpr(compiler, localEnv, currentClass);
            }
            else if ((type1.isFloat() && type2.isInt())) {
                AbstractExpr rightOp = new ConvFloat(this.getRightOperand());
                this.setRightOperand(rightOp);
                rightOp.verifyExpr(compiler, localEnv, currentClass);
            }
            return compiler.environmentType.BOOLEAN;
        }
        //TO DO Add the case where op ∈ {eq, neq} and T1 = type_class(A)
        throw new ContextualError(op + " operation cannot occur between " + type1 + " and " + type2 + " !", this.getLocation());



    }

    public boolean verifyDomTypeArithOp(Type type){
        return (type.isInt() || type.isFloat());
    }


    /**
     * Generate general code for comparison operations with a branch instruction.
     * Generates instructions for a comparison operation with a specified branch instruction,
     * handling cases where additional stack manipulation is required due to the limited number of registers.
     *
     * @param compiler           The DecacCompiler instance managing the compilation process.
     * @param branchInstruction  The branch instruction to be used for the comparison.
     */
    protected void codeGenInstGeneral(DecacCompiler compiler,
                                      UnaryInstructionToReg branchInstruction) {
        if(compiler.getStack().getCurrentRegister()+1 < compiler.getStack().getNumberOfRegisters()) {
            codeGenInstOpCmp(compiler,branchInstruction);
        }
        else {
            compiler.getStack().pushRegister(compiler);
            codeGenInstOpCmp(compiler,branchInstruction);
            compiler.getStack().decreaseRegister();
            compiler.getStack().popRegister(compiler);
            compiler.getStack().increaseRegister();
        }
    }

    protected void codeGenInstGeneralOP(DecacCompiler compiler, UnaryInstructionToReg branchInstruction){
        LOG.debug("Im OpCMP and ill call extract variables");
        String vars = extractVariable(compiler);

        GPRegister rightRegister = null;
        GPRegister leftRegister = null;

        LOG.debug(vars);

        switch (vars){
            case "both":
                rightRegister = compiler.getRegister((AbstractIdentifier) getRightOperand());
                leftRegister = compiler.getRegister((AbstractIdentifier) getLeftOperand());
                compiler.getStack().increaseRegister();
                break;

            case "left":
                leftRegister = compiler.getRegister((AbstractIdentifier) getLeftOperand());
                getRightOperand().codeGenInstOP(compiler);
                rightRegister = Register.getR(compiler.getStack().getCurrentRegister() - 1);
                break;

            case "right":
                rightRegister = compiler.getRegister((AbstractIdentifier) getRightOperand());
                getLeftOperand().codeGenInstOP(compiler);
                leftRegister = Register.getR(compiler.getStack().getCurrentRegister() - 1);
                break;

            default:
                codeGenInstGeneral(compiler, branchInstruction);
                return;

        }

        compiler.addInstruction(new CMP(
                rightRegister,
                leftRegister
        ));

        compiler.addInstruction(branchInstruction);





    }

    /**
     * Generate code for a comparison operation with branch instruction and loading true/false values.
     * Generates instructions for a comparison operation with a specified branch instruction,
     * loading true or false values based on the result of the comparison.
     *
     * @param compiler           The DecacCompiler instance managing the compilation process.
     * @param branchInstruction  The branch instruction to be used for the comparison.
     */
    protected void codeGenInstOpCmp(DecacCompiler compiler,
                                    UnaryInstructionToReg branchInstruction) {

        if(compiler.getCompilerOptions().getOPTIM()) {
            getLeftOperand().codeGenInstOP(compiler);
            getRightOperand().codeGenInstOP(compiler);
        }
        else {
            getLeftOperand().codeGenInst(compiler);
            getRightOperand().codeGenInst(compiler);
        }
        compiler.addInstruction(new CMP(
                Register.getR(compiler.getStack().getCurrentRegister()-1),
                Register.getR(compiler.getStack().getCurrentRegister()-2)
        ));



        compiler.addInstruction(branchInstruction);
        compiler.getStack().decreaseRegister();
    }


    public abstract UnaryInstructionToReg getOperator(GPRegister register);


    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        LOG.debug("the current register is " + compiler.getStack().getCurrentRegister() + " AND I WANNA DIE");
        UnaryInstructionToReg branchInstruction = getOperator(Register.getR(compiler.getStack().getCurrentRegister()));
        codeGenInstGeneralOP(compiler,branchInstruction);
    }
}
