package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.nodes.ParseNode;

public class StatementNode implements ParseNode<Void> {

    private ParseNode<Void> m_Node;

    public StatementNode( ParseNode<Void> node){
    	m_Node = node;
    }

    public Void eval(){
        m_Node.eval();
        return null;
    }
}