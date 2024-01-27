package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import org.antlr.v4.runtime.atn.SemanticContext;

/**
 *
 * @author gl22
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {

    private static int counter = 0;

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


    protected AbstractExpr ConstantFoldingAndPropagation(DecacCompiler compiler) {
        return ConstantFoldingAndPropagationBool(compiler, true);
    }

    @Override
    public void checkAliveVariables() {
        // nothing to do
    }



    @Override
    public int isOr() {
        return 0;
    }


}
