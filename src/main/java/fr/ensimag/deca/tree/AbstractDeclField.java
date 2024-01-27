package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclField extends Tree{

    protected abstract EnvironmentExp verifyField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier currentClass) throws ContextualError;

    protected abstract void verifyFieldInit(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError ;

    public abstract void codeGenInitListDeclClass(DecacCompiler compiler);

    public abstract Type getType();

    public abstract void ConstantFoldingAndPropagation(DecacCompiler compiler);
}
