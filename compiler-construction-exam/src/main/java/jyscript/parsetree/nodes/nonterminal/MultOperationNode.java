package jyscript.parsetree.nodes.nonterminal;

public class MultOperationNode extends ThermeDerivedNode {

    protected FactorNode m_Factor;
    protected ThermeDerivedNode m_Right;

    public MultOperationNode(FactorNode node, ThermeDerivedNode node2){
        m_Factor = node;
        m_Right = node2;
    }

    public int eval(int a){
        return m_Right.eval(a * m_Factor.eval() );
    }
}