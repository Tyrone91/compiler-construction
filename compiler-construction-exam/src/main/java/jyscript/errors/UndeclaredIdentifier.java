package jyscript.errors;

public class UndeclaredIdentifier extends RuntimeException{
    
    private static final long serialVersionUID = 1L;

    public UndeclaredIdentifier(String identifer, String file, int line){
        super(String.format("Usage of undefined identifier '%s' in '%s:%s'.", identifer, file, line ));
    }
}