package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.util.Collections;
import java.util.Set;

public class ListDeclField extends TreeList<AbstractDeclField>{
    @Override
    public void decompile(IndentPrintStream s) {
        for(AbstractDeclField f: getList()){
            f.decompile(s);
            s.println();
        }
    }

    public EnvironmentExp verifyListDeclField(DecacCompiler compiler, AbstractIdentifier superId, AbstractIdentifier currentClass) throws ContextualError{
        EnvironmentExp envExpr = new EnvironmentExp(null);
        /*The definition of the class and the super class are ensured by pass 1*/
        currentClass.getClassDefinition().setNumberOfFields(superId.getClassDefinition().getNumberOfFields());

        for (AbstractDeclField declF : this.getList()) {
            currentClass.getClassDefinition().incNumberOfFields();
            EnvironmentExp envExp = declF.verifyField(compiler, superId, currentClass);
            Set<SymbolTable.Symbol> keyS = envExp.getExpDefinitionMap().keySet();
            Set<SymbolTable.Symbol> keySr = envExpr.getExpDefinitionMap().keySet();
            /*Vérifier si les deux environnements sont disjoints*/
            if (!Collections.disjoint(keyS, keySr)) {
                throw new ContextualError("Vous avez déclaré " + keySr + " plusieurs fois dans la classe !", declF.getLocation());
            }
            envExpr.getExpDefinitionMap().putAll(envExp.getExpDefinitionMap());
        }
        return envExpr;
    }

    public void verifyListDeclFieldI(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField declF : this.getList()) {
            declF.verifyFieldInit(compiler, localEnv, currentClass);
        }
    }

    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        for (AbstractDeclField abstractDeclField : getList()) {
            abstractDeclField.ConstantFoldingAndPropagation(compiler);
        }
    }

}
