package jyscript.parsetree.nodes.terminal;

import jyscript.parsetree.ParseTree;
import jyscript.parsetree.nodes.IntNode;

public class IdentifierNode extends IntNode{

    private String m_Id;
    private ParseTree m_Parent;

    public IdentifierNode(ParseTree parent, String id){
        m_Id = id;
        m_Parent = parent;
    }

    public Integer eval(){
        return m_Parent.getIdentifier(m_Id);
    }
}