package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.HashSet;

/**
 * 
 * @author gl22
 * @date 01/01/2024
 */
public class ListInst extends TreeList<AbstractInst> {

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     */    
    public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        for (AbstractInst inst : this.getList()) {
            inst.verifyInst(compiler, localEnv, currentClass, returnType);
        }
    }

    public void codeGenListInst(DecacCompiler compiler) {
        if (compiler.getCompilerOptions().getOPTIM()) {
            for (AbstractInst i : getList()) {
                i.codeGenInstOP(compiler);
                if (i instanceof AbstractExpr)
                    compiler.getStack().decreaseRegister();

            }
        }
        else {
            for (AbstractInst i : getList()) {
                i.codeGenInst(compiler);
                if (i instanceof AbstractExpr)
                    compiler.getStack().decreaseRegister();

            }
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }

    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            i.ConstantFoldingAndPropagation(compiler);
        }
    }

    public void checkAliveVariables() {
        for (AbstractInst i : getList()) {
            i.checkAliveVariables();
        }
    }

    public ListInst DeadCodeElimination() {
        ListInst listInst = new ListInst();
        for (AbstractInst i : getList()) {
            if (i instanceof IfThenElse ) {
                for (AbstractInst inst : ((IfThenElse) i).DeadCodeElimination().getList()) {
                    listInst.add(inst);
                }
            }
            else if (i instanceof While ) {
                for (AbstractInst inst : ((While) i).DeadCodeElimination().getList()) {
                    listInst.add(inst);
                }
            }
            else
                listInst.add(i);
        }
        return listInst;
    }

    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {
        for (AbstractInst i : getList()) {
            i.addLiveVariable(liveVariable);
        }
    }
}
