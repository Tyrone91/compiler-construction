package jyscript.parsetree.nodes.nonterminal;

public class PlusOperatorNode extends ExpressionDerivedNode {

    protected ThermeNode m_Therme;
    protected ExpressionDerivedNode m_Right;

    public PlusOperatorNode(ThermeNode node, ExpressionDerivedNode node2){
        m_Therme = node;
        m_Right = node2;
    }

    public int eval(int a){
        return m_Right.eval(a + m_Therme.eval() );
    }
}