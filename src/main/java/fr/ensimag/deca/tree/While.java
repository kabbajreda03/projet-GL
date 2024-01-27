package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class While extends AbstractInst {

    private static final Logger LOG = Logger.getLogger(While.class);
    private AbstractExpr condition;
    private ListInst body;

    private final HashSet<AbstractIdentifier> liveVariables = new HashSet<>();

    private static int counter = 0;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    /**
     * Overrides the instruction code generation method for a specific expression.
     * Generates instructions for a while loop, including the condition evaluation and loop body.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {
            int i = counter;
            increaseCounter();
            Label whileLabel = new Label("while_" + i);
            Label endOfWhile = new Label("end_of_while_" + i);
            compiler.addLabel(whileLabel);
            condition.codeGenInst(compiler);
            compiler.addInstruction(new CMP(0, Register.getR(compiler.getStack().getCurrentRegister() - 1)));
            compiler.addInstruction(new BEQ(endOfWhile));
            compiler.getStack().decreaseRegister();
            body.codeGenListInst(compiler);
            compiler.addInstruction(new BRA(whileLabel));
            compiler.addLabel(endOfWhile);
        } else {
            compiler.getStack().pushRegister(compiler);
            codeGenInst(compiler);
            compiler.getStack().popRegister(compiler);
        }
    }

    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        if (compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {

            for (AbstractIdentifier abstractIdentifier : liveVariables) {
                LOG.debug("this variable " + abstractIdentifier + " is in liveVariables");
            }


            //Loop inversion
            int num = counter;
            increaseCounter();
            Label endOfWile = new Label("end_of_while_" + num);
            Label startOfWile = new Label("start_while_" + num);

            // if (condition)
            this.condition.codeGenInstOP(compiler);
            compiler.addInstruction(new CMP(
                    0,
                    Register.getR(compiler.getStack().getCurrentRegister() - 1)
            ));
            compiler.getStack().decreaseRegister();

            compiler.addInstruction(new BEQ(endOfWile));

            LOG.info("There are " + liveVariables.size() + " live variables in the while ...");
            LOG.debug("The variables are: " + liveVariables);

            Boolean isInWhile = compiler.isInWhile();
            // Load live variables
            if (!isInWhile)
                loadLiveVariable(compiler);

            // Start + Body
            compiler.addLabel(startOfWile);

            body.codeGenListInst(compiler);

            // while (cond)
            this.condition.codeGenInstOP(compiler);
            compiler.addInstruction(new CMP(
                    0,
                    Register.getR(compiler.getStack().getCurrentRegister() - 1)
            ));
            compiler.getStack().decreaseRegister();


            compiler.addInstruction(new BNE(startOfWile));
            compiler.addLabel(endOfWile);

            //Store variables
            if (!isInWhile)
                storeLiveVariable(compiler);


        } else {
            compiler.getStack().pushRegister(compiler);
            codeGenInstOP(compiler);
            compiler.getStack().popRegister(compiler);
        }

    }



    private void loadLiveVariable(DecacCompiler compiler) {

        // Getting into a while is marked by loading all the variables in registers
        compiler.getInWhile();

        LOG.debug("We got to \"loadLiveVariable\"... That's something... ");


        compiler.initVariablesDict();
        Iterator<AbstractIdentifier> iterator = liveVariables.iterator();
        int i = compiler.getStack().getCurrentRegister();
        LOG.debug("Has next is " + iterator.hasNext());

        for (; (i < compiler.getStack().getNumberOfRegisters() - 4) && iterator.hasNext(); ++i  ) {

            AbstractIdentifier abstractIdentifier = iterator.next();
            LOG.info("Let's see those variables ...");
            compiler.addInstruction(new LOAD(
                    abstractIdentifier.getExpDefinition().getOperand(),
                    Register.getR(compiler.getStack().getCurrentRegister())
            ));

            compiler.addToVariablesDict(abstractIdentifier, Register.getR(compiler.getStack().getCurrentRegister()));
            compiler.getStack().setLastLiveRegister();
            compiler.getStack().increaseRegister();
            LOG.debug("The current register is " + compiler.getStack().getCurrentRegister());

        }
    }

    private void storeLiveVariable(DecacCompiler compiler) {
        // reset live register so that the program doesn't throw an error
        compiler.getStack().resetLastLiveRegister();
        // Getting out of a while is marked by storing all the variables in registers
        compiler.getOutWhile();

        for (AbstractIdentifier abstractIdentifier : compiler.getVariables()) {

            compiler.addInstruction(new STORE(
                    compiler.getRegister(abstractIdentifier),
                    abstractIdentifier.getExpDefinition().getOperand()
            ));
            compiler.getStack().decreaseRegister();
        }
        compiler.initVariablesDict();
    }


    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        //FIXME
        AbstractExpr conditionValue = condition.ConstantFoldingAndPropagation(compiler);
        if (conditionValue instanceof BooleanLiteral && !((BooleanLiteral) conditionValue).getValue())
            condition = conditionValue;


        body.checkAliveVariables();
        for (AbstractInst abstractInst : body.getList()) {
            abstractInst.ConstantFoldingAndPropagation(compiler);
        }
        if (compiler.isPass3())
            body.addLiveVariable(liveVariables);
        return null;
    }

    public ListInst DeadCodeElimination() {
        ListInst listInst = new ListInst();
        if (condition instanceof BooleanLiteral && !((BooleanLiteral) condition).getValue()) {
            return listInst;
        }
        listInst.add(this);
        return listInst;
    }

    @Override
    public void checkAliveVariables() {
        body.checkAliveVariables();
    }

    @Override
    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {
        body.addLiveVariable(liveVariable);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    private void increaseCounter() {
        counter++;
    }


}
