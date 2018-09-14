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

/**
 * Main class for parsing.
 */
public class JYParser {
    
    /**
     * Interface that must be implemented to be used by {@link #recursionHelper}
     * @param <T>
     */
    public static interface ValueSetter<T>{
        public void setValue(T value);
    }
    
    public static enum NonTerminal {FACTOR, THERME, THERME_DERIVED, EXPRESSION, EXPRESSION_DERIVED, DECLARE, ASSIGNMENT, PRINT, IF, WHILE, IS_NEG, STATEMENT}
    
    private static Map<NonTerminal, Set<String> >FIRST_OF_SET = new HashMap<>();
    
    /**
     * Helper function to initialize the map for the first set entries.
     * @param nonTerminal
     * @param values
     */
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
    
    /**
     * Returns true if the given token is in the first set of the non-terminal symbol.
     * @param token
     * @param nonTerminal
     * @return
     */
    private static boolean isfirst(String token, NonTerminal nonTerminal){
        return FIRST_OF_SET.get(nonTerminal).contains(token);
    }
    
    /**
     * Returns a string that concatenates all possible following token for the given non terminal symbol.  
     * @param nonTerminal
     * @return
     */
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
    
    /**
     * Helper function to remove end recursion.
     * The function will continue as long the same non-terminal symbol is read and will pass the new created value to the former created one by using the {@link ValueSetter}.
     * The function returns the first created element that will contain the 2nd and so forth.
     * @param nonTerminal
     * @param supplier
     * @param nonFirst
     * @return
     */
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

    /**
     * Tries to read the value of the current token and returns it.
     * If successful the function will read the next token.
     * If the token does not match the expected token an error will be thrown.
     * @param expectedToken
     * @return
     */
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

    /**
     * Reads the next token.
     * @return
     */
    private String nextToken(){
        final String token = m_Scanner.nextToken();
        if(token == null){
            error("Unexpected file end");
        }
        return token;
    }
    
    /**
     * Returns the current token.
     * @return
     */
    private String currentToken(){
        return m_CurrentToken;
    }
    
    /**
     * Tries to match the current token with the expected one.
     * If successful the next token is read, else an error is thrown.
     * @param token
     */
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
    
    /**
     * Helper function to create a unified error message.
     * @param additionalMessage
     */
    private void error(String additionalMessage){
        final String line = String.valueOf(m_Scanner.line());
        final String file = this.m_Scanner.file();
        final String msg = String.format("Parser error in %s:%s. %s", file, line, additionalMessage );
        throw new RuntimeException(msg);
    }
    
    /**
     * Pre-defined error message for the case that the given token it not allowed.
     * @param ecpected
     */
    private void unexpectedTokenFor(NonTerminal ecpected) {
        if(tokenIs(JYSymbols.UNKNOWN_TOKEN)){
            error(String.format("'%s' is not a valid character", m_Scanner.tokenValue()));
        }else{    		
            error("Expected tokens: " + firststr(ecpected) + " but got " + currentToken());
        }
    }
    
    /**
     * Returns true if the given current token matches the expected one.
     * @param match
     * @return
     */
    private boolean tokenIs(String match){
        return currentToken().equals(match);
    }
    
    /**
     * Returns true if the current token is in the first set of the given non terminal symbol.
     * @param nonTerminal
     * @return
     */
    private boolean isfirstof(NonTerminal nonTerminal){
        return JYParser.isfirst(currentToken(), nonTerminal);
    }
    
    /**
     * Returns a reference to the used {@link IdentifierTable}.
     * @return
     */
    public IdentifierTable getIdentifierTable(){
        return m_IdentifierTable;
    }

    // ------------- actions ---------
    
    /**
     * Tries to parse the data from the {@link JYScanner}.
     * If successful the root of the parse tree is returned, otherwise an error is thrown.
     * @return
     */
    public ParseNode<Void> parse(){
        StatementListNode res = statementlist();
        match(JYSymbols.EOF);
        return res;
    }
    
    // The following code is the core of the parser and matches the specified grammar for the language.
    // Each method represents a non-terminal symbol in the language and returns the corresponding node object for the symbol.
    // if a token does not match the expected token in the method an error will be thrown with a detailed error messages for the user.
    // The comments for all method would be same therefore will comment exists.
    
    
    public StatementListNode statementlist(){
        return statementlist_derived();
    }

    public StatementListNode statementlist_derived(){
        return recursionHelper(NonTerminal.STATEMENT, () -> { // collect all following statements. Effectively building a simple linked list.
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
        return recursionHelper(NonTerminal.EXPRESSION_DERIVED, () -> { // collect all following expressions. Effectively building a simple linked list.
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
        return recursionHelper(NonTerminal.THERME_DERIVED, () -> { // collect all following thermes. Effectively building a simple linked list.
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
        boolean unaryMinus = false;
        while(tokenIs(JYSymbols.MINUS)){ // consume all unary minuses and only use the last relevant.
            match(JYSymbols.MINUS);
            unaryMinus = !unaryMinus;
        }
        if(tokenIs(JYSymbols.NUMBER)){
            return new FactorNode(new NumberNode( consume(JYSymbols.NUMBER) ), unaryMinus);

        }else if(tokenIs(JYSymbols.IDENTIFIER)){
            return new FactorNode( new IdentifierNode(m_IdentifierTable, consume(JYSymbols.IDENTIFIER)), unaryMinus);

        }else if(tokenIs(JYSymbols.LPR)){
            match(JYSymbols.LPR);
            FactorNode res =  new FactorNode(expression(), unaryMinus);
            match(JYSymbols.RPR);
            return res;
            
        }else if(tokenIs(JYSymbols.IS_NEG)) {
            return new FactorNode(isNeg(), unaryMinus);
        }else {
            unexpectedTokenFor(NonTerminal.FACTOR);
            return null;
        }
    }
}