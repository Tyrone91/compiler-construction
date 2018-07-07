package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.nodes.ParseNode;

public class IfNode implements ParseNode<Void> {

    private ExpressionNode m_ExpressionNode;
    private StatementNode m_ThenNode;
    private StatementNode m_ElseNode;

    public IfNode(ExpressionNode expressionNode, StatementNode then, StatementNode else_stmt){
        m_ExpressionNode = expressionNode;
        m_ThenNode = then;
        m_ElseNode = else_stmt;
    }

    public Void eval(){
        if(m_ExpressionNode.eval() != 0){
            m_ThenNode.eval();
        }else{
            m_ElseNode.eval();
        }
        return null;
    }
}