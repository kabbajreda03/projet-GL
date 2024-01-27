package fr.ensimag.deca.context;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

/**
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class BooleanType extends Type {

    public BooleanType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isBoolean() {
        return true;
    }


// TODO step B
    @Override
    public boolean sameType(Type otherType) {
        //throw new UnsupportedOperationException("not yet implemented");
        return otherType.isBoolean();
    }


}
