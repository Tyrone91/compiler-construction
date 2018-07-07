package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.nodes.ParseNode;

public class WhileNode implements ParseNode<Void>{

    private ExpressionNode m_ExpressionNode;
    private StatementNode m_StatementNode;

    public WhileNode(ExpressionNode expression, StatementNode statement){
        m_ExpressionNode = expression;
        m_StatementNode = statement;
    }

    public Void eval(){
        while( m_ExpressionNode.eval() != 0){
            m_StatementNode.eval();
        }
        return null;
    }
}