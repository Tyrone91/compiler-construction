package jyscript.errors;

import jyscript.parsetree.Identifier;

public class RedeclarationError extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RedeclarationError(Identifier former, Identifier redeclared){
        super(String.format("Identifier '%s' was already declared in '%s'. Redclaration in '%s'", former.name(), former.fistAppearance(), redeclared.fistAppearance() ));
    }
}