package jyscript.parsetree.nodes.nonterminal;

import jyscript.JYParser.ValueSetter;

public class MinusOperatorNode extends ExpressionDerivedNode implements ValueSetter<ExpressionDerivedNode>{

    protected ThermeNode m_Therme;
    protected ExpressionDerivedNode m_Right;

    public MinusOperatorNode(ThermeNode node, ExpressionDerivedNode node2){
        m_Therme = node;
        m_Right = node2;
    }

    public int eval(int a){
        int res = simpleEval(a);
        ExpressionDerivedNode current = m_Right; 
        while(current != null){
            res = current.simpleEval(res);
            current = current.next();
        }
        return res;
    }
    
    @Override
    public int simpleEval(int a) {
        return a - m_Therme.eval();
    }

    @Override
    public void setValue(ExpressionDerivedNode value) {
        m_Right = value;
    }
    
    @Override
    public boolean hasNext() {
        return m_Right != null;
    }
}