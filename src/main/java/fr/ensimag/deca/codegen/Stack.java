package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.util.HashMap;

/**
 * The Stack class represents the stack management in the Deca compiler's code generation.
 * It keeps track of the address counter, the number of registers, and provides methods
 * for managing the stack during code generation.
 *
 * @author gl22
 * @date 30/12/2023
 */

public class Stack {
    private int addrCounter = 1;
    private int numberOfRegisters = 16;
    private int maxTSTO;

    private int counterTSTO;
    private int currentRegister;

    private int maxRegister;

    /**
     * The Stack constructor initializes stack-related variables.
     * It sets the maximum TSTO to 1, the counter TSTO to 1, and the current register to 2.
     * These values are set based on the requirements of the compiler.
     */
    public Stack(){

        maxTSTO = 0;
        counterTSTO = 0;
        currentRegister = 2;
    }



    /**
     * Returns the current maximum TSTO value.
     *
     * @return The maximum TSTO.
     */
    public int getMaxTSTO() {
        return maxTSTO;
    }

    /**
     * Set Number of Registers that needs to be between 4 and 16 
     * @param nbre
     */
    public void setNumberOfRegisters(int nbre){
        if(nbre >= 4 && nbre <= 16){
        this.numberOfRegisters=nbre;
        }
        else{
            System.err.println("Number of registers needs to be between 4 and 16 ");
        }
    }

    /**
     * Returns the current counter TSTO value.
     *
     * @return The counter TSTO.
     */
    public int getCounterTSTO() {
        return counterTSTO;
    }

    public void setMaxTSTO(int maxTSTOArg) {
        maxTSTO = maxTSTOArg;
    }

    public int getAddrCounter() {
        return addrCounter;
    }

    public void increaseAddrCounter(){
        addrCounter++;
    }

    public int getCurrentRegister(){
        return currentRegister;
    }

    public void setCurrentRegister(int currentRegister) {this.currentRegister = currentRegister;}
    public void resetCurrentRegister() {
        if (currentRegister > maxRegister)
            setMaxRegister(currentRegister);
        this.currentRegister = 2;
    }

    public void increaseRegister(){
        currentRegister++;
    }

    public void decreaseRegister(){
        if (currentRegister == 0) {
            System.err.println("Error: The expression exceeds the available register capacity.");
            System.exit(1);
        }
        if(currentRegister == lastLiveRegister + 1){
            System.err.println("Error: The expression exceeds the available register capacity. Thus the code cannot be optimized");
            System.exit(1);
        }
        if (currentRegister > maxRegister)
            setMaxRegister(currentRegister);
        currentRegister--;

    }

    public void resetMaxRegister() {
        maxRegister = 0;
    }

    public int getNumberOfRegisters() {
        return numberOfRegisters;
    }

    public void increaseCounterTSTO() {
        counterTSTO++;
    }
    public void increaseCounterTSTO(int i) {
        counterTSTO += i;
    }
    public void decreaseCounterTSTO() {
        if (getCounterTSTO() > getMaxTSTO())
            setMaxTSTO(getCounterTSTO());
        counterTSTO--;
    }

    public void decreaseCounterTSTO(int i) {
        if (getCounterTSTO() > getMaxTSTO())
            setMaxTSTO(getCounterTSTO());
        counterTSTO -= i;
    }

    /**
     * Increases the counter TSTO and pushes the current register onto the stack.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    public void pushRegister(DecacCompiler compiler){
        compiler.getStack().decreaseRegister();
        compiler.addInstruction(new PUSH(Register.getR(compiler.getStack().getCurrentRegister())));
    }

    public void pushRegister(DecacCompiler compiler, Register R) {
        compiler.getStack().setCurrentRegister(1);
        compiler.addInstruction(new PUSH(R));
        increaseCounterTSTO();
    }

    /**
     * Pops the current register from the stack and decreases the counter TSTO.
     *
     * @param compiler The DecacCompiler instance managing the compilation process.
     */
    public void popRegister(DecacCompiler compiler){
        compiler.addInstruction(new POP(Register.getR(compiler.getStack().getCurrentRegister())));
        compiler.getStack().increaseRegister();
        decreaseCounterTSTO();
    }

    public void resetTSTO() {
        counterTSTO = 0;
        maxTSTO = 0;
    }

    public void resetAddrCounter() {
        addrCounter = 1;
    }

    public void setMaxRegister(int maxRegister) {
        this.maxRegister = maxRegister;
    }

    public int getMaxRegister() {
        return maxRegister;
    }

    private int lastLiveRegister = -1;

    public void setLastLiveRegister() {
        this.lastLiveRegister = currentRegister;
    }

    public void resetLastLiveRegister() {
        this.lastLiveRegister = -1;
    }


}
