package jyscript.parsetree.nodes.terminal;

import jyscript.parsetree.nodes.IntNode;

public class NumberNode extends IntNode{

    private int m_Value;

    public NumberNode(int value){
        m_Value = value;
    }

    public Integer eval(){
        return m_Value;
    }
}