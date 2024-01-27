package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.*;

/**
 * @author Ensimag
 * @date 01/01/2024
 */
public class MUL extends BinaryInstructionDValToReg {
    public MUL(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public MUL(int i, GPRegister op2) {
        this(new ImmediateInteger(i), op2);
    }
}
