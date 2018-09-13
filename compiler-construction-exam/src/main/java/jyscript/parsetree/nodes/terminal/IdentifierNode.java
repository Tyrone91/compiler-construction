package jyscript.parsetree.nodes.terminal;

import jyscript.parsetree.IdentifierTable;
import jyscript.parsetree.nodes.IntNode;

public class IdentifierNode extends IntNode{

    private String m_Id;
    private IdentifierTable m_IdentifierTable;

    public IdentifierNode(IdentifierTable table, String id){
        m_Id = id;
        m_IdentifierTable = table;
        m_IdentifierTable.getIdentifier(id);
    }

    public Integer eval(){
        return m_IdentifierTable.getIdentifier(m_Id);
    }
}