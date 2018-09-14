package jyscript.parsetree;

/**
 * Helper class to hold information of the first appearance of an {@link Identifier}.
 */
public class Source {

    private String m_FileName;
    private int m_Line;

    public Source(String file, int line){
        m_FileName = file;
        m_Line = line;
    }

    public Source(){
        this("UNKNOWN_FILE", -1);
    }

    @Override
    public String toString(){
        return m_FileName + ":" + m_Line;
    }

    public int line(){
        return m_Line;
    }

    public String file(){
        return m_FileName;
    }
}