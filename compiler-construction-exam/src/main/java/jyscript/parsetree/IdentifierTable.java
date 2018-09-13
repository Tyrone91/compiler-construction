package jyscript.parsetree;

import java.util.HashMap;
import java.util.Map;

import jyscript.JYScanner;
import jyscript.errors.RedeclarationError;
import jyscript.errors.UndeclaredIdentifier;


public class IdentifierTable {

    private Map<String, Identifier> m_SymbolTable;
    private JYScanner m_Scanner;

    public IdentifierTable(JYScanner scanner){
        m_SymbolTable = new HashMap<>();
        m_Scanner = scanner;
    }
    
    private int line(){
    	return m_Scanner.line();
    }
    
    private String file(){
    	return m_Scanner.file();
    }
    
    public void declareIdentifier(String name){
        declareIdentifier(name, 0);
    }
    
    private void declareIdentifier(String name, int initialValue){
        final Identifier v =  new Identifier(name, initialValue, file(), line());
        final Identifier former = m_SymbolTable.put(name,v);
        if(former != null){
            throw new RedeclarationError(former, v);
        }
    }

    public void setIdentifierToValue(String id, int value){
        final Identifier v = m_SymbolTable.get(id);
        if(v == null){
            throw new UndeclaredIdentifier(id, file(), line());
        }
        v.value(value);
    }

    public int getIdentifier(String name){
        final Identifier v = m_SymbolTable.get(name);
        if(v == null){
            throw new UndeclaredIdentifier(name, file(), line());
        }
        return v.value();
    }
    
    public void printAllIdentifiers(){
    	m_SymbolTable.forEach( (name ,identifier) -> {
    		System.out.println(
    			String.format("%s := %s\t\tdeclared in %s", name, identifier.value(), identifier.fistAppearance())
    		);
    	});
    }
}