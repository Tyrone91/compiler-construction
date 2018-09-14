package jyscript.parsetree;

/**
 * Representation of an identifier in the code.
 * If includes the name, value and the first appearance in the code.
 */
public class Identifier {

    public int m_Value;
    public String m_Name;
    public Source m_FirstAppearance;

    public Identifier(String name, int value, String file, int line){
        this(name, value);
        m_FirstAppearance = new Source(file, line);
    }

    public Identifier(String name, int value){
        m_FirstAppearance = new Source();
        m_Name = name;
        m_Value = value;
    }

    public Source fistAppearance(){
        return m_FirstAppearance;
    }

    public int value(){
        return m_Value;
    }

    public void value(int newvalue){
        m_Value = newvalue;
    }

    public String name(){
        return m_Name;
    }

    @Override
    public String toString(){
        return String.format("var %s = %s. First in %s", m_Name, String.valueOf(m_Value), m_FirstAppearance.toString());
    }
}