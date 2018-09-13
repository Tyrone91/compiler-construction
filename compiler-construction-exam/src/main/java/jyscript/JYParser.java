package jyscript;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import jyscript.parsetree.IdentifierTable;
import jyscript.parsetree.nodes.ParseNode;
import jyscript.parsetree.nodes.nonterminal.AssignmentNode;
import jyscript.parsetree.nodes.nonterminal.DeclareNode;
import jyscript.parsetree.nodes.nonterminal.DivOperationNode;
import jyscript.parsetree.nodes.nonterminal.ExpressionDerivedNode;
import jyscript.parsetree.nodes.nonterminal.ExpressionNode;
import jyscript.parsetree.nodes.nonterminal.FactorNode;
import jyscript.parsetree.nodes.nonterminal.IfNode;
import jyscript.parsetree.nodes.nonterminal.IsNegativNode;
import jyscript.parsetree.nodes.nonterminal.MinusOperatorNode;
import jyscript.parsetree.nodes.nonterminal.MultOperationNode;
import jyscript.parsetree.nodes.nonterminal.PlusOperatorNode;
import jyscript.parsetree.nodes.nonterminal.PrintNode;
import jyscript.parsetree.nodes.nonterminal.StatementListNode;
import jyscript.parsetree.nodes.nonterminal.StatementNode;
import jyscript.parsetree.nodes.nonterminal.ThermeDerivedNode;
import jyscript.parsetree.nodes.nonterminal.ThermeNode;
import jyscript.parsetree.nodes.nonterminal.WhileNode;
import jyscript.parsetree.nodes.terminal.IdentifierNode;
import jyscript.parsetree.nodes.terminal.NumberNode;

public class JYParser {
    
    public static interface ValueSetter<T>{
        public void setValue(T value);
    }
    
    public static enum NonTerminal {FACTOR, THERME, THERME_DERIVED, EXPRESSION, EXPRESSION_DERIVED, DECLARE, ASSIGNMENT, PRINT, IF, WHILE, IS_NEG, STATEMENT}
    
    private static Map<NonTerminal, Set<String> >FIRST_OF_SET = new HashMap<>();

    private static void setFirst(NonTerminal nonTerminal, String ... values){
        FIRST_OF_SET.put(nonTerminal, new HashSet<>(Arrays.asList(values)));
    }
    
    private static void initFirstOf(){
        setFirst(NonTerminal.FACTOR, JYSymbols.NUMBER, JYSymbols.IDENTIFIER, JYSymbols.MINUS, JYSymbols.LPR);
        setFirst(NonTerminal.THERME, JYSymbols.NUMBER, JYSymbols.IDENTIFIER, JYSymbols.MINUS, JYSymbols.LPR);
        setFirst(NonTerminal.THERME_DERIVED, JYSymbols.MULT, JYSymbols.DIV);
        setFirst(NonTerminal.EXPRESSION, JYSymbols.NUMBER, JYSymbols.IDENTIFIER, JYSymbols.MINUS, JYSymbols.LPR);
        setFirst(NonTerminal.EXPRESSION_DERIVED, JYSymbols.PLUS, JYSymbols.MINUS);
        setFirst(NonTerminal.DECLARE, JYSymbols.VAR);
        setFirst(NonTerminal.ASSIGNMENT, JYSymbols.IDENTIFIER);
        setFirst(NonTerminal.PRINT, JYSymbols.PRINT_STMT);
        setFirst(NonTerminal.IF, JYSymbols.IF);
        setFirst(NonTerminal.WHILE, JYSymbols.WHILE);
        setFirst(NonTerminal.IS_NEG, JYSymbols.IS_NEG);
        setFirst(NonTerminal.STATEMENT, JYSymbols.WHILE, JYSymbols.LPC, JYSymbols.VAR, JYSymbols.IDENTIFIER, JYSymbols.PRINT_STMT, JYSymbols.IF);
    }
    

    private static boolean isfirst(String token, NonTerminal nonTerminal){
        return FIRST_OF_SET.get(nonTerminal).contains(token);
    }

    private static String firststr(NonTerminal nonTerminal){
        return FIRST_OF_SET.get(nonTerminal).stream().collect(Collectors.joining(","));
    }

    static {
        initFirstOf();
    }

    private JYScanner m_Scanner;
    private String m_CurrentToken;
    private IdentifierTable m_IdentifierTable;

    public JYParser(JYScanner scanner){
        m_Scanner = scanner;
        m_CurrentToken = nextToken();
        m_IdentifierTable = new IdentifierTable(scanner);
    }
    
    public <T extends ValueSetter<T>> T recursionHelper(NonTerminal nonTerminal, Supplier<T> supplier, Supplier<T> nonFirst){
        T first = null;
        if(isfirstof(nonTerminal)){    		
            first = supplier.get();
        }else{
            return nonFirst.get();
        }
        T current = first;
        while(true){
            if(isfirstof(nonTerminal)){
                T tmp = supplier.get();
                current.setValue(tmp);
                current = tmp;
            } else {
                current.setValue(nonFirst.get());
                return first;
            }
        }
    }

    
    @SuppressWarnings("unchecked")
    private <T> T consume(String expectedToken){
        if(!expectedToken.equals(currentToken())){
            error("Unexpected Token. Expected " + expectedToken + " but got " + currentToken());
        }
        final Object val = this.m_Scanner.tokenValue();
        if(val == null){ // Should not happen if grammar is right.
            error("Tried to consume value for token " + currentToken() + " but failed");
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
            if(tokenIs(JYSymbols.UNKNOWN_TOKEN)){
                error(String.format("'%s' is not a valid character", m_Scanner.tokenValue()));
            } else {        		
                error("unexpected symbol. Expected " + token + " but was " + read);
            }
        }
        m_CurrentToken = nextToken();
    }

    private void error(String additionalMessage){
        final String line = String.valueOf(m_Scanner.line());
        final String file = this.m_Scanner.file();
        final String msg = String.format("Parser error in %s:%s. %s", file, line, additionalMessage );
        throw new RuntimeException(msg);
    }
    
    private void unexpectedTokenFor(NonTerminal ecpected) {
        if(tokenIs(JYSymbols.UNKNOWN_TOKEN)){
            error(String.format("'%s' is not a valid character", m_Scanner.tokenValue()));
        }else{    		
            error("Expected tokens: " + firststr(ecpected) + " but got " + currentToken());
        }
    }

    private boolean tokenIs(String match){
        return currentToken().equals(match);
    }

    private boolean isfirstof(NonTerminal nonTerminal){
        return JYParser.isfirst(currentToken(), nonTerminal);
    }
    
    public IdentifierTable getIdentifierTable(){
        return m_IdentifierTable;
    }

    // ------------- actions ---------

    public ParseNode<Void> parse(){
        StatementListNode res = statementlist();
        match(JYSymbols.EOF);
        return res;
    }

    public StatementListNode statementlist(){
        return statementlist_derived();
    }

    public StatementListNode statementlist_derived(){
        return recursionHelper(NonTerminal.STATEMENT, () -> {
            if(isfirstof(NonTerminal.STATEMENT)){
                return new StatementListNode(statement(), null);
            }
            unexpectedTokenFor(NonTerminal.STATEMENT);
            return null;
        }, StatementListNode::new);
    }

    public StatementNode statement(){
        if(tokenIs(JYSymbols.WHILE)){
            return new StatementNode(while_stmt());
        } else if(tokenIs(JYSymbols.LPC)){
            match(JYSymbols.LPC);
            StatementListNode res = statementlist();
            match(JYSymbols.RPC);
            return new StatementNode(res);

        } else if(tokenIs(JYSymbols.IF)){
            return new StatementNode(if_stmt());
        } else if(isfirstof(NonTerminal.DECLARE)){
            return new StatementNode(declare());

        } else if(isfirstof(NonTerminal.ASSIGNMENT)){
            return new StatementNode(assignment());

        } else if(isfirstof(NonTerminal.PRINT)){
            return new StatementNode(print());
            
        }else{
            unexpectedTokenFor(NonTerminal.STATEMENT);
            return null;
        }
    }

    public WhileNode while_stmt(){
        match(JYSymbols.WHILE);
        match(JYSymbols.LPR);
        ExpressionNode expression = expression();
        match(JYSymbols.RPR);
        StatementNode stmt = statement();
        return new WhileNode(expression, stmt);
    }

    public IfNode if_stmt(){
        match(JYSymbols.IF);

        match(JYSymbols.LPR);
        ExpressionNode expression = expression();
        match(JYSymbols.RPR);

        StatementNode thenNode = statement();
        match(JYSymbols.ELSE);
        StatementNode elseNode = statement();

        return new IfNode(expression, thenNode, elseNode);
    }

    public DeclareNode declare(){
        if(tokenIs(JYSymbols.VAR)){
            match(JYSymbols.VAR);
            DeclareNode res = declare_derived( consume(JYSymbols.IDENTIFIER) );
            match(JYSymbols.SEMICOLON);
            return res;
        }else{
            unexpectedTokenFor(NonTerminal.DECLARE);
            return null;
        }
    }

    public DeclareNode declare_derived(String identiferName){
        AssignmentNode a = null;
        m_IdentifierTable.declareIdentifier(identiferName); //runtime vs compiler check see DeclareNode.eval()
        if( tokenIs(JYSymbols.ASSIGN)){
            match(JYSymbols.ASSIGN);
            a = new AssignmentNode(m_IdentifierTable, identiferName, expression());
        }
        return new DeclareNode(m_IdentifierTable, identiferName, a);
    }

    public PrintNode print(){
        match(JYSymbols.PRINT_STMT);
        PrintNode n = new PrintNode(expression());
        match(JYSymbols.SEMICOLON);
        return n;
    }

    public AssignmentNode assignment(){
        if(tokenIs(JYSymbols.IDENTIFIER)){
            String identifier = consume(JYSymbols.IDENTIFIER);
            match(JYSymbols.ASSIGN);
            AssignmentNode res = new AssignmentNode(m_IdentifierTable, identifier, expression());
            match(JYSymbols.SEMICOLON);
            return res;
        }else{
            unexpectedTokenFor(NonTerminal.ASSIGNMENT);
            return null;
        }
    }

    public ExpressionNode expression(){
        return new ExpressionNode(therme(), expression_derived() );
    }
    
    protected ExpressionDerivedNode expression_derived(){
        return recursionHelper(NonTerminal.EXPRESSION_DERIVED, () -> {
            if(tokenIs(JYSymbols.PLUS)){
                match(JYSymbols.PLUS);
                return new PlusOperatorNode(therme(), null);

            }else if(tokenIs(JYSymbols.MINUS)){

                match(JYSymbols.MINUS);
                return new MinusOperatorNode(therme(), null);
            }
            unexpectedTokenFor(NonTerminal.EXPRESSION_DERIVED);
            return null;
        }, ExpressionDerivedNode::new);
    }

    protected ThermeNode therme(){
        return new ThermeNode(factor(), therme_derived());
    }

    protected ThermeDerivedNode therme_derived(){
        return recursionHelper(NonTerminal.THERME_DERIVED, () -> {
            if(tokenIs(JYSymbols.MULT)){

                match(JYSymbols.MULT);
                return new MultOperationNode(factor(), null);
            }else if(tokenIs(JYSymbols.DIV)){

                match(JYSymbols.DIV);
                return new DivOperationNode(factor(), null);
            }
            unexpectedTokenFor(NonTerminal.THERME_DERIVED);
            return null;
        }, ThermeDerivedNode::new);
        
    }

    protected IsNegativNode isNeg(){
       match(JYSymbols.IS_NEG); 
       match(JYSymbols.LPR);
       ExpressionNode n = expression();
       match(JYSymbols.RPR);
       return new IsNegativNode(n);
    }

    protected FactorNode factor(){
        boolean uniaryMinus = false;
        while(tokenIs(JYSymbols.MINUS)){
            match(JYSymbols.MINUS);
            uniaryMinus = !uniaryMinus;
        }
        if(tokenIs(JYSymbols.NUMBER)){
            return new FactorNode(new NumberNode( consume(JYSymbols.NUMBER) ), uniaryMinus);

        }else if(tokenIs(JYSymbols.IDENTIFIER)){
            return new FactorNode( new IdentifierNode(m_IdentifierTable, consume(JYSymbols.IDENTIFIER)), uniaryMinus);

        }else if(tokenIs(JYSymbols.LPR)){
            match(JYSymbols.LPR);
            FactorNode res =  new FactorNode(expression(), uniaryMinus);
            match(JYSymbols.RPR);
            return res;
            
        }else if(tokenIs(JYSymbols.IS_NEG)) {
            return new FactorNode(isNeg(), uniaryMinus);
        }else {
            unexpectedTokenFor(NonTerminal.FACTOR);
            return null;
        }
    }


}