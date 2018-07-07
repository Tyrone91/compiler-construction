package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.ParseTree;
import jyscript.parsetree.nodes.ParseNode;

public class AssignmentNode implements ParseNode<Void>{ //TODO: extends StatementNode

    private ExpressionNode m_ExpressionNode;
    private String m_Identifier;
    private ParseTree m_Tree;

    public AssignmentNode(ParseTree tree, String identifier, ExpressionNode expressionNode){
        m_ExpressionNode = expressionNode;
        m_Identifier = identifier;
        m_Tree = tree;
        m_Tree.getIdentifier(identifier); //TODO: does this make sense.
    }

    public Void eval(){
        m_Tree.setIdentifierToValue( m_Identifier, m_ExpressionNode.eval());
        return null;
    }
}