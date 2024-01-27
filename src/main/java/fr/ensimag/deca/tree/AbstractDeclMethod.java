package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

public abstract class AbstractDeclMethod extends Tree {
    protected abstract EnvironmentExp verifyMethod(DecacCompiler compiler, AbstractIdentifier superId, ClassDefinition className) throws ContextualError;

    protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition classId) throws ContextualError;

    protected abstract AbstractIdentifier getMethodName();

    protected abstract Type getMethodType();

    protected abstract void setIndex(int index);

    protected abstract int getIndex();

    protected abstract boolean isOverride();

    protected abstract void codeGenMethods(DecacCompiler compiler);

    protected abstract void setClassName(String className);

    public abstract void ConstantFoldingAndPropagation(DecacCompiler compiler);

    public abstract void DeadCodeElimination();
}
