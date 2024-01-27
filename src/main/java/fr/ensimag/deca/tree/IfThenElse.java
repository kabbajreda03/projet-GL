package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class IfThenElse extends AbstractInst {

    private AbstractExpr condition;
    private ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        condition.verifyCondition(compiler, localEnv, currentClass);
        thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    private static int counterIf = -1;

    private void increaseCounterIf() {
        counterIf++;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (compiler.getStack().getCurrentRegister() < compiler.getStack().getNumberOfRegisters()) {
            increaseCounterIf();
            int number = counterIf;
            Label ifBranch = new Label("if_branch_" + number);
            Label elseBranch_ = new Label("else_branch_" + number);
            Label endOfIf = new Label("end_of_if_" + number);

            if (compiler.getCompilerOptions().getOPTIM())
                condition.codeGenInstOP(compiler);
            else
                condition.codeGenInst(compiler);

            compiler.addInstruction(new CMP(
                    0,
                    Register.getR(compiler.getStack().getCurrentRegister() - 1)
            ));
            compiler.getStack().decreaseRegister();
            compiler.addInstruction(new BEQ(elseBranch_));

            compiler.addLabel(ifBranch);


            thenBranch.codeGenListInst(compiler);

            compiler.addInstruction(new BRA(endOfIf));

            compiler.addLabel(elseBranch_);


            elseBranch.codeGenListInst(compiler);


            compiler.addLabel(endOfIf);
        } else {
            compiler.getStack().pushRegister(compiler);
            codeGenInst(compiler);
            compiler.getStack().popRegister(compiler);

        }


    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        boolean isCritical = compiler.getIsCritical();

        //constant folding and propagation for the condition
        AbstractExpr conditionValue = condition.ConstantFoldingAndPropagation(compiler);
        if (conditionValue != null)
            condition = conditionValue;

        compiler.setIsCritical(true);

        //constant folding and propagation for then branch
        for (AbstractInst abstractInst : thenBranch.getList()) {
            abstractInst.ConstantFoldingAndPropagation(compiler);
        }

        // restoring all variables before if
        for (AbstractIdentifier var : compiler.getIfManager().keySet()) {
            var.getExpDefinition().setValue(
                    compiler.getIfManager().get(var)
            );
        }

        //constant folding and propagation for else branch
        for (AbstractInst abstractInst : elseBranch.getList()) {
            abstractInst.ConstantFoldingAndPropagation(compiler);
        }

        // setting all variables in if to null
        for (AbstractIdentifier var : compiler.getIfManager().keySet()) {
            var.getExpDefinition().setValue(
                    null
            );
        }
        compiler.setIfManager(new HashMap<>());


        compiler.setIsCritical(isCritical);
        return null;
    }

    @Override
    public void checkAliveVariables() {
        thenBranch.checkAliveVariables();
        elseBranch.checkAliveVariables();
    }

    @Override
    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {
        condition.addLiveVariable(liveVariable);
        thenBranch.addLiveVariable(liveVariable);
        elseBranch.addLiveVariable(liveVariable);
    }

    public ListInst DeadCodeElimination() {
        if (condition instanceof BooleanLiteral) {
            if (((BooleanLiteral) condition).getValue())
                return thenBranch.DeadCodeElimination();
            else
                return elseBranch.DeadCodeElimination();
        } else {
            ListInst listInst = new ListInst();
            listInst.add(this);
            return listInst;
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        String st = "if";
        s.print(st);
        s.print(" (");
        condition.decompile(s);
        s.print(") ");
        s.println(" {");
        thenBranch.decompile(s);
        s.println("}");
        s.println("else {");
        elseBranch.decompile(s);
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }


}
