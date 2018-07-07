package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.ParseTree;
import jyscript.parsetree.nodes.IntNode;

public class FactorNode extends IntNode {

    private IntNode m_Child;
    private boolean m_UniaryMinus;

    public FactorNode(IntNode child, boolean uniary){
        m_Child = child;
        m_UniaryMinus = uniary;
    }

    public FactorNode(IntNode child){
        this(child, false);
    }

    public Integer eval(){
        return m_UniaryMinus ? -m_Child.eval() : m_Child.eval();
    }
}