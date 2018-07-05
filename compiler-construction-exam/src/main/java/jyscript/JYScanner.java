package jyscript;

public interface JYScanner {

    public String nextToken();
    public Object tokenValue();

    public String line();
    public String file();
}