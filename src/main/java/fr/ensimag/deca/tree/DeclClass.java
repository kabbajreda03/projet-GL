package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.*;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl22
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {
    final private AbstractIdentifier className;
    final private AbstractIdentifier superName;
    final private ListDeclField listField;
    final private ListDeclMethod listMethod;
    private final TreeMap<Integer, Label> methodTable = new TreeMap<>();

    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superName, ListDeclField listField, ListDeclMethod listMethod) {
        Validate.notNull(className);
        Validate.notNull(superName);
        Validate.notNull(listField);
        Validate.notNull(listMethod);
        this.className = className;
        this.superName = superName;
        this.listField = listField;
        this.listMethod = listMethod;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        className.decompile(s);
        if(!superName.getName().getName().equals("Object")){
        s.print(" extends ");
        superName.decompile(s);
        }
        s.println(" {");
        this.listField.decompile(s);
        this.listMethod.decompile(s);
        s.print("}");



    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify verifyClass: start");
        SymbolTable.Symbol superSymb = superName.getName();
        Definition superDef = compiler.environmentType.defOfType(superSymb);
        if (superDef == null) {
            throw new ContextualError("Super Class " + superSymb + " is not defined !", this.getLocation());
        }
        if (!superDef.isClass()) {
            throw new ContextualError("Identifier " + superSymb + " is not of type Class !", this.getLocation());
        }
        SymbolTable.Symbol classSymb = className.getName();
        if (compiler.environmentType.defOfType(classSymb) != null) {
            throw new ContextualError("Class " + classSymb + " is already defined !", this.getLocation());
        }
        compiler.environmentType.declareClass(className, (ClassDefinition) superDef);
        superName.setDefinition(compiler.environmentType.defOfType(superSymb));
        className.setDefinition(compiler.environmentType.defOfType(className.getName()));
        LOG.debug("verify verifyClass: end");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify verifyClassMembers: start");
        EnvironmentExp envExpf = listField.verifyListDeclField(compiler, superName, className);
        EnvironmentExp envExpm = listMethod.verifyListDeclMethod(compiler, superName, className);
        Set<SymbolTable.Symbol> keyF = envExpf.getExpDefinitionMap().keySet();
        Set<SymbolTable.Symbol> keyM = envExpm.getExpDefinitionMap().keySet();

        /*Vérifier si les deux environnements sont disjoints*/
        if (!Collections.disjoint(keyF, keyM)) {
            throw new ContextualError("Un champ et une méthode ont le même nom dans la classe " + className.getName() + " !", this.getLocation());
        }
        /*Method putAll overwrites Symbols already present in the superclass map*/
        Map<SymbolTable.Symbol, ExpDefinition> mergedMap = new HashMap<>(((ClassDefinition) compiler.environmentType.defOfType(superName.getName())).getMembers().getExpDefinitionMap());
        mergedMap.putAll(envExpm.getExpDefinitionMap());
        mergedMap.putAll(envExpf.getExpDefinitionMap());
        /*Here we build the classDefinition of our currentClass based on the updated ClassDefinition of our superClass*/
        superName.setDefinition(compiler.environmentType.defOfType(superName.getName()));
        ClassDefinition classDef = className.getClassDefinition();
        /*Here we set the members of our currentClass to complete the definition*/
        classDef.getMembers().setExpDefinitionMap(mergedMap);
        compiler.environmentType.put(className.getName(), classDef);
        classDef.setLocation(this.getLocation());
        className.setDefinition(classDef);
        className.setType(classDef.getType());
        LOG.debug("verify verifyClassMembers: end");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        /*The two first passes ensure that className is defined in environmentType as a class)*/
        EnvironmentExp envExp = ((ClassDefinition) compiler.environmentType.defOfType(className.getName())).getMembers();
        listField.verifyListDeclFieldI(compiler, envExp, className.getClassDefinition());
       listMethod.verifyListDeclMethodBody(compiler, envExp, className.getClassDefinition());
    }

    @Override
    public void codeGenDeclClass(DecacCompiler compiler) {
        compiler.getClassManager().put(className,this);
        className.getDefinition().setOperand(new RegisterOffset(compiler.getStack().getAddrCounter(), Register.GB));
        compiler.getStack().increaseAddrCounter();
        compiler.getStack().increaseCounterTSTO();

        boolean isSuperClassObject = superName.getName().equals(compiler.createSymbol("Object"));
        // define the supper class
        if (isSuperClassObject)
            compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB),Register.R0));

        else
            compiler.addInstruction(new LEA(superName.getDefinition().getOperand(),Register.R0));

        compiler.addInstruction(new STORE(Register.R0,className.getDefinition().getOperand()));

        if (!isSuperClassObject){
            methodTable.putAll(compiler.getClassManager().get(superName).getMethodTable());
        }

        for (AbstractDeclMethod method : compiler.getClassManager().get(className).listMethod.getList()){
            method.setClassName(className.getName().toString());
            int index = method.getIndex();
            methodTable.put(index, new Label("code." + className.getName().toString()
                    + "." + method.getMethodName().getName().toString()));
        }

        Program.setOperandEquals(compiler);
        for(Label methodLabel : methodTable.values()){
            Program.setOperandMethod(compiler,methodLabel);
        }


    }

    public void codeGenInitListDeclClassBody(DecacCompiler compiler) {
        if (superName.getName().toString().equals("Object")) {
            for (AbstractDeclField abstractDeclField : listField.getList()) {
                int index = ((DeclField) abstractDeclField).getFieldName().getFieldDefinition().getIndex();
                ((DeclField) abstractDeclField).getFieldName().getExpDefinition().setOperand(
                        new RegisterOffset(
                                index, Register.R1
                        )
                );
                if (((DeclField) abstractDeclField).getInitialization() instanceof NoInitialization) {
                    loadR0(compiler, abstractDeclField);
                    compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
                    compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.R1)));
                } else {
                    codeGenInit(compiler, abstractDeclField, index);
                }
            }
        }
        else {


            for (AbstractDeclField abstractDeclField : listField.getList()) {
                loadR0(compiler, abstractDeclField);
                int index = ((DeclField) abstractDeclField).getFieldName().getFieldDefinition().getIndex();
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
                compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.R1)));
            }
            compiler.addInstruction(new LOAD(
                    new RegisterOffset(-2, Register.LB),
                    Register.R1
            ));
            compiler.getStack().pushRegister(compiler, Register.R1);
            Label initBSR = new Label("init." + this.superName.getName());
            compiler.getStack().increaseCounterTSTO(2);
            compiler.addInstruction(new BSR(initBSR));
            compiler.getStack().decreaseCounterTSTO(2);
            compiler.addInstruction(new SUBSP(1));
            for (AbstractDeclField abstractDeclField : listField.getList()) {
                if ((((DeclField) abstractDeclField).getInitialization() instanceof Initialization)) {
                    int index = ((DeclField) abstractDeclField).getFieldName().getFieldDefinition().getIndex();
                    codeGenInit(compiler, abstractDeclField, index);
                }
            }
        }
    }
    @Override
    public void codeGenInitListDeclClass(DecacCompiler compiler) {
        compiler.getStack().resetMaxRegister();
        compiler.addComment("Initialize " + this.className.getName() + "'s fields");
        Label init = new Label("init." + this.className.getName());
        compiler.addLabel(init);

        ImmediateInteger integer = new ImmediateInteger(0);
        IMAProgram myProgram = compiler.getProgram();
        IMAProgram copyProgram = new IMAProgram();

        compiler.getStack().resetCurrentRegister();
        compiler.getStack().resetTSTO();
        compiler.getStack().resetAddrCounter();
        compiler.addInstruction(new TSTO(integer));
        compiler.addInstruction(new BOV(compiler.getErrorHandler().addStackOverflowError()));
        compiler.setProgram(copyProgram);

        codeGenInitListDeclClassBody(compiler);

        pushAndPopRegister(compiler, integer, myProgram, copyProgram);

    }

    static void pushAndPopRegister(DecacCompiler compiler, ImmediateInteger integer, IMAProgram myProgram, IMAProgram copyProgram) {
        int max = Math.max(compiler.getStack().getCurrentRegister(), compiler.getStack().getMaxRegister())-1;
        for (int i = max;i>1;i--) {
            compiler.getProgram().addFirst(new PUSH(
                    Register.getR(i)
            ));
            compiler.addInstruction(new POP(
                    Register.getR(i)
            ));
        }
        compiler.addInstruction(new RTS());
        myProgram.append(copyProgram);
        compiler.setProgram(myProgram);
        compiler.setProgram(myProgram);
        integer.setValue(Math.max(compiler.getStack().getMaxTSTO(), compiler.getStack().getCounterTSTO())+ max-1);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
//        throw new UnsupportedOperationException("Not yet supported");
        className.prettyPrint(s, prefix, false);
        superName.prettyPrint(s, prefix, false);
        listField.prettyPrint(s, prefix, false);
        listMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
//        throw new UnsupportedOperationException("Not yet supported");
        className.iter(f);
        superName.iter(f);
        listField.iter(f);
        listMethod.iter(f);
    }

    public AbstractIdentifier getClassName() {
        return className;
    }

    public Map<Integer, Label> getMethodTable(){
        return this.methodTable;
    }

    public void codeGenInit(DecacCompiler compiler, AbstractDeclField abstractDeclField, int index) {
        DVal dVal = className.getDval(((Initialization) ((DeclField) abstractDeclField).getInitialization()).getExpression());
        if (dVal != null) {
            compiler.addInstruction( new LOAD(
                    dVal, Register.R0
            ));
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.R1)));
        }
        else {
            ((Initialization) ((DeclField) abstractDeclField).getInitialization()).getExpression().codeGenInst(compiler);
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
            compiler.addInstruction(new STORE(Register.getR(compiler.getStack().getCurrentRegister() - 1)
                    , new RegisterOffset(index, Register.R1)));
        }
    }

    public void codeGenMethods(DecacCompiler compiler) {
        listMethod.codeGenMethods(compiler);
    }

    @Override
    public void ConstantFoldingAndPropagation(DecacCompiler compiler) {
        listField.ConstantFoldingAndPropagation(compiler);
        listMethod.ConstantFoldingAndPropagation(compiler);
    }

    @Override
    public void DeadCodeElimination() {
        listMethod.DeadCodeElimination();
    }

    public void loadR0(DecacCompiler compiler, AbstractDeclField declField) {
        if (declField.getType().isFloat()) {
            compiler.addInstruction(new FLOAT(new ImmediateInteger(0), Register.R0));
            return;
        }
        else if (declField.getType().isClassOrNull()) {
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
            return;
        }
        compiler.addInstruction(new LOAD(0, Register.R0));
    }
}
