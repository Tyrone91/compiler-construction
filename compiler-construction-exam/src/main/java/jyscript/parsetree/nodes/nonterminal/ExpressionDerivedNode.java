package jyscript.parsetree.nodes.nonterminal;

import jyscript.JYParser.ValueSetter;

/**
 * This class would be equal to epsilon
 */
public class ExpressionDerivedNode implements ValueSetter<ExpressionDerivedNode>{

    public ExpressionDerivedNode(){

    }

    public int eval(int a){
        return a;
    }
    
    public int simpleEval(int a){
        return a;
    }

    @Override
    public void setValue(ExpressionDerivedNode value) {
        throw new UnsupportedOperationException("This node can't have a value");
    }
    
    public boolean hasNext(){
        return false;
    }
    
    public ExpressionDerivedNode next(){
        return null;
    }
}