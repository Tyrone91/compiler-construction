package jyscript;

import java.io.FileNotFoundException;
import java.io.FileReader;

import jyscript.parsetree.nodes.ParseNode;


public class Main {

    public static void debugFile() throws FileNotFoundException{
    	parseFile("../text/input.jy");
    }
    
    public static void examFile() throws FileNotFoundException{
        parseFile("../text/exam.jy");
    }
    
    public static void parseFile(String filename) {
    	try{
    		JYScannerAdapter adapter = new JYScannerAdapter( new FileReader(filename), filename);
            JYParser parser = new JYParser(adapter);
            System.out.println("-------------------starting parsing");
            ParseNode<Void> root = parser.parse();
            System.out.println("-------------------finished parsing");
            System.out.println("-------------------starting evaluation");
            root.eval();
            System.out.println("-------------------finished evaluation");
            System.out.println("-------------------printing all identifiers");
            parser.getIdentifierTable().printAllIdentifiers();
            System.out.println("-------------------programm finished.");
    	} catch(FileNotFoundException ex) {
    		System.out.println("couldn't find '" + filename + "'");
    		System.exit(0);
    	}
    }

    public static void main( String[] args ) throws FileNotFoundException {
    	if(args.length != 0){
    		System.out.println("Reading code from: '" + args[0] + "'.");
    		parseFile(args[0]);
    	} else {
    		System.out.println("No file given. Reading default.");
    		debugFile();
            //examFile();
    	}
    }

}
