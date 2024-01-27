package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import java.util.Collections;
import java.util.Set;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

    Signature sig;

    public Signature getSignature() {
        return sig;
    }

    public void setSignature(Signature sig) {
        this.sig = sig;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam exp : this.getList()){
           if(exp != null){
            this.decompile(s);
            s.print(",");
            exp.decompile(s);
           }
        }
    }

    public Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        for (AbstractDeclParam param : this.getList()){
            sig.add(param.verifyParam(compiler));
        }
        this.setSignature(sig);
        return sig;
    }

    public EnvironmentExp verifyListParamName(DecacCompiler compiler) throws ContextualError{
        EnvironmentExp envExpr = new EnvironmentExp(null);
        for(AbstractDeclParam param:this.getList()){
            EnvironmentExp exp = param.verifyParamName(compiler);
            Set<SymbolTable.Symbol> keysExp = exp.getExpDefinitionMap().keySet();
            Set<SymbolTable.Symbol> keysExpR = envExpr.getExpDefinitionMap().keySet();
            if (!Collections.disjoint(keysExp, keysExpR)) {
                throw new ContextualError("Vous avez déclaré " + keysExp + " plusieurs fois dans la classe !", param.getLocation());
            }
            envExpr.getExpDefinitionMap().putAll(exp.getExpDefinitionMap());
        }
        return envExpr;
    }

}
