package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.IdentifierTable;
import jyscript.parsetree.nodes.ParseNode;

public class DeclareNode  implements ParseNode<Void>{

    private String m_Identifier;
    private AssignmentNode m_AssignmentNode;

    private IdentifierTable m_Table;

    public DeclareNode(IdentifierTable table, String identifier, AssignmentNode assignmentNode){
        m_Identifier = identifier;
        m_Table = table;
        m_AssignmentNode = assignmentNode;
    }

    public Void eval(){
        //m_Table.declareIdentifier(m_Identifier);// runtime vs compiler check see constructor
        if(m_AssignmentNode != null){
            m_AssignmentNode.eval();
        }
        return null;
    }



    
}