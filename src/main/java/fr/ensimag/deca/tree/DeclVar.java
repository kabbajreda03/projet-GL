package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type ty = type.verifyType(compiler);
        if (ty.isVoid()) {
            throw new ContextualError("Type of variable must not be void", type.getDefinition().getLocation());
        }
        if (localEnv.getInCurrentEnv(varName.getName()) != null) {
            throw new ContextualError("Name " + varName.getName() + " is already defined in localEnv !", this.getLocation());
        }
        VariableDefinition varDef = new VariableDefinition(ty, varName.getLocation());
        varName.setDefinition(varDef);
        //expDef.put(name, varDef);
        localEnv.declare(varName.getName(), varDef);
        initialization.verifyInitialization(compiler, ty, localEnv, currentClass);

    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }


    /**
     * Implements code generation logic for the declaration of a variable.
     * Sets the operand for the variable's expression definition and generates
     * initialization code for the variable.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    public void codeGenDeclVar(DecacCompiler compiler){
        varName.getExpDefinition().setOperand(
                new RegisterOffset(
                        compiler.getStack().getAddrCounter(),
                        Register.GB
                )
        );
        compiler.getStack().increaseAddrCounter();
        initialization.codeGenInitialization(compiler, varName);

    }

    @Override
    public void codeGenMethods(DecacCompiler compiler) {
        varName.getExpDefinition().setOperand(
                new RegisterOffset(
                        compiler.getStack().getAddrCounter(),
                        Register.LB
                )
        );
        compiler.getStack().increaseAddrCounter();
        initialization.codeGenInitialization(compiler,varName);
    }

    @Override
    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        AbstractExpr value = initialization.ConstantFoldingAndPropagation(compiler);
        varName.getExpDefinition().setValue(value);
    }
}
