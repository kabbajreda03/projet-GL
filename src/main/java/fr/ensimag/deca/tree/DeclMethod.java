package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;

public class DeclMethod extends AbstractDeclMethod{
    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam list_param;
    final private MethodBody methodBody;
    private boolean isOverride = false;

    private int methodIndex;

    private String className;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam list_param, MethodBody methodBody) {
        Validate.notNull(type);
        Validate.notNull(list_param);
        Validate.notNull(methodName);
        this.type=type;
        this.methodName = methodName;
        this.list_param=list_param;
        this.methodBody=methodBody;

    }
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        list_param.decompile(s);
        s.print(")");
        methodBody.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        list_param.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        list_param.iter(f);
        methodBody.iter(f);
    }

    public boolean isOverride() {
        return isOverride;
    }

    @Override
    protected void codeGenMethods(DecacCompiler compiler) {
        compiler.addLabel(new Label("code." + className + "." + methodName.getName()));
        int index = -3;
        for (AbstractDeclParam param : list_param.getList()) {
            param.getParamName().getExpDefinition().setOperand(new RegisterOffset(index,
                    Register.LB));
            index--;
        }
        methodBody.codeGenMethods(compiler, className,
                methodName.getName().toString(), getMethodType().isVoid());
    }

    @Override
    protected void setClassName(String className) {
        this.className = className;
    }

    @Override
    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        methodBody.ConstantFoldingAndPropagation(compiler);
    }

    @Override
    public void DeadCodeElimination() {
        methodBody.DeadCodeElimination();
    }

    @Override
    protected EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier superId, ClassDefinition classDef) throws ContextualError {
        ClassDefinition envSup = (ClassDefinition) compiler.environmentType.defOfType(superId.getName());
        Signature signature = list_param.verifyListDeclParam(compiler);

        Type typeM = type.verifyType(compiler);
        if(envSup == null){
            throw new ContextualError("Super class"+superId.getName()+"is not defined !", superId.getLocation());
        }
        ExpDefinition envExpSuper = envSup.getMembers().getExpDefinitionMap().get(methodName.getName());

        if(envExpSuper != null){
            if (!envExpSuper.isMethod()) {
                throw new ContextualError(methodName.getName() + " can't be a method because it is already defined in super class and is not a method !", methodName.getLocation());
            }
            this.isOverride = true;
            classDef.incrNbrOfOverrides();
            Signature sig2 = ((MethodDefinition) envExpSuper).getSignature();
            Type type2=envExpSuper.getType();
            if(!sig2.equals(signature)){
                throw new ContextualError(methodName.getName()+" not same signature", getLocation());
            }
            if(! type2.isSubType( compiler.environmentType,typeM)){
                throw new ContextualError(type2 + " not subtype of " + typeM, getLocation());
            }
            this.setIndex(((MethodDefinition) envExpSuper).getIndex());
        }

        EnvironmentExp envExp=new EnvironmentExp(null);
        MethodDefinition methDefReturned= new MethodDefinition(typeM, getLocation(), list_param.getSignature(),this.methodIndex);
        classDef.incNumberOfMethods();
        envExp.declare(methodName.getName(), methDefReturned);
        methodName.setDefinition(methDefReturned);
        return envExp;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition classId) throws ContextualError {
        Type returnType = type.verifyType(compiler);
        EnvironmentExp enxExpParam = list_param.verifyListParamName(compiler);

        methodBody.verifyMethodBody(compiler, localEnv,enxExpParam, classId, returnType);

    }

    @Override
    protected AbstractIdentifier getMethodName() {
        return this.methodName;
    }

    @Override
    protected Type getMethodType() {
        return this.type.getType();
    }


    @Override
    protected void setIndex(int index) {
        this.methodIndex = index;
    }

    @Override
    protected int getIndex() {
        return this.methodIndex;
    }
}