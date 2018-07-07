package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.nodes.IntNode;

public class ExpressionNode extends IntNode {

    ThermeNode m_Therme;
    ExpressionDerivedNode m_Derived;

    public ExpressionNode(ThermeNode node, ExpressionDerivedNode derivedNode ){
        m_Derived = derivedNode;
        m_Therme = node;
    }

    public ExpressionNode(ThermeNode node){
        this(node, new ExpressionDerivedNode());
    }

    public Integer eval(){
        return m_Derived.eval(m_Therme.eval());
    }
}