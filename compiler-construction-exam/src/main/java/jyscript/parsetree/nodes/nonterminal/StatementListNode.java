package jyscript.parsetree.nodes.nonterminal;

import jyscript.JYParser.ValueSetter;
import jyscript.parsetree.nodes.ParseNode;

/**
 * I know I know... This is just a List but I want to keep the Structure of the tree, else I would just make a List<Statement> and done.
 */
public class StatementListNode implements ParseNode<Void>, ValueSetter<StatementListNode>{

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
            StatementListNode current = m_Rest;
            while(current != null){
                if(current.m_Statement != null){
                    current.m_Statement.eval();
                }
                current = current.m_Rest;
            }
            return null;
        }

        @Override
        public void setValue(StatementListNode value) {
            m_Rest = value;
        }
}