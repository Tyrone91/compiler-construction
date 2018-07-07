package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.ParseTree;
import jyscript.parsetree.nodes.ParseNode;

public class DeclareNode  implements ParseNode<Void>{ //TODO: make statement

    private String m_Identifier;
    private AssignmentNode m_AssignmentNode;

    private ParseTree m_Tree;

    public DeclareNode(ParseTree tree, String identifier, AssignmentNode assignmentNode){
        m_Identifier = identifier;
        m_Tree = tree;
        //m_Tree.declareIdentifier(m_Identifier); //TODO: does this makes sense
        m_AssignmentNode = assignmentNode;
    }

    public Void eval(){
        if(m_AssignmentNode != null){
            m_AssignmentNode.eval();
        }
        return null;
    }



    
}