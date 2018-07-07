package jyscript.parsetree;

import java.util.HashMap;
import java.util.Map;

import jyscript.errors.RedeclarationError;
import jyscript.errors.UndeclaredIdentifier;
import jyscript.parsetree.nodes.ParseNode;


public class ParseTree {

    ParseNode<Void> m_Root;
    Map<String, Variable> m_SymbolTable;

    public ParseTree(){
        m_SymbolTable = new HashMap<>();
    }
    public void declareIdentifier(String name){
        declareIdentifier(name, 0);
    }
    public void declareIdentifier(String name, int initialValue){
        final Variable v =  new Variable(name, initialValue); // TODO: add line and file
        final Variable former = m_SymbolTable.put(name,v);
        if(former != null){
            throw new RedeclarationError(former, v);
        }
    }

    public void setIdentifierToValue(String id, int value){
        final Variable v = m_SymbolTable.get(id);
        if(v == null){
            throw new UndeclaredIdentifier(id, "NO file", -1); //TODO: add line and file
        }
    }

    public int getIdentifier(String name){
        final Variable v = m_SymbolTable.get(name);
        if(v == null){
            throw new UndeclaredIdentifier(name, "NO file", -1); //TODO: add line and file
        }
        return v.value();
    }
}