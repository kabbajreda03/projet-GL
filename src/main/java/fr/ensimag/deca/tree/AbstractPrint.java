package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import java.util.HashSet;

import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    public void setArguments(ListExpr arguments) {
        this.arguments = arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        for (AbstractExpr exp : arguments.getList()) {
            Type typePrint = exp.verifyExpr(compiler, localEnv, currentClass);
            if (!(typePrint.isInt()) && !(typePrint.isFloat()) && !(typePrint.isString())) {
                throw new ContextualError("Printable expressions can only be of type Int or Float or String: " + typePrint + " was given !", this.getLocation());
            }
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler, printHex);
        }

    }



    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrintOP(compiler, printHex);
        }
    }

    public boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        ListExpr listExpr = new ListExpr();
        for (AbstractExpr expr : getArguments().getList()) {
            AbstractExpr value = expr.ConstantFoldingAndPropagation(compiler);
            if (value != null) {
                listExpr.add(value);
            }
            else {
                listExpr.add(expr);
            }
        }
        setArguments(listExpr);
        return null;
    }

    @Override
    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {
        for (AbstractExpr expr : getArguments().getList()) {
            expr.addLiveVariable(liveVariable);
        }
    }

}
