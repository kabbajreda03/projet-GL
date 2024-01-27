package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl22
 * @date 01/01/2024
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    public List<Type> getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Signature){
            Signature sig2 = (Signature)obj;
            return this.args.equals(sig2.args);
        }
        return false;
    }
}
