package jyscript;

import java.io.FileNotFoundException;
import java.io.FileReader;


public class Main {

    public static void debugFile() throws FileNotFoundException{
        JYScannerAdapter adapter = new JYScannerAdapter( new FileReader("../text/input.jy"), "input.jy");
        JYParser parser = new JYParser(adapter);
        System.out.println(parser.parse().eval());
    }
    
    public static void examFile() throws FileNotFoundException{
    	JYScannerAdapter adapter = new JYScannerAdapter( new FileReader("../text/exam.jy"), "input.jy");
        JYParser parser = new JYParser(adapter);
        parser.parse().eval();
    }

    public static void main( String[] args ) throws FileNotFoundException {
        System.out.println( "Hello World!" );
        //debugFile();
        examFile();
    }

}
