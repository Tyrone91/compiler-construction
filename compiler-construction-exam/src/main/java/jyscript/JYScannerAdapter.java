package jyscript;

import java.io.IOException;
import java.io.Reader;


public class JYScannerAdapter implements JYScanner {

    private JFlexJYScriptScanner m_Scanner;
    private String m_File;
    private JFlexJYScriptScanner.Token m_Current;

    public JYScannerAdapter(Reader reader, String file){
        m_Scanner = new JFlexJYScriptScanner(reader);
        m_File = file;
    }

    public String nextToken(){
        try{
            m_Current = m_Scanner.yylex();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return m_Current.token;
    }
    public Object tokenValue(){
        return m_Current.value;
    }

    public int line(){
        return m_Current.line + 1;
    }
    public String file(){
        return m_File;
    }


}