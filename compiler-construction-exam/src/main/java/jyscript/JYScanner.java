package jyscript;

public interface JYScanner {

    public String nextToken();
    public Object tokenValue();

    public int line();
    public String file();
}