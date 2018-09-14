package jyscript;

import java.io.IOException;
import java.io.Reader;

/**
 * Adapter call the {@link JYScanner}.
 * It will delegate the methods from {@link JYScanner} to the right methods from
 * the generated JFlex Scanner.
 */
public class JYScannerAdapter implements JYScanner {

    private JFlexJYScriptScanner m_Scanner;
    private String m_File;
    private JFlexJYScriptScanner.Token m_Current;

    public JYScannerAdapter(Reader reader, String file){
        m_Scanner = new JFlexJYScriptScanner(reader);
        m_File = file;
    }

    @Override
    public String nextToken(){
        try{
            m_Current = m_Scanner.yylex();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return m_Current.token;
    }
    
    /**
     * Returns the value of the current token.
     * Not each token has a value, it is job of the parser to handle that.
     */
    @Override
    public Object tokenValue(){
        return m_Current.value;
    }

    @Override
    public int line(){
        return m_Current.line + 1; // Jflex line counting seems to be zero based. 
    }
    @Override
    public String file(){
        return m_File;
    }


}