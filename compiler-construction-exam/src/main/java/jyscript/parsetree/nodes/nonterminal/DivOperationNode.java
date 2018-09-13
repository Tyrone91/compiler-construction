package jyscript.parsetree.nodes.nonterminal;

public class DivOperationNode extends ThermeDerivedNode {

    protected FactorNode m_Factor;
    protected ThermeDerivedNode m_Right;

    public DivOperationNode(FactorNode node, ThermeDerivedNode node2){
        m_Factor = node;
        m_Right = node2;
    }
    
    @Override
    public int simpleEval(int a) {
        return a / m_Factor.eval();
    }

    public int eval(int a){
        int res = simpleEval(a);
        ThermeDerivedNode current = m_Right;
        while(current != null){
            res = current.simpleEval(res);
            current = current.next();
        }
        return res;
    }
    
    @Override
    public ThermeDerivedNode next() {
        return m_Right;
    }
    
    @Override
    public void setValue(ThermeDerivedNode value) {
        m_Right = value;
    }
}