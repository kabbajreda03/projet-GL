package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.WNL;

/**
 * @author gl22
 * @date 01/01/2024
 */
public class Println extends AbstractPrint {

    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printlnx)
     */
    public Println(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {

        super.codeGenInst(compiler);
        compiler.addInstruction(new WNL());
    }

    @Override
    protected void codeGenInstOP(DecacCompiler compiler) {
        super.codeGenInstOP(compiler);
        compiler.addInstruction(new WNL());

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
    public void decompile(IndentPrintStream s) {
        String st = this.getPrintHex() ? "printlnx(" : "println(";
        s.print(st);
        s.print('"');
        this.getArguments().decompile(s);
        s.print('"');
        s.print(");");
    }

    @Override
    String getSuffix() {
        return "ln";
    }
}
