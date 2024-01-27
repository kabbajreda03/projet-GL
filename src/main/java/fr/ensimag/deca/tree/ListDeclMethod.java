package fr.ensimag.deca.tree;

import java.util.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    //static int indexCounter = 0;

    @Override
    public void decompile(IndentPrintStream s) {
       for(AbstractDeclMethod m:getList()){
        m.decompile(s);
        s.println();
       }

    }
    public EnvironmentExp verifyListDeclMethod(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier className) throws ContextualError{
        ClassDefinition supClass = (ClassDefinition) compiler.environmentType.defOfType(superId.getName());
        ClassDefinition classDef = (ClassDefinition)compiler.environmentType.defOfType(className.getName());

        int indexCounter = supClass.getNumberOfAllMethods() - supClass.getNbrOfOverrides();
        EnvironmentExp envExpr =  new EnvironmentExp(null);
        for(AbstractDeclMethod meth : this.getList()){
            indexCounter++;
            meth.setIndex(indexCounter);
            EnvironmentExp envExp = meth.verifyMethod(compiler, superId, classDef);

            if( meth.isOverride()){
                indexCounter--;
            }

            Set<SymbolTable.Symbol> keyS = envExp.getExpDefinitionMap().keySet();
            Set<SymbolTable.Symbol> keySr = envExpr.getExpDefinitionMap().keySet();
            /*Vérifier si les deux environnements sont disjoints*/
            if (!Collections.disjoint(keyS, keySr)) {
                throw new ContextualError("Vous avez déclaré la méthode " + keySr + " plusieurs fois dans la classe !", meth.getLocation());
            }
            envExpr.getExpDefinitionMap().putAll(envExp.getExpDefinitionMap());
        }
        return envExpr;
    }

    public void verifyListDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError{
       for(AbstractDeclMethod meth: this.getList()){
        meth.verifyMethodBody(compiler, localEnv, currentClass);
       }
    }

    public void codeGenMethods(DecacCompiler compiler) {
        for (AbstractDeclMethod method : getList()) {
            method.codeGenMethods(compiler);
        }
    }

    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        for (AbstractDeclMethod abstractDeclMethod : getList()) {
            abstractDeclMethod.ConstantFoldingAndPropagation(compiler);
        }
    }

    public void DeadCodeElimination() {
        for (AbstractDeclMethod abstractDeclMethod : getList()) {
            abstractDeclMethod.DeadCodeElimination();
        }
    }

}
