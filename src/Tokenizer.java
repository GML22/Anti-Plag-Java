 import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.TreeMap;

/** The class where the joined source code of whole project (without comments and lower case) is switching into strictly defined set of 3-letter tokens. Each new generated token is kept as a
	node with the position of original word in source code. Each node has pointer to next and previous node (in order like in source code). The constructor of this class creates Node
	structure and returns the fist Node - the node that contains first significant substring od source code. Two navigate through such defined Node structure, the application just
	call a method GetPrev or GetNext on the curent Node
**/

public class Tokenizer {
	
	private Node text;
	private int NodeCounter = 0;
	private double wsp;
		
	public Tokenizer(String str){
		
		this.text = Tokenize(str);
		
	}
	
	/**
         * The function returns the root of Node structure (Node chain), containing pointers to next and previous nodes, the tokens as strings and position of string in source code
         *         
         * @param  str  whole source code of the project, lower case and without comments
         * @return    Root of tokenized node structure (tokens joined bu pointers)
   **/	
	
	public Node Tokenize(String str){
		
		int licznik1 = 0, counter = 0, indx=0; //counter - the current position in tokens matrix - place where current token can be added,  indx - current index of token in original source code
		String TempSTR, nextTk;
		HashSet<String> classnames = new HashSet<String>(); //set that contains all classnames used in source code
		HashSet<String> objnames = new HashSet<String>(); //set that contains all objectnames used in source code
		HashSet<String> varnames = new HashSet<String>(); //set that contains all varnames used in source code
		HashSet<String> primitives = new HashSet<String>(); //set that contains names of primitive types in java
		TreeMap<String,String> Keywords = new TreeMap<String,String>(); //set that contains all names of keywords of java language
		Node Root, previous;
		
		CreateKeyWords(Keywords);
		
		//defines set of primitive types of variables
		primitives.add("INT");  // INT - token fot int type
		primitives.add("BYT"); // BYT - token for byte type
		primitives.add("SHT"); // SHT - token for short type
		primitives.add("LNG"); // LNG - token for long type
		primitives.add("FLT"); // FLT - token for float type
		primitives.add("BOO"); // BOO - token for boolean type  
		primitives.add("CHR"); // CHR - token fo char type
		primitives.add("VOD"); // VOD - token for void type
		primitives.add("STR"); // STR - token for String type
					
		StringTokenizer tokenizer = new StringTokenizer(str, "; \\.<\\(\t>:=\\+\\-/\\*\\{\\}\\)&~,\\[\\]!\n%\"\\|",true); // the place where souce code is separated to single words/signs, based on signs listed as parameter of StringTokenizer, with maintaining these signs
		String[][] tokens = new String[tokenizer.countTokens()][3]; //creates an array that will keep new tokens
				
		while(tokenizer.hasMoreTokens()){ //first stage of tokenization, every single word/sign gained from String Tokenizer is switch with suitable token or is added without switching (if doesn't fulfield any of bottom condition)
			
			nextTk = tokenizer.nextToken();	//gets next  word/sign of source code separated by StringTokenizer
			
			if (nextTk.equals(" ")) continue; //if nextTk is space then nothing happens			
			
			//the set of conditions (lines 63-90) that adds nextTk to classnames set or varnames set on specific conditions 
			if (counter > 3 && (Character.isLetter(nextTk.charAt(0)) || nextTk.equals("=") || nextTk.equals(">") || nextTk.charAt(0) == '_' || nextTk.equals(";")) ){
				
				if(tokens[counter-1][1].equals("LCL") || tokens[counter-1][1].equals("NEW") || tokens[counter-1][1].equals("EXT") || tokens[counter-1][1].equals("IMP")){ //if previous added token is LCL("class") or NEW("new") or EXT("extends") 
					
					if(!(classnames.contains(nextTk))) classnames.add(nextTk);						
				}	
				else if(tokens[counter-2][1].equals("LT") && nextTk.equals(">")){ //if token added to matrix two iteration ago is eqaul to LT ("<")
					
					if(!(classnames.contains(tokens[counter-1][1]))) classnames.add(tokens[counter-1][1]);
					if(!(classnames.contains(tokens[counter-3][1]))) classnames.add(tokens[counter-3][1]);
					
				}					 
				else if(primitives.contains(tokens[counter-1][1])){	 // if previously added token's name is in the set on names of primitive types		
					
					if(Character.isLetter(nextTk.charAt(0)) || nextTk.charAt(0) == '_'){ //if firts character of nextTk is letter od "_"
						
						if(!(classnames.contains(nextTk)) && !(varnames.contains(nextTk)) && !(objnames.contains(nextTk))) varnames.add(nextTk);				
					}
				}
				else if(tokens[counter-2][1].equals("COM") && (nextTk.equals("=") || nextTk.equals(";"))){ //if token added two iterations ago is equal to COM (",")
					
					if(Character.isLetter(tokens[counter-1][1].charAt(0)) || tokens[counter-1][1].charAt(0) == '_'){
						
						if(!(classnames.contains(counter-1)) && !(varnames.contains(counter-1)) && !(objnames.contains(counter-1))) varnames.add(tokens[counter-1][1]);
					}
				}
			}
			
			// lines (92-111) find all arays and name them obcject_name
			if(counter > 1){ 
				
				if(nextTk.equals("]") && tokens[counter-1][1].equals("LBR")){ // LBR = "{"
					
					if(!(objnames.contains(tokens[counter-2][1])) && !(classnames.contains(tokens[counter-2][1]))) objnames.add(tokens[counter-2][1]); //adds to the objectnames element  if it wasn't add earlier
					counter -= 2;
					tokens[counter][1] = "AOB";	 //AOB = array object
					tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
					tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
					indx = str.indexOf(nextTk, indx) + nextTk.length(); 
					while(!(Character.isLowerCase((nextTk = tokenizer.nextToken()).charAt(0))) && !(nextTk.charAt(0) == '_'));
					tokens[counter][1] = "ONM";	// ONM = object name
					if(!(objnames.contains(nextTk)) && !(classnames.contains(nextTk))) objnames.add(nextTk);
					tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));	
					tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
					indx = str.indexOf(nextTk, indx) + nextTk.length();					
					continue;
				}
			}	
			if (nextTk.equals("import")){ //replace library name and path with words: IMP and LNM
				
				tokens[counter][1] = "IMT"; //IMT = "import"
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length() + 1);
				indx = str.indexOf(nextTk, indx) + nextTk.length();					
				while (tokenizer.hasMoreTokens() && !(nextTk = tokenizer.nextToken()).equals(";")){
					
					if(Character.isLetter(nextTk.charAt(0)) && !(classnames.contains(nextTk))) classnames.add(nextTk);
				}
				tokens[counter][1] = "LNM"; // LNM = library name		
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) - 1);
				tokens[counter][1] = "SEM"; // SEM = ";"
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
				indx = str.indexOf(nextTk, indx) + nextTk.length();						
				continue;				
			}
			if(counter > 1){
				
				if(tokens[counter-1][1].equals("ITR")){ //ITR = "Integer"
				
					tokens[counter][1] = "INM"; //INM = integer name
					tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
					tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
					indx = str.indexOf(nextTk, indx) + nextTk.length();					
					continue;						
				}
			}
			if(nextTk.charAt(0) == '@'){ //for all annontiations in source code
				
				tokens[counter][1] = "ANN"; //ANN - annotation
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
				indx = str.indexOf(nextTk, indx) + nextTk.length();					
				continue;
			}				
			if(counter > 1){ //for naming exception object after exception name as an object
				
				if(tokens[counter-1][1].equals("EXP") || tokens[counter-1][1].contains("exception")){ //EXP = exception name
					
					tokens[counter-1][1] = "EXP"; //EXP = exception name
					if (Character.isLetter(nextTk.charAt(0))){
						
						tokens[counter][1]= "ONM"; //ONM = object name
						if(!(objnames.contains(nextTk)) && !(classnames.contains(nextTk))) objnames.add(nextTk);
						tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
						tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
						indx = str.indexOf(nextTk, indx) + nextTk.length();
						continue;
					}
				}
			}
			if (nextTk.equals("package")){ //replace package name and path with words: IMP and LNM
				
				tokens[counter][1] = "LPG"; //LPG -"package"
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx)  + nextTk.length() + 1);			
				indx = str.indexOf(nextTk, indx) + nextTk.length();
				while (tokenizer.hasMoreTokens() && !(nextTk = tokenizer.nextToken()).equals(";")){
					
					if(Character.isLetter(nextTk.charAt(0)) && !(classnames.contains(nextTk))) classnames.add(nextTk);
				}
				tokens[counter][1] = "PNM"; // PNM = package name
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) - 1);
				tokens[counter][1] = "SEM"; //SEM = ";" 
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
				indx = str.indexOf(nextTk, indx) + nextTk.length();						
				continue;				
			}
			if(counter > 1){
				
				if(nextTk.equals("(") && Character.isLowerCase(tokens[counter-1][1].charAt(0))){ //for adding token for methods
					
					tokens[counter-1][1] = "MNM"; //MNM = method name
					tokens[counter][1] = "LPR"; //LPR = "("
					tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
					tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
					indx = str.indexOf(nextTk, indx) + nextTk.length();
					continue;						
				}
			}
			if(counter > 2){  //for class members, not methods
				
				if(tokens[counter-2][1].equals("DOT") && !tokens[counter-3][1].equals("DOT") && !nextTk.equals("(") && Character.isLowerCase(tokens[counter-1][1].charAt(0))) tokens[counter-1][1] = "CMB";	//DOT = ".", CMB = clas member			
			}
			if(counter > 1){
				
				if(!tokens[counter-1][1].equals("RBR") && !tokens[counter-1][1].equals("RPR") && !tokens[counter-1][1].equals("SVL") && nextTk.equals(".") && Character.isLowerCase(tokens[counter-1][1].charAt(0))){ //RBR = "]", RPR = ")", SVL = string Value 
					
					if(!(objnames.contains(tokens[counter-1][1])) && !(classnames.contains(tokens[counter-1][1]))) objnames.add(tokens[counter-1][1]);	
					if(Character.isLetter(tokens[counter-1][1].charAt(0)) || tokens[counter-1][1].charAt(0) == '_') tokens[counter-1][1] = "ONM";	//ONM = object name			
				}
			}				
			if((TempSTR = Keywords.get(nextTk))!= null){ //for searchin nextTk in keywords set
				
				tokens[counter][1] = TempSTR;
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
				indx = str.indexOf(nextTk, indx) + nextTk.length();					
				continue;	
			}
			if(nextTk.equals("\"")){ //ensures that all senstances in "" will be marked as SVL 
				
				String previous1 = "", previous2 = ""; // variables that represent nextTk-1 and nextTk -2
				
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
				indx = str.indexOf(nextTk, indx) + nextTk.length();
				
				while(tokenizer.hasMoreTokens() && !nextTk.equals("\n")){	
					
					nextTk = tokenizer.nextToken();
					
					if(nextTk.equals("\"") && !previous1.equals("\\")) break;
					else if(nextTk.equals("\"")  && previous1.equals("\\") && previous2.equals("\\")) break;
					previous2 = previous1;
					previous1 = nextTk;
				}
				tokens[counter][1] = "SVL"; //SVL = string value
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
				indx = str.indexOf(nextTk, indx) + nextTk.length();
				continue;
			}
			if(nextTk.contains("'")){ //ensures that all characters marked by '' will be tokenized to CVL
				
				tokens[counter][1] = "CVL"; // CVL = character value
				tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
				tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
				indx = str.indexOf(nextTk, indx) + nextTk.length();
				continue;					
			}
			if(counter > 2){ //finds all numebers and name them as NUM
				
				if(nextTk.equals("]") && tokens[counter-1][1].equals("DOT") && tokens[counter-2][1].equals("NUM")){ 
					
					counter-=2;
					tokens[counter][1] = "NUM"; //Num = number
					tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
					tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
					indx = str.indexOf(nextTk, indx) + nextTk.length();
					continue;
				}
			}				
			if(counter > 2 && nextTk.matches("^[0-9]+[a-zA-Z]?")){ //finds all NUMs, regex - negative lookbehind and negative lookahead for letters
				
				if(tokens[counter-1][1].equals("DOT") && tokens[counter-2][1].equals("NUM")){ //for NUMs that are seperated with .
					
					counter-=2;
					tokens[counter][1] = "NUM";
					tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
					tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());				
					indx = str.indexOf(nextTk, indx) + nextTk.length();
					continue;
				}
				else{	//for the rest NUMs
					
					tokens[counter][1] = "NUM";
					tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));	
					tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
					indx = str.indexOf(nextTk, indx) + nextTk.length();
					continue;
				}						
			}
				
			// if nextTk doesn't fulfielled any of above conditions it is added to tokens matrix in its source code form
			tokens[counter][1] = nextTk;
			tokens[counter][0] = String.valueOf(str.indexOf(nextTk, indx));
			tokens[counter++][2] = String.valueOf(str.indexOf(nextTk, indx) + nextTk.length());
			indx = str.indexOf(nextTk, indx) + nextTk.length();
				
		}
		
		Root = new Node(tokens[0]); //creates a node that will be the root node for node structure
		previous = Root;
		this.NodeCounter++; //counts the number of node in node structure
				
		for(int j=0;j<counter;j++){	//Second stage of tokenization - in this stage the tokens array is analyzed and the words that weren't found earlier are switch to tokens
						
			if(j > 1){  //creates a chain of nodes (node structure), which can be iterate by next or previous pointer
			
			if(!(tokens[j-1][1].equals("\n") || tokens[j-1][1].equals("\t"))) { 
					
					Node current = new Node(tokens[j-1]);
					previous.setNext(current);
					current.setPrev(previous);
					previous = current;
					this.NodeCounter++;
				}	
			}
			
			if(j > 2){ //searches for objects
				
				if(tokens[j-1][1].equals("LCR")){ 
					
					int n = 2;
					while(!tokens[j-n][1].equals("RPR") && !tokens[j-n][1].equals("LCL") && !tokens[j-n][1].equals("IFC") && !tokens[j-n][1].equals("\n")) n++;
					while(!tokens[j-n-1][1].equals("LPR") && !tokens[j-n-1][1].equals("RPR") && !tokens[j-n][1].equals("LCL") && !tokens[j-n][1].equals("IFC")  && !tokens[j-n][1].equals("\n")) n++;
					if(tokens[j-n-2][1].equals("MNM")){
						
						int m = 1;
						while(!tokens[j-n-1+m][1].equals("RPR")){
							
							if((Character.isLetter(tokens[j-n-1+m][1].charAt(0)) || tokens[j-n-1+m][1].charAt(0) == '_') && Character.isLowerCase(tokens[j-n-1+m][1].charAt(0))){
								
								if(!(objnames.contains(tokens[j-n-1+m][1])) && !(classnames.contains(tokens[j-n-1+m][1]))) objnames.add(tokens[j-n-1+m][1]); 
								if(!(classnames.contains(tokens[j-n-1+m][1]))) classnames.add(tokens[j-n-1+m][1]); //adds parameters of function to sets of object and class names
								
							}
							
							if(!tokens[j-n-1+m][1].equals("DOT") && Character.isLowerCase(tokens[j-n-1+m][1].charAt(0))){
							
								if(m%3 == 1){
									
									if (!primitives.contains(tokens[j-n-1+m][1])){
										
										if(tokens[j-n-1+m][1].equals("UCD")) licznik1--; //UCD - undefined code, licznik1 - counts words that weren't switch to tokens
										tokens[j-n-1+m][1] = "CNM";	 //CNM - class name							
										if(!classnames.contains(tokens[j-n-1+m][1])) classnames.add(tokens[j-n-1+m][1]);
									}
								}
								if(m%3 == 2){
									
									if(tokens[j-n-1+m][1].equals("UCD")) licznik1--; //UCD - undefined code
									tokens[j-n-1+m][1] = "ONM"; //ONM - Object name
									if(!classnames.contains(tokens[j-n-1+m][1]) && !objnames.contains(tokens[j-n-1+m][1])) objnames.add(tokens[j-n-1+m][1]);								
								}
							}
							m++;
						}
						continue;
					}									
				}	
			}	
			
			if(!(Character.isLowerCase(tokens[j][1].charAt(0))) && !(tokens[j][1].charAt(0) == '_')) continue; //all botton conditions should assume that character is either letter or _ so it is easier to jump to the next loop 
			//code runs significantly faster with above condition
						
			if(j > 1){ //and token CLN (class name) after words public, private, protected
		
				if(tokens[j-1][1].equals("PUB") || tokens[j-1][1].equals("PRV") || tokens[j-1][1].equals("PRD")){ //PUB - "public", PRV - "private", PRD - "protected"
					
					if(!(classnames.contains(tokens[j+1][1]))) classnames.add(tokens[j][1]);
					tokens[j][1] = "CLN";  //CLN - "class name"
		
					if((Character.isLowerCase(tokens[j+1][1].charAt(0)) || tokens[j+1][1].charAt(0) == '_')  &&  !tokens[j][1].equals("LCL") && !tokens[j+1][1].equals("EXT") && !tokens[j+1][1].equals("IMP")){ //LCL - class name, EXT - extends, IMP - import
						
						if(!(objnames.contains(tokens[j+1][1])) && !(classnames.contains(tokens[j+1][1]))){
							
							objnames.add(tokens[j+1][1]);
							tokens[j+1][1] = "ONM";   //ONM - object name
							continue;
						}				
					}				
				}
			}
			if(j > 3){ //another condition for creating object name token
				
				if(tokens[j-3][1].equals("LT") && tokens[j-1][1].equals("GT")){ //GT - grater than, LT - less than
					
					if(!(objnames.contains(tokens[j][1])) && !(classnames.contains(tokens[j][1]))) objnames.add(tokens[j][1]);
					tokens[j][1] = "ONM";
					continue; 
				}
			}
			if(j > 5){ //another conditions for creating object name token
				
				if(tokens[j-5][1].equals("LT") && tokens[j-3][1].equals("COM") && tokens[j-1][1].equals("GT")){ //COM - ","
					
					if(!(objnames.contains(tokens[j][1])) && !(classnames.contains(tokens[j][1]))) objnames.add(tokens[j][1]);
					tokens[j][1] = "ONM";
					continue;
				}					
			}			
			if(j > 5){ //another conditions for creating object name token
					
				if(tokens[j-5][1].equals("LT") && tokens[j-4][1].equals("QMK") &&  tokens[j-3][1].equals("EXT") && tokens[j-1][1].equals("GT")){
					
					if(!(objnames.contains(tokens[j][1])) && !(classnames.contains(tokens[j][1]))) objnames.add(tokens[j][1]);
					tokens[j][1] = "ONM";
					continue;
				}					
			}
			if(j > 1 && j < counter - 1 && classnames.contains(tokens[j][1])){	//another conditions for creating object name token	
				
				tokens[j][1] = "CLN"; //CLN - class name	
				if((Character.isLowerCase(tokens[j+1][1].charAt(0)) || tokens[j+1][1].charAt(0) == '_')  &&  !tokens[j][1].equals("LCL") && !tokens[j+1][1].equals("EXT") && !tokens[j+1][1].equals("IMP")){
					
					if(!(objnames.contains(tokens[j+1][1])) && !(classnames.contains(tokens[j+1][1]))){
						
						objnames.add(tokens[j+1][1]);
						tokens[j+1][1] = "ONM";
						continue;
					}						
				}							
			}
			if(objnames.contains(tokens[j][1])){ tokens[j][1] = "ONM"; continue; } //if token is in objnames set it gets nam ONM
			if(j > 1 && j < counter - 1 && varnames.contains(tokens[j][1])){
							
						if(tokens[j+1][1].equals("LPR")){ tokens[j][1] = "MNM"; continue; } //user-defiened function name
						else if(Character.isLowerCase(tokens[j][1].charAt(0))){ tokens[j][1] = "VNM"; continue; } //user-defiened variable name	

			}
			if(!tokens[j][1].equals(" ") && (Character.isLowerCase(tokens[j][1].charAt(0)) || tokens[j][1].charAt(0) == '_')){ 
				
				tokens[j][1] = "UCD"; //UCD - undefined code - token for all words that wasnt defined by this algorithm
				licznik1++;
				continue;
			}		
		}				
					
		this.wsp = (double)100 * ((double) 1 - (double) licznik1/counter); //the coefficient of tokenization (in GUI is used in the table with similarity socre)
									
		return Root; //returns root of the node structure
	}
	
	/**
     * The function returns the root node of tokenized text
     *         
     * @return    Root of tokenized node structure (tokens joined bu pointers)
     **/	
	
	public Node GetTokenizeText(){
		
		return this.text;
	}
	
	/**
     * The function returns the coefficient of tokenization i.e the proporiton of not tokenized to tokenized words
     *         
     * @return    Tokenization coefficient
     **/	
	
	public double GetTokenCoef(){
		
		return this.wsp;
	}
	
	/**
     * The function returns the umber of Node in Node chain i.e number of tokenized words
     *         
     * @return    Number of tokenized Nodes 
     **/
	
	public int GetNodeCounter(){
		
		return this.NodeCounter;
	}
	
	/**
     * The function creates the TreeMap of java language keywords 
     *         
     * @param   Empty TreeMap to fill with java language keywords
     **/
	
	public static void CreateKeyWords(TreeMap<String,String> KeyW){		
				
		KeyW.put(";", "SEM");
		KeyW.put("&", "AND");
		KeyW.put("|", "OR");
		KeyW.put("=", "EQL");
		KeyW.put("[", "LBR");
		KeyW.put("]", "RBR");
		KeyW.put(".", "DOT");
		KeyW.put("<", "LT");		
		KeyW.put(",", "COM");
		KeyW.put(">", "GT");
		KeyW.put("{", "LCR");
		KeyW.put("}", "RCR");
		KeyW.put("(", "LPR");
		KeyW.put(")", "RPR");
		KeyW.put("+", "PLS");
		KeyW.put("-", "MIN");
		KeyW.put("/", "DIV");
		KeyW.put("%", "MOD");
		KeyW.put("!", "NOT");
		KeyW.put("~", "BNT");
		KeyW.put("\\", "BSL");
		KeyW.put("*", "PDT");
		KeyW.put(":", "COL");
		KeyW.put("class_end", "CED");
		KeyW.put("exception", "EXP");
		KeyW.put("true", "TRU");
		KeyW.put("false", "FLS");
		KeyW.put("null", "NUL");
		KeyW.put("new", "NEW");
		KeyW.put("string", "STR");
		KeyW.put("void", "VOD");
		KeyW.put("boolean", "BOO");
		KeyW.put("byte", "BYT");
		KeyW.put("char", "CHR");
		KeyW.put("double", "DBL");
		KeyW.put("short", "SHT");
		KeyW.put("int", "INT");
		KeyW.put("float", "FLT");
		KeyW.put("long", "LNG");		
		KeyW.put("if", "IF");
		KeyW.put("else", "ELS");
		KeyW.put("for", "FOR");
		KeyW.put("while", "WHL");		
		KeyW.put("do", "DO");
		KeyW.put("break", "BRK");	
		KeyW.put("continue", "CNT");
		KeyW.put("return", "RTN");
		KeyW.put("switch", "SWT");
		KeyW.put("throw", "THR");
		KeyW.put("case", "CAS");
		KeyW.put("@override", "ORD");
		KeyW.put("character", "CTR");
		KeyW.put("integer", "ITR");
		KeyW.put("enumeration", "CLN");
		KeyW.put("enum", "ENM");
		KeyW.put("SYS", "SYS");
		KeyW.put("out", "OUT");
		KeyW.put("ARG", "ARG");
		KeyW.put("MAI", "MAI");
		KeyW.put("abstract", "ABT");
		KeyW.put("strictfp", "SCF");
		KeyW.put("assert", "AST");
		KeyW.put("enumerate", "ENU");
		KeyW.put("static", "STC");
		KeyW.put("?", "QMK");
		KeyW.put("extends", "EXT");
		KeyW.put("super", "SPR");
		KeyW.put("private", "PRV");
		KeyW.put("public", "PUB");
		KeyW.put("protected", "PRD");
		KeyW.put("transient", "TRT");
		KeyW.put("native", "NVT");	
		KeyW.put("threadsafe", "TSF");
		KeyW.put("synchronized", "SYN");
		KeyW.put("volatile", "VOL");		
		KeyW.put("interface", "IFC");
		KeyW.put("default", "DFT");
		KeyW.put("class", "LCL");
		KeyW.put("implements", "IMP");
		KeyW.put("this", "THS");
		KeyW.put("throws", "TWS");	
		KeyW.put("try", "TRY");
		KeyW.put("actionevent", "ACT");
		KeyW.put("finally", "FLY");
		KeyW.put("final", "FIN");
		KeyW.put("catch", "CTH");
		KeyW.put("goto", "GTO");
		KeyW.put("const", "CST");		
		KeyW.put("instanceof", "INO");	
		KeyW.put("runnable", "RUN");		
		KeyW.put("iterator", "ITR");		
	}	
}
