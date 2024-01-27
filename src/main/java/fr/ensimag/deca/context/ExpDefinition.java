package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Definition associated to identifier in expressions.
 *
 * @author gl22
 * @date 01/01/2024
 */
public abstract class ExpDefinition extends Definition {


    private AbstractExpr value = new IntLiteral(42);

    public AbstractExpr getValue() {
        return value;
    }

    public void setValue(AbstractExpr value) {
        this.value = value;
    }

    public ExpDefinition(Type type, Location location) {
        super(type, location);
    }

}
