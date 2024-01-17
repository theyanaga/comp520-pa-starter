package miniJava;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Compiler {
	// Main function, the file to compile will be an argument.
	public static void main(String[] args) throws IOException {
		// TODO: Instantiate the ErrorReporter object
		ErrorReporter errorReporter = new ErrorReporter();

		InputStream in = new FileInputStream(args[0]);

		int val = in.read();
		while(val != -1) {
			char c = (char) val;
//			System.out.println("The current int is " + val);
			System.out.println("The current char is: "  + c);
			val = in.read();
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
