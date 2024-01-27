package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Cast extends AbstractExpr {
    private final AbstractIdentifier type;
    private final AbstractExpr expression;

    public Cast(AbstractIdentifier type, AbstractExpr expression) {
        Validate.notNull(type);
        Validate.notNull(expression);
        this.type = type;
        this.expression = expression;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type typeCast = type.verifyType(compiler);
        Type typeExpr = expression.verifyExpr(compiler, localEnv, currentClass);
        this.castCompatible(compiler, typeExpr, typeCast);
        this.setType(typeCast);
        return typeCast;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(")");
        s.print("(");
        expression.decompile(s);
        s.print(")");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expression.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expression.iter(f);
    }

    public void castCompatible(DecacCompiler compiler, Type type1, Type type2) throws ContextualError{
        if (type1.isVoid()) {
            throw new ContextualError("Can not cast a void type !", this.getLocation());
        }
        if (assignCompatible(compiler, type1, type2) && assignCompatible(compiler, type2, type1)) {
            throw new ContextualError("Can not cast " + type1.getName() + " to " + type2.getName() + " !", this.getLocation());
        }
    }

    public boolean assignCompatible(DecacCompiler compiler, Type type1, Type type2) throws ContextualError{
        if (!(type1.isFloat() && type2.isInt())) {
            if (!(type1.isSubType(compiler.environmentType, type2))) {
                throw new ContextualError( "Can not cast " + type1.getName() + " to " + type2.getName() + " !", this.getLocation());
            }
        }
        return false;
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        expression.codeGenInst(compiler);
        compiler.addInstruction(new LEA(
                type.getDefinition().getOperand(),
                Register.getR(compiler.getStack().getCurrentRegister())
        ));
        compiler.addInstruction(new STORE(
                Register.getR(compiler.getStack().getCurrentRegister()),
                new RegisterOffset(0,  Register.getR(compiler.getStack().getCurrentRegister()-1))
        ));
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return null;
    }

    @Override
    public void checkAliveVariables() {

    }
}
