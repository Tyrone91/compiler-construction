package jyscript;

public class JYParser {

    private JYScanner m_Scanner;
    private String m_CurrentToken;

    public JYParser(JYScanner scanner){
        m_Scanner = scanner;
        m_CurrentToken = nextToken();
    }

    private  Object consume(){
        final Object val = this.m_Scanner.tokenValue();
        if(val == null){ // Should not happen if gramma is right.
            error("Missing token value");
        }
        m_CurrentToken = nextToken();
        return val;
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
        final String line = this.m_Scanner.line();
        final String file = this.m_Scanner.file();
        final String msg = String.format("Parser error in %s:%s. %s", file, line, additionalMessage );
        throw new RuntimeException(msg);
    }


}