package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl22
 * @date 01/01/2024
 */
public class ListExpr extends TreeList<AbstractExpr> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractExpr exp : super.getList()){
            exp.decompile(s);
        }
    }

    public void verifyListRValues(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Signature sig) throws ContextualError{
        int paramCounter = 0;

        for(AbstractExpr rvalue : this.getList()){
            Type requiredType = sig.paramNumber(paramCounter);

            Type rvalueType = rvalue.verifyRValue(compiler, localEnv, currentClass, requiredType).getType();
            if (!rvalueType.sameType(requiredType)){
                throw new ContextualError("The type of " + rvalue.getType().getName() +
                        " is not compatible with the parameter type: " + requiredType + " !", rvalue.getLocation());
            }
            paramCounter++;
        }
    }

}