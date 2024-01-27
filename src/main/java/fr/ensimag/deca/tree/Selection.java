package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.HashSet;

public class Selection extends AbstractLValue {

    private final AbstractExpr expression;
    private final AbstractIdentifier fieldIdent;

    public Selection(AbstractExpr expression, AbstractIdentifier fieldIdent) {
        Validate.notNull(expression);
        Validate.notNull(fieldIdent);
        this.expression = expression;
        this.fieldIdent = fieldIdent;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type typeExp = expression.verifyExpr(compiler, localEnv, currentClass);
        if (!typeExp.isClass()) {
            throw new ContextualError("The object to which you apply the selection must be of type Class: " + typeExp.getName() + " was given !", this.getLocation());
        }
        TypeDefinition classDef2 = compiler.environmentType.defOfType(typeExp.getName());
        if (classDef2 == null) {
            throw new ContextualError("Class: " + typeExp.getName() + " is not defined in local environment", this.getLocation());
        }
        EnvironmentExp envExp2 = ((ClassDefinition) classDef2).getMembers();
        if (envExp2 == null) {
            throw new ContextualError("Class: " + typeExp.getName() + " is not defined in local environment", this.getLocation());
        }
        Type fieldIdentType = fieldIdent.verifyExpr(compiler, envExp2, currentClass);
        FieldDefinition fieldDefinition = fieldIdent.getFieldDefinition();

        if (fieldDefinition.getVisibility().equals(Visibility.PROTECTED)) {
            ClassType classField = fieldDefinition.getContainingClass().getType();
            if (currentClass.getType() == null || !classField.isSubType(compiler.environmentType, currentClass.getType())) {
                throw new ContextualError("Cannot get access to field " + fieldIdent.getName() +
                        " from current class", this.getLocation());
            }
            ClassType classType = currentClass.getType();
            if (!classType.isSubType(compiler.environmentType, typeExp)) {
                throw new ContextualError("Cannot get access to field " + fieldIdent.getName() +
                        " from current class", this.getLocation());
            }
        }
        this.setType(fieldIdentType);
        return fieldIdentType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.expression.decompile(s);
        s.print(".");
        this.fieldIdent.decompile(s);

    }

    @Override
    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {
        if (expression instanceof Identifier)
            liveVariable.add((AbstractIdentifier) expression);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, false);
        fieldIdent.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
        fieldIdent.iter(f);
    }

    protected void codeGenInstGeneral(DecacCompiler compiler) {
        expression.codeGenInst(compiler);
        compiler.addInstruction(new CMP(
                new NullOperand(),
                Register.getR(compiler.getStack().getCurrentRegister() - 1)
        ));
        compiler.addInstruction(new BEQ(
                compiler.getErrorHandler().addDereferencingNull()
        ));
    }

    protected void codeGenInstGeneralOP(DecacCompiler compiler) {
        if (expression instanceof AbstractIdentifier && compiler.isVariableInDict(((AbstractIdentifier) expression))) {
            compiler.addInstruction(new LOAD(
                    compiler.getRegister((AbstractIdentifier) expression),
                    Register.getR(compiler.getStack().getCurrentRegister())
            ));
            compiler.getStack().increaseRegister();
        } else
            expression.codeGenInst(compiler);

        compiler.addInstruction(new CMP(
                new NullOperand(),
                Register.getR(compiler.getStack().getCurrentRegister() - 1)
        ));
        compiler.addInstruction(new BEQ(
                compiler.getErrorHandler().addDereferencingNull()
        ));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if(compiler.getCompilerOptions().getOPTIM())
            codeGenInstGeneralOP(compiler);
        else
            codeGenInstGeneral(compiler);

        compiler.addInstruction(new LOAD(

                new RegisterOffset(
                        fieldIdent.getFieldDefinition().getIndex(),
                        Register.getR(compiler.getStack().getCurrentRegister() - 1)
                ),

                Register.getR(compiler.getStack().getCurrentRegister() - 1)
        ));

    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return null;
    }

    @Override
    public void checkAliveVariables() {

    }

    protected DAddr codeGenInstAssign(DecacCompiler compiler) {
        codeGenInstGeneral(compiler);
        return new RegisterOffset(fieldIdent.getFieldDefinition().getIndex(),
                Register.getR(compiler.getStack().getCurrentRegister() - 1));
    }
}
