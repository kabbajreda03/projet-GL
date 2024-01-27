package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {

    private static final Logger LOG = Logger.getLogger(Assign.class);


    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        AbstractLValue left = this.getLeftOperand();
        AbstractExpr right = this.getRightOperand();
        Type inheritedType = left.verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr convRight = right.verifyRValue(compiler, localEnv, currentClass, inheritedType);
        this.setRightOperand(convRight);
        this.setType(inheritedType);
        return inheritedType;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        getLeftOperand().prettyPrint(s, prefix, false);
        getRightOperand().prettyPrint(s, prefix, true);
    }

    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for an assignment operation, storing the value of the right operand
     * into the memory location specified by the left operand.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {
            if (compiler.getCompilerOptions().getOPTIM()) {
                LOG.debug("Let's optimize that assign");
                getRightOperand().codeGenInstOP(compiler);
            }
            else
                getRightOperand().codeGenInst(compiler);
            if (getLeftOperand() instanceof Identifier) {
                if (((Identifier) getLeftOperand()).getDefinition().isField()) {
                    compiler.addInstruction(new LOAD(
                            new RegisterOffset(-2,Register.LB),
                            Register.getR(compiler.getStack().getCurrentRegister())
                    ));
                    compiler.addInstruction(new CMP(
                            new NullOperand(),
                            Register.getR(compiler.getStack().getCurrentRegister())
                    ));

                    compiler.addInstruction(new BEQ(compiler.getErrorHandler().addDereferencingNull()));

                    compiler.addInstruction(
                            new STORE(Register.getR(compiler.getStack().getCurrentRegister()-1),
                                    new RegisterOffset(((Identifier) getLeftOperand()).getFieldDefinition().getIndex(),
                                            Register.getR(compiler.getStack().getCurrentRegister()))
                            )
                    );
                    compiler.getStack().increaseRegister();
                    compiler.getStack().decreaseRegister();
                } else {
                    AbstractIdentifier lvalue = (AbstractIdentifier) getLeftOperand();
                    compiler.addInstruction(
                            new STORE(Register.getR(compiler.getStack().getCurrentRegister()-1),
                                    lvalue.getExpDefinition().getOperand()
                            )
                    );
                }
            } else {
                compiler.addInstruction(new STORE(
                        Register.getR(compiler.getStack().getCurrentRegister()-1),
                        ((Selection) getLeftOperand()).codeGenInstAssign(compiler)
                ));
                compiler.getStack().decreaseRegister();
            }
        }
        else {
            compiler.getStack().pushRegister(compiler);
            codeGenInst(compiler);
            compiler.getStack().popRegister(compiler);
        }
    }

    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        LOG.debug("This is codeGen assign");

        String vars = extractVariable(compiler);

        switch (vars){
            case "both":
                compiler.addInstruction(new LOAD(
                        compiler.getRegister((AbstractIdentifier) getRightOperand()),
                        compiler.getRegister((AbstractIdentifier) getLeftOperand())

                ));
                break;
            case "right":
                if (compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {

                    compiler.addInstruction(new STORE(
                            compiler.getRegister((AbstractIdentifier) getRightOperand()),
                            ((Selection) getLeftOperand()).codeGenInstAssign(compiler)
                    ));

                    compiler.getStack().decreaseRegister();
                }
                else {
                    compiler.getStack().pushRegister(compiler);
                    codeGenInstOP(compiler);
                    compiler.getStack().popRegister(compiler);
                }
                break;
            case "left":
                if (compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {
                    getRightOperand().codeGenInstOP(compiler);
                    compiler.addInstruction(new LOAD(
                            Register.getR(compiler.getStack().getCurrentRegister() -  1),
                            compiler.getRegister((AbstractIdentifier) getLeftOperand())
                    ));
                }
                else {
                    compiler.getStack().pushRegister(compiler);
                    codeGenInstOP(compiler);
                    compiler.getStack().popRegister(compiler);
                }
                break;
            default:
                codeGenInst(compiler);



        }



    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        if (compiler.getIsCritical()) {
            compiler.getIfManager().putIfAbsent((Identifier) getLeftOperand(),
                    ((Identifier) getLeftOperand()).getExpDefinition().getValue()
            );
        };
        AbstractExpr rightValue = getRightOperand().ConstantFoldingAndPropagation(compiler);
        if (rightValue != null) {
            setRightOperand(rightValue);
            if (getLeftOperand() instanceof Identifier)
                ((Identifier) getLeftOperand()).getExpDefinition().setValue(rightValue);
        }
        return null;
    }

    @Override
    public void checkAliveVariables() {
        if (getLeftOperand() instanceof Identifier)
            ((Identifier) getLeftOperand()).getExpDefinition().setValue(null);
    }


}
