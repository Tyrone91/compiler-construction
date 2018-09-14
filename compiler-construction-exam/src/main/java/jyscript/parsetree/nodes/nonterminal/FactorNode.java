package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.nodes.IntNode;

public class FactorNode extends IntNode {

    private IntNode m_Child;
    private boolean m_UnaryMinus;

    public FactorNode(IntNode child, boolean unaryMinus){
        m_Child = child;
        m_UnaryMinus = unaryMinus;
    }

    public FactorNode(IntNode child){
        this(child, false);
    }

    @Override
    public Integer eval(){
        return m_UnaryMinus ? -m_Child.eval() : m_Child.eval();
    }
}