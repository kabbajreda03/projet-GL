package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam{

    private final AbstractIdentifier type;
    private final AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        Validate.notNull(type);
        Validate.notNull(paramName);
        this.type = type;
        this.paramName = paramName;
    }
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        paramName.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        paramName.iter(f);
    }

    @Override
    protected Type verifyParam(DecacCompiler compiler) throws ContextualError {
        Type typeP = this.type.verifyType(compiler);
        if (typeP.isVoid()){
            throw new ContextualError("Type of param " + paramName.getName() + " must not be of Void type !", this.getLocation());
        }
        return typeP;
    }

    @Override
    protected EnvironmentExp verifyParamName(DecacCompiler compiler) throws ContextualError {
        Type typeP =  this.type.verifyType(compiler);
        EnvironmentExp exp = new EnvironmentExp(null);
        ParamDefinition paramDef = new ParamDefinition(typeP, getLocation());
        exp.declare(paramName.getName(),paramDef);
        paramName.setDefinition(paramDef);
        return exp;
    }

    @Override
    protected AbstractIdentifier getParamName() {return paramName;}

    @Override
    protected Type getType() {
        return this.type.getType();
    }
}
