package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

/**
 * Type defined by a class.
 *
 * @author gl22
 * @date 01/01/2024
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    
// TODO
    @Override
    public boolean sameType(Type otherType) {
        return otherType.isClass();
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    // TODO
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        if (potentialSuperClass.getName().equals(this.getName()))
            return true;
        if (potentialSuperClass.getName().toString().equals("Object")) {
            return true;
        }
        ClassType realSuperClass =  this.getDefinition().getSuperClass().getType();
        if(realSuperClass.getName().equals(potentialSuperClass.getName())) {
            return true;
        }
        //here we verify if the realSuperClass is Object or not
        if ((realSuperClass.getDefinition().getSuperClass() != null &&
                realSuperClass.isSubClassOf(potentialSuperClass))) {
            return true;
        }
        return false;
    }


}
