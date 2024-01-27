package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.deca.tree.StringLiteral;
import fr.ensimag.ima.pseudocode.*;

/**
 * @author Ensimag
 * @date 01/01/2024
 */
public class LOAD extends BinaryInstructionDValToReg {

    public LOAD(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public LOAD(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

    public LOAD(float f, GPRegister r) {
        this(new ImmediateFloat(f),r);
    }

    public LOAD(boolean value, GPRegister r){
        this(
                value ? 1 : 0,
                r
        );
    }

    public LOAD(Label label, GPRegister r) {
        this(new LabelOperand(label), r);
    }
}
