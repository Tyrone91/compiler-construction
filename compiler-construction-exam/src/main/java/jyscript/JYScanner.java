package jyscript;

/**
 * Interface that is needed for the {@link JYParser}.
 */
public interface JYScanner {

    public String nextToken();
    public Object tokenValue();

    public int line();
    public String file();
}