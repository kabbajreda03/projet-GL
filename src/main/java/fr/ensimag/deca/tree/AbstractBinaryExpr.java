package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.HashSet;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Binary expressions.
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(AbstractBinaryExpr.class);


/**
 * The function returns the left operand of an abstract expression.
 * 
 * @return The method is returning the left operand of an abstract expression.
 */
    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

/**
 * The function returns the right operand of an abstract expression.
 * 
 * @return The method is returning the right operand of an abstract expression.
 */
    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

/**
 * The function sets the left operand of an expression and validates that it is not null.
 * 
 * @param leftOperand The `leftOperand` parameter is of type `AbstractExpr`, which is an abstract class
 * representing an expression.
 */
    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

/**
 * The function sets the right operand of an abstract expression and validates that it is not null.
 * 
 * @param rightOperand The rightOperand parameter is an instance of the AbstractExpr class.
 */
    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }


    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    
    /***
     * Constructor of the class
     * Validates that @param leftOperand abd @param rightOperand are not null.
     * @param leftOperand
     * @param rightOperand
     */
    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /***
     * Generate the decompiled version of the class
     * @param s PrintStream variable
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

    @Override
    public void addLiveVariable(HashSet<AbstractIdentifier> liveVariable) {
        getLeftOperand().addLiveVariable(liveVariable);
        getRightOperand().addLiveVariable(liveVariable);

    }

    public String extractVariable(DecacCompiler compiler){
        LOG.debug("getLeftOperand() instanceof AbstractIdentifier is " + (getLeftOperand() instanceof AbstractIdentifier));
        if(getLeftOperand() instanceof AbstractIdentifier && compiler.isVariableInDict((AbstractIdentifier) getLeftOperand())){
            if (getRightOperand() instanceof AbstractIdentifier && compiler.isVariableInDict((AbstractIdentifier) getRightOperand())){
                return "both";
            }
            return"left";
        }

        if(getRightOperand() instanceof AbstractIdentifier && compiler.isVariableInDict((AbstractIdentifier) getRightOperand())){
            return "right";
        }
        return "none";
    }

}
