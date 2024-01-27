package fr.ensimag.deca.context;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.ConvFloat;
import fr.ensimag.deca.tree.Divide;
import fr.ensimag.deca.tree.Minus;
import fr.ensimag.deca.tree.Plus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import fr.ensimag.deca.tree.ConvFloat;

public class TestConvFloat {
    
    final Type Float = new FloatType(null);
    final Type Int = new IntType(null);

    @Mock
    AbstractExpr intexpr;

    DecacCompiler compiler;


    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        when(intexpr.verifyExpr(compiler, null, null)).thenReturn(Int);
    }

    @Test
    public void testConversionToFloat() throws ContextualError{
        ConvFloat convFloat = new ConvFloat(intexpr);
        // check if Conversion type is Float
        assertTrue(convFloat.verifyExpr(compiler, null, null).isFloat());

    }
}
