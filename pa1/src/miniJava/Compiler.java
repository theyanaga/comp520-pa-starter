package miniJava;

import miniJava.SyntacticAnalyzer.Parser;
import miniJava.SyntacticAnalyzer.Scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Compiler {

    // Main function, the file to compile will be an argument.
    public static void main(String[] args) throws IOException {
        // TODO: Instantiate the ErrorReporter object
        ErrorReporter errorReporter = new ErrorReporter();
        if (args.length != 1) {
            throw new RuntimeException("Please provide a file to compile!");
        }
        InputStream in = new FileInputStream(args[0]);
        Scanner scanner = new Scanner(in, errorReporter);
        Parser parser = new Parser(scanner, errorReporter);
        parser.parse();
        if (errorReporter.hasErrors()) {
            System.out.println("Error");
            errorReporter.outputErrors();
        } else {
            System.out.println("Success");
        }
        // TODO: Check to make sure a file path is given in args
        // TODO: Create the inputStream using new FileInputStream
        // TODO: Instantiate the scanner with the input stream and error object
        // TODO: Instantiate the parser with the scanner and error object
        // TODO: Call the parser's parse function
        // TODO: Check if any errors exist, if so, println("Error")
        //  then output the errors
        // TODO: If there are no errors, println("Success")
    }
}
