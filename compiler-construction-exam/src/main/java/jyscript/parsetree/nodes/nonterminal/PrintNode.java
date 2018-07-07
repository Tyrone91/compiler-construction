package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.nodes.ParseNode;

public class PrintNode implements ParseNode<Void> {
    
    ExpressionNode m_ExpressionNode;

    public PrintNode(ExpressionNode node){
        m_ExpressionNode = node;
    }

    public Void eval(){
        System.out.println(m_ExpressionNode.eval());
        return null;
    }
}