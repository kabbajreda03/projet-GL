package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.util.HashMap;

/**
 * The ErrorHandler class manages error labels and generation during code generation.
 * It provides methods to add error labels, retrieve labels by error message,
 * and generate assembly code for raising errors during program execution.
 *
 * @author gl22
 * @date 30/12/2023
 */
public class ErrorHandler {
    HashMap<String, Label> errors;

    /**
     * The ErrorHandler constructor initializes the errors HashMap.
     */
    public ErrorHandler(){
        errors = new HashMap<>();
    }

    /**
     * Adds a stack overflow error label to the errors HashMap.
     * If the label already exists, returns the existing label.
     *
     * @return The stack overflow error label.
     */

    public Label addStackOverflowError(){
        Label label = new Label("stack_overflow_error");
        errors.putIfAbsent(
                "\"Error: Stack Overflow\"",
                label
        );
        return label;
    }

    /**
     * Adds a division by zero error label to the errors HashMap.
     * If the label already exists, returns the existing label.
     *
     * @return The division by zero error label.
     */
    public Label addDivisionByZero(){
        Label label = new Label("division_by_zero");
        errors.putIfAbsent(
                "Error: Division by Zero",
                label
                );
        return label;
    }

    public Label addDereferencingNull() {
        Label label = new Label("dereferencing.null");
        errors.putIfAbsent("\"Error: dereferencing null\"",
                label
                );
        return label;
    }

    public Label addInvalidIntegerInput(){
        Label label = new Label("invalid_integer_input");
        errors.putIfAbsent(
                "\"Error: Invalid Integer Input\"",
                label
        );
        return label;
    }

    public Label addInvalidFloatInput(){
        Label label = new Label("invalid_float_input");
        errors.putIfAbsent(
                "\"Error: Invalid Float Input\"",
                label
        );
        return label;
    }

    public Label addModuloByZero(){
        Label label = new Label("modulo_by_zero");
        errors.putIfAbsent(
                "\"Error: Modulo by Zero\"",
                label
        );
        return label;
    }

    public Label addOverflow(){
        Label label = new Label("overflow");
        errors.putIfAbsent(
                "\"Error: Overflow\"",
                label
        );
        return label;
    }
    
    public Label addFullStack() {
        Label label = new Label("full_stack");
        errors.putIfAbsent(
                "\"Error : The Stack is Full\"",
                label);
        return label;
    }

    public Label getLabel(String errorMessage){
        return errors.get((errorMessage));
    }


    /**
     * Generates assembly code for raising errors during program execution.
     * It adds labels, error messages, new lines, and the ERROR instruction.
     *
     * @param compiler The program's compiler.
     */
    public void putErrors(DecacCompiler compiler){
        for(String errorMessage : errors.keySet()){
            compiler.addLabel(getLabel(errorMessage));
            compiler.addInstruction(new WSTR(new ImmediateString(errorMessage)));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
    }
}
