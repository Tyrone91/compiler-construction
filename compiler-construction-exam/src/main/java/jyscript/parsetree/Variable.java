package jyscript.parsetree;

public class Variable {

    public int m_Value;
    public String m_Name;
    public Source m_FirstAppearance;

    public Variable(String name, int value, String file, int line){
        this(name, value);
        m_FirstAppearance = new Source(file, line);
    }

    public Variable(String name, int value){
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

    public String name(){
        return m_Name;
    }

    public String toString(){
        return String.format("var %s = %s. First in %s", m_Name, String.valueOf(m_Value), m_FirstAppearance.toString());
    }
}