package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{
    private final AbstractExpr expression;
    private final AbstractIdentifier methodIdent;
    private final ListExpr listExpression;


    public MethodCall(AbstractExpr expression, AbstractIdentifier methodIdent, ListExpr listExpression) {
        Validate.notNull(expression);
        Validate.notNull(methodIdent);
        Validate.notNull(listExpression);
        this.expression = expression;
        this.methodIdent = methodIdent;
        this.listExpression = listExpression;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type typeExp = expression.verifyExpr(compiler, localEnv, currentClass);
        if (!typeExp.isClass()) {
            throw new ContextualError("The object to which we apply the method call must be of type Class: " + typeExp.getName() + " was given !", this.getLocation());
        }
        TypeDefinition classDef2 = compiler.environmentType.defOfType(typeExp.getName());
        if (classDef2 == null) {
            throw new ContextualError("Class: " + typeExp.getName() +" is not defined in local environment", this.getLocation());
        }
        EnvironmentExp envExp2 = ((ClassDefinition) classDef2).getMembers();
        Type methodIdentType = methodIdent.verifyExpr(compiler, envExp2, currentClass);
        if (!methodIdent.getDefinition().isMethod()) {
            throw new ContextualError(methodIdent.getName() + " is not a method, you can't call it on an object !", this.getLocation());
        }
        Signature sig = methodIdent.getMethodDefinition().getSignature();
        if(sig.size() != listExpression.getList().size()){
            throw new ContextualError("Number of method arguments is not respected, this method accepts " + sig.getArgs().size() +" argument(s), " + listExpression.getList().size() + " arguments were given !", this.getLocation());
        }
        listExpression.verifyListRValues(compiler, localEnv, currentClass, sig);
        this.setType(methodIdentType);
        return methodIdentType;
    }



    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, false);
        methodIdent.prettyPrint(s, prefix, false);
        listExpression.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
        methodIdent.iter(f);
        listExpression.iter(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int size = listExpression.size();

        compiler.addComment("Call Method " + methodIdent.getName());
        compiler.addInstruction(new ADDSP(size + 1));
        compiler.getStack().increaseCounterTSTO(size + 1);

        expression.codeGenInst(compiler);
        compiler.getStack().decreaseRegister();

        compiler.addInstruction(new STORE(
                Register.getR(compiler.getStack().getCurrentRegister()),
                new RegisterOffset(0, Register.SP)
        ));

        AbstractExpr abstractExpr;
        for(int i = size - 1; i >= 0; --i){
            abstractExpr = listExpression.getList().get(size - i - 1);

            abstractExpr.codeGenInst(compiler);
            compiler.getStack().decreaseRegister();

            compiler.addInstruction(new STORE(
                    Register.getR(compiler.getStack().getCurrentRegister()),
                    new RegisterOffset(i - size, Register.SP)
            ));
        }

        compiler.addInstruction(new LOAD(
                new RegisterOffset(0, Register.SP),
                Register.getR(compiler.getStack().getCurrentRegister())
        ));

        compiler.addInstruction(new CMP(
                new NullOperand(),
                Register.getR(compiler.getStack().getCurrentRegister())
        ));

        compiler.addInstruction(new BEQ(compiler.getErrorHandler().addDereferencingNull()));

        compiler.addInstruction(new LOAD(
                new RegisterOffset(0, Register.getR(compiler.getStack().getCurrentRegister())),
                Register.getR(compiler.getStack().getCurrentRegister())
        ));
        compiler.getStack().increaseCounterTSTO(2);
        compiler.addInstruction(new BSR(new RegisterOffset(
                methodIdent.getMethodDefinition().getIndex()+1,
                Register.getR(compiler.getStack().getCurrentRegister()))
        ));
        compiler.getStack().decreaseCounterTSTO(2);
        compiler.addInstruction(new SUBSP(size + 1));
        compiler.getStack().decreaseCounterTSTO(size + 1);
        if (!getType().isVoid()) {
            compiler.addInstruction(new LOAD(
                    Register.R0,
                    Register.getR(compiler.getStack().getCurrentRegister())
                    ));
        }
        compiler.getStack().increaseRegister();

    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return null;
    }

    @Override
    public void checkAliveVariables() {

    }
}
