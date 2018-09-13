package jyscript.parsetree.nodes.nonterminal;

import jyscript.JYParser.ValueSetter;

public class ThermeDerivedNode implements ValueSetter<ThermeDerivedNode>{


    public ThermeDerivedNode(){
    }

    public int eval(int a){
        return a;
    }
    
    public int simpleEval(int a){
        return a;
    }

    @Override
    public void setValue(ThermeDerivedNode value) {
        throw new UnsupportedOperationException("Can't set value on this node");
    }
    
    public ThermeDerivedNode next(){
        return null;
    }
}