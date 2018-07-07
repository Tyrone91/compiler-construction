package jyscript;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import jyscript.parsetree.nodes.IntNode;

public class Main {

    public static void debugFile() throws FileNotFoundException{
        JYScannerAdapter adapter = new JYScannerAdapter( new FileReader("../text/input.jy"), "input.jy");
        JYParser parser = new JYParser(adapter);
        System.out.println(parser.expression().eval());
    }

    public static void main( String[] args ) throws FileNotFoundException {
        System.out.println( "Hello World!" );
        debugFile();
    }

}
