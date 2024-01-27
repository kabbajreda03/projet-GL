package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public int isOr() {
        return 1;
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


    /**
     * Generates code for a logical OR operation.
     * If the left operand or the right operand is true, the result is true.
     *
     * @param compiler The {@link DecacCompiler}  instance managing the compilation process.
     */

    @Override
    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationBool(compiler,false);
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }
}
