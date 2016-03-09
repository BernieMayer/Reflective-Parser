package methods;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class CmdLine {
	
	
	
	
	
	
	
	
	/**
	 *  This method will display the users options when running this compiler.
	 */
	public static void startMessage() {
		System.out.println("q \t : Quit the program.\n"
				+ "v \t : Toggle verbose mode (stack traces).\n"
				+ "f \t : List all known functions.\n"
				+ "? \t : Print this helpful text.\n"
				+ "<expression>: Evaluate the expression."
				+ "Expressions can be integers, floats, strings (surrounded in double quotes) or function\n"
				+ "calls of the form '(identifier {expression}*)'");
	} 
    
	
	
	/**
	 * This method will display the functions that the user may use in the command line
	 */
	public static void functionList() {
		System.out.println("Function List:\n"
				+ "(add string string) : string\n"
				+ "(add float float) : float\n"
				+ "(add int int) : int\n"
				+ "(sub float float) : float\n"
				+ "(sub int int) : int\n"
				+ "(div int int) : int\n"
				+ "(div float float) : float\n"
				+ "(mul float float) : float\n"
				+ "(mul int int) : int\n"
				+ "(inc float) : float\n"
				+ "(inc int) : int\n"
				+ "(dec int) : int\n"
				+ "(dec float) : float\n"
				+ "(len string) : int\n");
	}
	
	
	/**
	 * Prints the synopsis of what the program can do and what commands it may take.
	 */
	public static void printSummary(){
		System.out.println("Synopsis:\n  methods\n  methods { -h | -? | --help }+\n  methods {-v --verbose}* <jar-file> [<class-name>]");
		System.out.println("Arguments:\n  <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]");
		System.out.println("Qualifiers:\n  -v --verbose: Print out detailed errors, warnings, and tracking.\n  -h -? --help: Print out a detailed help message.");
		System.out.println("Single-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.");
	}
	
	
	
	/**
	 * This is the primary part of the compiler after correct commands have been called from the class. 
	 * This will loop infinitely until key 'q' has been pressed and it quits the function
	 * 
	 */
	public static void mainMenu() {
		Scanner keyboard = new Scanner(System.in);
		boolean keepRunningParser = true;
		//if verbose is on, track code
		if (Debug.isVerbose) {
			System.out.println("<<< mainMenu() >>>");
		}
		
		startMessage();	//runs the startup message
		while (keepRunningParser == true) {
			System.out.print("> ");
			String userIn = keyboard.nextLine();
			if (userIn.equals("q")) {			//quit the program if user input is 'q'
				keepRunningParser = false;
			} else if (userIn.equals("v")) {	//If user input = 'v', we will toggle verbose mode
				if (Debug.isVerbose() == false) {	// check to see if verbose mode is off
					Debug.setIsVerbose(true);		//if verbose mode is off, we toggle it and turn it on
				} else {
					Debug.setIsVerbose(false);			//otherwise just turn it off
				}
			} else if (userIn.equals("f")) {		//If the user inputs 'f', we display the function list
				functionList();
			} else {
				boolean validBrackets = checkBrackets(userIn); //first checks the brackets on the input
				boolean validInput = invalidFunc(userIn); //then checks if the input is valid at all
				if (validBrackets == false) {
					System.out.println("Matching braces error.");
				} else if (validInput == false) {
					System.out.println("Input to be calculated is invalid.");
				} //else { 								//if both tests pass, then input will be processed through recursion tree
					//finalAnswer = this.treeTime(userInput);
					//if (finalAnswer != null) {
						//System.out.println("finalAnswer");
						//Errors.setInput("userInput"); //gets input from user for error handling
					//}
				}
			}
		}
		
		
		
		/**
		 * This method checks to see if the input that a user entered is valid
		 * This function will check many things, such as the balance of brackets
		 * or if the user has used too many brackets
		 * Eg. ((2+2)) is valid, but it has too many brackets and will return true
		 * @param userIn A string that the user inputed via the command line
		 * @return true if invalid, false if valid
		 */
	public static boolean invalidFunc(String userIn){
		//if verbose is on, track code
		if (Debug.isVerbose) {
			System.out.println("<<< invalidFunc() >>>");
		}
		String[] functionList = {"add", "sub", "mul", "div", "len", "dec"}; // List of strings containing out function names
		char[] userInAsArray = userIn.toCharArray(); //Convert userInput string into a char list to check bracket validity
		// Alphabet pattern to be matched against for condition check downstream (is a part of the input illegal after a left parenthesis?)
		char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			
		// Simple case: check if one function call is balanced and has proper syntax
		if (checkBrackets(userIn) == true) /*&& (tree.funcall(userIn))*/ {
			return false;
		} 
			
		// Negative definition of a function call. If a function name from fList is not found at all, immediately return false
		for (String f : functionList) {
			if (!userIn.contains(f)) {
				return true;
				// Check if a number or alphabetical character comes after a left parenthesis and break the program
			} 
			else{
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					for (int j = 0; j < userInAsArray.length; j++ ) {
						if (userInAsArray[j] == '(' && (userInAsArray[j+1] == i || userInAsArray[j+1] == alphabet[j])) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	

	/**
	 * The purpose of this function is to ensure that there is a valid/even number of brackets
	 * in the calculation that the user has specified. 
	 * <p>
	 * This function will also check to see whether or not a right brace will exist before
	 * a left brace. If so, then a false value will return. Otherwise it should return true.
	 * 
	 * @param userIn the calculation that the user has entered as input
	 * @return The boolean value that will inform whether or not the brackets check is valid or not.
	 */
	public static boolean checkBrackets (String userIn) {
		//if verbose is on, track code
		if (Debug.isVerbose) {
			System.out.println("<<< checkBrackets() >>>");
		}
		
		int balancedBraces = 0;
		char[] userIn_charArray = userIn.toCharArray();
		
		//for every ( add 1, for every ) subtract one. If it ever goes below 0, break and return false.
		for (int i = 0; i < userIn_charArray.length; i++){
			if (userIn_charArray[i] == '(') {
				balancedBraces++;
			} else if (userIn_charArray[i] == ')') {
				balancedBraces--;
				if (balancedBraces < 0) {
					return false;
				}
			}
		}
		if (balancedBraces != 0) {
			return false; //if by the end the variable is NOT 0, return false.
		}
		return true;
	}

	
	/**
	 * 
	 * @param args - System arguments given in command line
	 */
	
	public static void argCheck(String[] args){
		LoadedJar loader = new LoadedJar();
		
		//This block of code is embedded in a try catch to keep track of non-fatal errors
		try {
			char[] inputAsArray = null;

			if (args.length == 0) {
				printSummary();
			}
			else if ((args[0].equals("-h") || args[0].equals("-?") || args[0].equals("--help")) && (args.length == 1)) {
				printSummary();
				System.out.println("\nThis program interprets commands of the format '(<method> {arg}*}' on the command line, finds corresponding methods in <class-name>, and executes them, printing the result to sysout. Terminate with ^D or \"exit\"");
			} 
			else if ((args[0].equals("-h") || args[0].equals("-?") || args[0].equals("--help")) && (args.length != 1)) {
				System.out.println("Qualifier --help (-h, -?) should not appear with any command-line arguments.");
				printSummary();
			} 
			else if (((args[0].equals("-v") || args[0].equals("--verbose")) && args[1].contains(".jar") && args.length == 3)
					|| (args[0].equals("-v") || args[0].equals("--verbose")) && args[1].contains(".jar") && args.length == 2){
				Debug.setIsVerbose(true); //set verbose to true if first argument is -v or --verbose, and check if a jar file is in the arguments.
				System.out.println("Running in verbose mode...");		
				if (jarValid(args[1])) { // Check if jar file is valid
					if (args.length == 2) {  // Check if defaulted "Commands" class is in the jar file
						if (isClassValid(args[1], "Commands")) {	//check if class has methods
							loader.loadJarClass(args[1], "Commands"); //load jar class with commands as argument
							mainMenu();
						} 
						else {
							System.out.println("Could not find class: Commands");
						}
					} 
					else {
						if (isClassValid(args[1], args[2])) { // Check if class argument is in the jar file
							loader.loadJarClass(args[1], args[2]);
							mainMenu();
						} 
						else {
							System.out.println("Could not find class: " + args[2]);
						}
					}
				} 
				else {
					System.out.println("Could not find jar file: " + args[1]);
				}
			//if calls for merely the jar file
			} 
			else if ((args[0].contains(".jar") && args.length == 2) || (args[0].contains(".jar") && args.length == 1)) {
				Debug.setIsVerbose(false);
				if (jarValid(args[0])) { // Check if jar file is valid
					if (args.length == 1) { // Check if defaulted "Commands" class is in the jar file
						if (isClassValid(args[0], "Commands")) {
							loader.loadJarClass(args[0], "Commands");
							mainMenu();
						} 
						else {
							System.out.println("Could not find class: Commands");
						}
					} 
					else {
						if (isClassValid(args[0], args[1])) { // Check if class argument is in the jar file
							loader.loadJarClass(args[0], args[1]);
							mainMenu();
						} 
						else {
							System.out.println("Could not find class: " + args[1]);
						}
					}
				} 
				else {
					System.out.println("Could not find jar file: " + args[0]);
				}
			//default output if none of the above apply
			} 
			else {
				inputAsArray = args[0].toCharArray();
				if (Character.valueOf(inputAsArray[0]) == '-') {
					System.out.println("Unrecognized qualifier '" + inputAsArray[1] + "' in '" + args[0] + "'");
					printSummary();
				} 
				else {
					System.out.println("Unrecognized qualifier '" + inputAsArray[0] + "' in '" + args[0] + "'");
					printSummary();
				}
			}
		} 
		catch (Throwable e1){
			//If verbose is on it will print ut the stack trace of the given error 
			if (Debug.isVerbose) { //error begins
				//Errors.beginError();
				//e1.printStackTrace(System.out);				//prints out specified stack trace from the given error
				//returns back to normal execution
				mainMenu();
			}
			//returns back to normal execution if verbose mode is off 
			mainMenu();
		}
		//otherwise returns back to normal execution
		mainMenu();
		
    }
	
	
	/**
	 * Checks if jar file is in the class path and is valid
	 * @param jarName is the name of the jar file
	 * @return true if jar file is acceptable, false otherwise
	 * Based on one of the answers from http://stackoverflow.com/questions/20152195/how-to-check-if-a-jar-file-is-valid
	 */
	public static boolean jarValid(String jarName) {
		try {
		        JarFile myJar = new JarFile(jarName); // create my jar file based on the string parameter
		        Enumeration<? extends ZipEntry> e = myJar.entries(); // Create my entries for the jar file
		        while(e.hasMoreElements()) { // Loop of entries in the jarfile
		            ZipEntry entry = e.nextElement();
		        }
		        return true; //Return true if no exception has been thrown
			} catch(Exception myException) {
		        return false; //If any exceptions have been thrown, we know that the jar file is not valid
			}
	}
	
	
	/**
	 * Checks if class is in the jar file and is valid
	 * @param jarName is the name of the jar file, className is the name of the class
	 * @return true if class is acceptable, false otherwise
	 * Based on one of the answers from http://stackoverflow.com/questions/11016092/how-to-load-classes-at-runtime-from-a-folder-or-jar
	 */
	public static boolean isClassValid(String jarName, String className) {
		try {
			JarFile jarFile = new JarFile(jarName); // Creates jarfile from parameter
			Enumeration e = jarFile.entries(); // Creates entries from the jarfile
			URL[] urls = { new URL("jar:file:" + jarName + "!/") }; // Creates jarfile URL
			URLClassLoader cl = URLClassLoader.newInstance(urls); // Creates classloader
		    while (e.hasMoreElements()) { // Loop of all classes from the jarfile
		        JarEntry je = (JarEntry) e.nextElement(); // Jarfile entries
		        if(je.isDirectory() || !je.getName().endsWith(".class")){ // Checks only for classes
		            continue;
		        }
		        String classes = je.getName().substring(0,je.getName().length()-6); // Gets class name
		        classes = classes.replace('/', '.'); // Replaces slashes to periods to access class name
		        if(classes.equals(className)) { // Checks if the current class name is the same as the class name argument
		        	return true; // Returns true if argument class name exists
		        }
		    }
		    return false; // Otherwise we return false because class name argument is not found
		} catch(Exception ex) {
			return false; // If exceptions somehow occur we return false
		}
	}

	
	
	
	
}