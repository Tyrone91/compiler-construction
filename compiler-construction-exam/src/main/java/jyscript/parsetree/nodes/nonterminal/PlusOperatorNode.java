package jyscript.parsetree.nodes.nonterminal;

import jyscript.JYParser.ValueSetter;

public class PlusOperatorNode extends ExpressionDerivedNode implements ValueSetter<ExpressionDerivedNode> {

    protected ThermeNode m_Therme;
    protected ExpressionDerivedNode m_Right;

    public PlusOperatorNode(ThermeNode node, ExpressionDerivedNode node2){
        m_Therme = node;
        m_Right = node2;
    }
    
    @Override
    public int simpleEval(int a) {
        int res = simpleEval(a);
        ExpressionDerivedNode current = m_Right; 
        while(current != null){
            res = current.simpleEval(res);
            current = current.next();
        }
        return res;
    }
    
    public int eval(int a){
        return m_Right.eval(a + m_Therme.eval() );
    }
    
    public void setValue(ExpressionDerivedNode node){
        m_Right = node;
    }
    
    @Override
    public ExpressionDerivedNode next() {
        return m_Right;
    }
    
    @Override
    public boolean hasNext() {
        return m_Right != null;
    }
}