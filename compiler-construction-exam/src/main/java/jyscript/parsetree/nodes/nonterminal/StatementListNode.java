package jyscript.parsetree.nodes.nonterminal;

import jyscript.parsetree.nodes.ParseNode;

/**
 * I know I know... This is just a List but I want to keep the Structure of the tree, else I would just make a List<Statement> and done.
 */
public class StatementListNode implements ParseNode<Void>{

        private StatementNode m_Statement;
        private StatementListNode m_Rest;

        public StatementListNode(StatementNode node, StatementListNode rest){
            m_Rest = rest;
            m_Statement = node;
        }   

        public StatementListNode(){
            this(null, null);
        }

        public Void eval(){
            if(m_Statement != null){
                m_Statement.eval();
            }
            if(m_Rest != null){
                m_Rest.eval();
            }
            return null;
        }
}