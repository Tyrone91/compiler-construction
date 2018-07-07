package jyscript;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import jyscript.parsetree.ParseTree;
import jyscript.parsetree.nodes.nonterminal.DivOperationNode;
import jyscript.parsetree.nodes.nonterminal.ExpressionDerivedNode;
import jyscript.parsetree.nodes.nonterminal.ExpressionNode;
import jyscript.parsetree.nodes.nonterminal.FactorNode;
import jyscript.parsetree.nodes.nonterminal.MinusOperatorNode;
import jyscript.parsetree.nodes.nonterminal.MultOperationNode;
import jyscript.parsetree.nodes.nonterminal.PlusOperatorNode;
import jyscript.parsetree.nodes.nonterminal.ThermeDerivedNode;
import jyscript.parsetree.nodes.nonterminal.ThermeNode;
import jyscript.parsetree.nodes.terminal.IdentifierNode;
import jyscript.parsetree.nodes.terminal.NumberNode;

public class JYParser {

    

    private static Map<String, Set<String> >FIRST_OF_SET = new HashMap<>();

    private static void setFirst(String terminal, String ... values){
        FIRST_OF_SET.put(terminal, new HashSet<>(Arrays.asList(values)));
    }

    private static void initFirstOf(){
        setFirst("FACTOR", JYSymbols.NUMBER, JYSymbols.IDENTIFIER, JYSymbols.MINUS, JYSymbols.LPR);
        setFirst("THERME", JYSymbols.NUMBER, JYSymbols.IDENTIFIER, JYSymbols.MINUS, JYSymbols.LPR);
        setFirst("THERME_DERIVED", JYSymbols.MULT, JYSymbols.DIV);
        setFirst("EXPRESSION", JYSymbols.NUMBER, JYSymbols.IDENTIFIER, JYSymbols.MINUS, JYSymbols.LPR);
        setFirst("EXPRESSION_DERIVED", JYSymbols.PLUS, JYSymbols.MINUS);
    }

    private static boolean isfirst(String token, String terminal){
        return FIRST_OF_SET.get(terminal).contains(token);
    }

    private static String firststr(String terminal){
        return FIRST_OF_SET.get(terminal).stream().collect(Collectors.joining(","));
    }

    static {
        initFirstOf();
    }

    private JYScanner m_Scanner;
    private String m_CurrentToken;
    private ParseTree m_ParseTree;

    public JYParser(JYScanner scanner){
        m_Scanner = scanner;
        m_CurrentToken = nextToken();
        m_ParseTree = new ParseTree();
    }

    private <T>  T consume(){
        final Object val = this.m_Scanner.tokenValue();
        if(val == null){ // Should not happen if gramma is right.
            error("Missing token value");
        }
        m_CurrentToken = nextToken();
        return (T)val; //ignore unchecked cast. IF wrong type let the exception happen
    }

    private String nextToken(){
        final String token = m_Scanner.nextToken();
        if(token == null){
            error("Unexpected file end");
        }
        return token;
    }

    private String currentToken(){
        return m_CurrentToken;
    }

    private void match(String token){
        String read = currentToken();
        if(!token.equals(read)){
            error("unexpected symbol. Expected " + token + " but was " + read);
        }
        m_CurrentToken = nextToken();
    }

    private void error(){
        error("");
    }

    private void error(String additionalMessage){
        final String line = String.valueOf(m_Scanner.line());
        final String file = this.m_Scanner.file();
        final String msg = String.format("Parser error in %s:%s. %s", file, line, additionalMessage );
        throw new RuntimeException(msg);
    }

    private boolean tokenIs(String match){
        return currentToken().equals(match);
    }

    // ------------- actions ---------

    public ExpressionNode expression(){
        return new ExpressionNode(therme(), expression_derived() );
    }

    protected ExpressionDerivedNode expression_derived(){
        if(tokenIs(JYSymbols.PLUS)){
        	match(JYSymbols.PLUS);
            return new PlusOperatorNode(therme(), expression_derived()); //TODO: remove recursion

        }else if(tokenIs(JYSymbols.MINUS)){

            match(JYSymbols.MINUS);
            return new MinusOperatorNode(therme(), expression_derived()); //TODO: remove recursion
        }
        return new ExpressionDerivedNode();
    }

    protected ThermeNode therme(){
        return new ThermeNode(factor(), therme_derived());
    }

    protected ThermeDerivedNode therme_derived(){
        if(tokenIs(JYSymbols.MULT)){

            match(JYSymbols.MULT);
            return new MultOperationNode(factor(), therme_derived()); //TODO: remove recursion

        }else if(tokenIs(JYSymbols.DIV)){

            match(JYSymbols.DIV);
            return new DivOperationNode(factor(), therme_derived()); //TODO: remove recursion

        }
        return new ThermeDerivedNode();
    }

    protected FactorNode factor(){
        if(tokenIs(JYSymbols.NUMBER)){
            return new FactorNode(new NumberNode( consume() ));

        }else if(tokenIs(JYSymbols.IDENTIFIER)){
            return new FactorNode( new IdentifierNode(m_ParseTree, consume()));

        }else if(tokenIs(JYSymbols.LPR)){
            match(JYSymbols.LPR);
            System.out.println("Expression read");
            FactorNode res =  new FactorNode(expression());
            match(JYSymbols.RPR);
            return res;

        }else if(tokenIs(JYSymbols.MINUS)){
            match(JYSymbols.MINUS);
            return new FactorNode(factor(), true); //TODO: remove recursion

        }else{
            error("expected on of " + firststr("FACTOR") + " but was " + currentToken());
        }
        return null;
    }


}