package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.Location;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl22
 * @date 01/01/2024
 */

public class EnvironmentType {

    private final Map<Symbol, TypeDefinition> envTypes;
    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // not added to envTypes, it's not visible for the user.

        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        // not added to envTypes, it's not visible for the user.

        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb, Location.BUILTIN,null);
        ClassDefinition objectDef = OBJECT.getDefinition();
        objectDef.incNumberOfMethods();
        Signature equalsSig = new Signature();
        equalsSig.add(OBJECT);
        /*Here we consider that the index of the first method is 0 and not 1*/
        MethodDefinition equalsMeth = new MethodDefinition(BOOLEAN, Location.BUILTIN, equalsSig, 0);
        Symbol equalsSymb = compiler.createSymbol("equals");
        objectDef.getMembers().declare(equalsSymb, equalsMeth);
        envTypes.put(objectSymb, objectDef);

        
    }

    public void declareClass(AbstractIdentifier id, ClassDefinition superClass) {
        ClassType classType = new ClassType(id.getName(), id.getLocation(), superClass);
        TypeDefinition classDef = classType.getDefinition();
        envTypes.put(id.getName(), classDef);
    }

    public void put(Symbol symb, TypeDefinition typeD) {
        envTypes.put(symb, typeD);
    }

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final NullType NULL;
    public final ClassType OBJECT;

}
