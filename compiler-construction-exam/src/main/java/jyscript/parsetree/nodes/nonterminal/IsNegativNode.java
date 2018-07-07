package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.nodes.IntNode;

public class IsNegativNode extends IntNode{

    private ExpressionNode m_ExpressionNode;

    public IsNegativNode(ExpressionNode expressionNode){
        m_ExpressionNode = expressionNode;
    }

    public Integer eval(){
        return m_ExpressionNode.eval() < 0 ? 1 : 0;
    }
}