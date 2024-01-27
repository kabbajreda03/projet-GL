package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.HashSet;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class Print extends AbstractPrint {
    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printx)
     */
    public Print(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        String st = this.getPrintHex() ? "printx(" : "print(";
        s.print(st);
        s.print('"');
        this.getArguments().decompile(s);
        s.print('"');
        s.print(");");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        super.codeGenInst(compiler);
    }

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return super.ConstantFoldingAndPropagation(compiler);
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }

    @Override
    String getSuffix() {
        return "";
    }
}
