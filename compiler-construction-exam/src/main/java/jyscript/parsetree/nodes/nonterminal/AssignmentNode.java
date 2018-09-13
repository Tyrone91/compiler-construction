package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.IdentifierTable;
import jyscript.parsetree.nodes.ParseNode;

public class AssignmentNode implements ParseNode<Void>{
    private ExpressionNode m_ExpressionNode;
    private String m_Identifier;
    private IdentifierTable m_IdentifierTable;

    public AssignmentNode(IdentifierTable table, String identifier, ExpressionNode expressionNode){
        m_ExpressionNode = expressionNode;
        m_Identifier = identifier;
        m_IdentifierTable = table;
        m_IdentifierTable.getIdentifier(identifier); //check if the identifier exist. runtime vs compile time check.
    }

    public Void eval(){
        m_IdentifierTable.setIdentifierToValue( m_Identifier, m_ExpressionNode.eval());
        return null;
    }
}