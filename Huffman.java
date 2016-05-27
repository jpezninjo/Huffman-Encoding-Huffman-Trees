public class Huffman {
	
	private static String magicNum = "0x4846";
	private static boolean debug = false;

	public static class Heap {
	
	 public int factorial(int n) {
	   if (n == 0) {
	           return 1;
	       } else {
	           return n * factorial(n - 1);
	       }
	   }

	private Node[] Heap;
    private int maxsize;
    private int size;

    public Heap(int max) {
	maxsize = max;
	Heap = new Node[maxsize];
	size = 0 ;
	Heap[0] = new Node("null", Integer.MIN_VALUE);
    }
    
    public Node returnFirst(){
    	return Heap[1];
    }

    private int leftchild(int pos) {
	return 2*pos;
    }
    private int rightchild(int pos) {
	return 2*pos + 1;
    }

    private int parent(int pos) {
	return  pos / 2;
    }
    
    private boolean isleaf(int pos) {
	return ((pos > size/2) && (pos <= size));
    }

    private void swap(int pos1, int pos2) {
	Node tmp;

	tmp = Heap[pos1];
	Heap[pos1] = Heap[pos2];
	Heap[pos2] = tmp;
    }

    public void insert(Node newNode) {
	size++;
	Heap[size] = newNode;
	int current = size;
	
	while (Heap[current].frequency < Heap[parent(current)].frequency) {
	    swap(current, parent(current));
	    current = parent(current);
	}	
    }

    public void print() {
	int i;
	for (i=1; i<=size;i++)
	    System.out.print(Heap[i] + " ");
	System.out.println();
    }

    public Node removemin() {
	swap(1,size);
	size--;
	if (size != 0)
	    pushdown(1);
	return Heap[size+1];
    }

    private void pushdown(int position) {
	int smallestchild;
	while (!isleaf(position)) {
	    smallestchild = leftchild(position);
	    if ((smallestchild < size) && (Heap[smallestchild].frequency > Heap[smallestchild+1].frequency))
		smallestchild = smallestchild + 1;
	    if (Heap[position].frequency <= Heap[smallestchild].frequency) return;
	    swap(position,smallestchild);
	    position = smallestchild;
	}
    }
    
    public void printTree(){
    	printTree(Heap[1]);
    }

	public void combine() {
		while(this.size != 1){
			Node tmp1 = this.removemin();
			Node tmp2 = this.removemin();
			int sum = tmp1.frequency + tmp2.frequency;
			this.insert(new Node(sum, tmp1, tmp2));
		}
	}
	public void printTree(Node node){
	
		boolean printTreeDebug = false;
	
		System.out.println(node);	//keep this
		if(!node.isLeaf()){
			printTree(node.leftChild);
			printTree(node.rightChild);
		} else{
			if (printTreeDebug) System.out.println("Called printTree() on leaf " + node.character);
		}
	}

}

	public static class Node implements Comparable<Node>{
		
		/** binaryPath of this node */
		private String binaryNum;
		
		/** the keyboard character associated with this node */
		private String character;
		
		/** the frequency of character, used for compressing */
		private int frequency;
		
		/** reference to the left child if this node is not a leaf */
		private Node leftChild;
		
		/** reference to the right child, if this node is not a leaf */
		private Node rightChild;
		
		/**
		 * Constructor for when reading in a compressed file
		 * 
		 * @param character
		 * @param frequency
		 */
		public Node(String character, int frequency){
			
			this.binaryNum = "";
			this.character = character;
			this.frequency = frequency;
			this.leftChild = null;
			this.rightChild = null;
		}
		
		/**
		 * Constructor for when combining nodes during file compression
		 * 
		 * @param frequency	frequency of character
		 * @param left	reference to the left child, if there is one
		 * @param right	reference to the right child, if there is one
		 */
		public Node(int frequency, Node left, Node right){
			//an internal node should not store a valid character
			this.character = "null";
			this.frequency = frequency;
			this.leftChild = left;
			this.rightChild = right;
		}
		
		/** character setter */
		public void setCharacter(String newChar) {
			this.character = newChar;
		}
		
		/** left child setter */
		public void setLeft(Node newNode){
			this.leftChild = newNode;
		}
		
		/** right child setter */
		public void setRight(Node newNode){
			this.rightChild = newNode;
		}
		
		/** binary path setter */
		public void setBinary(String num){
			this.binaryNum = num;
		}
		
		/**
		 * Returns true if the current node is a leaf
		 * @return true if is a leaf, false if internal node
		 */
		public boolean isLeaf(){
			return (leftChild == null);
		}
		
		@Override
		public String toString(){
			String toString = "Char: " + character + ", " + frequency;
			if( !(leftChild == null && rightChild == null)){
				toString += ", left: " + leftChild.character + ", right: " + rightChild.character +
						", binaryNum: " + binaryNum;
			}
			return toString;
		}

		/** unused, sad, neglected method #rip TreeSets, bois */
		@Override
		public int compareTo(Node o) {
			if(this.frequency == o.frequency){
				return 0;
			}
			return (this.frequency > o.frequency ? 1 : -1);
		}
	}

	public static void main(String[] args){

		if (args.length < 3 || args.length > 5 || !(args[0].contains("u") || args[0].contains("c"))){
			Usage();
		}
		
		boolean compress = (args[0].equals("-c") ? true : false);
		String inputFile = args[args.length - 2];
		String outputFile = args[args.length - 1];
		
		if(debug){
			System.out.println("Compress: " + compress);
			System.out.println("Input: " + inputFile);
			System.out.println("Output: " + outputFile);
		}
		
		String flag1 = "", flag2 = "";
		if(args.length > 3){
			flag1 = args[1];
			if(debug)System.out.println("Found flag: " + flag1);
			if(args.length > 4){
				flag2 = args[2];
				if(debug)System.out.println("Found flag: " + flag2);
			}
		}
		boolean forceCompress = ( (flag1.contains("f") | flag2.contains("f")) ? true : false);
		boolean useVerbose = ( (flag1.contains("v") | flag2.contains("v")) ? true : false);
		if(debug){
			System.out.println("Force compress: " + forceCompress);
			System.out.println("Use verbose: " + useVerbose);
		}
		//=============================================================================//
		
		if(compress){
			
			//File Reading-Frequency finding
			//=============================================================================//
			int numberOfChars = 255, bytes = 0;
			int[] frequencyArray = new int[numberOfChars];
//			char nextChar = 'Z';
			TextFile fileReader = new TextFile(inputFile, 'r');

			while(!fileReader.EndOfFile()){
//				nextChar = fileReader.readChar();
//				int pos = (int)nextChar;
				frequencyArray[(int)fileReader.readChar()] += 1;
				bytes++;
			}
			//=============================================================================//
	
			//TODO marker
			System.out.println("The uncompressed file has " + 
					bytes + " total characters, " + bytes*8 + " total bytes");
			
			
			//Tree Making
			//=============================================================================//
			System.out.println("Started Tree Making");
//			HashSet<Node> nodeSet = new HashSet<Node>();
			Heap heap = new Heap(25000);	//TODO heap shit
			int newBytes = 16 + 32;
			String[] codes = new String[255];
			for(int i = 0; i < numberOfChars; i++){
				if(frequencyArray[i] != 0){
					Node node = new Node(Character.toString((char)i), frequencyArray[i]);
//					nodeSet.add(node);
					node.binaryNum = "";
					newBytes = calcBytesplusassignBinaryPath(node, "", codes);
					heap.insert(node);
				}
			}
			heap.combine();
			
//			System.out.println("Started Node combining with " + nodeSet.size() + " fucking nodes");
//			HashSet<String> characters = new HashSet<String>();
//			int k = nodeSet.size();
//			while(nodeSet.size() != 1){		//TODO figure out why this doesnt work with huge files
////			while(k != 1){
////				System.out.println(k--);
//				Node small = new Node("dummy", 101);
//				Node smallest = new Node("dummyer", 100);
//				for(Node node : nodeSet){
//					if(node.character != null && !(node.character.equals("null"))){
////						characters.add(node.character);
//					}
//					if(node.frequency < small.frequency){
//						if(node.frequency < smallest.frequency){
//							small = smallest;
//							smallest = node;
//						}else{
//							small = node;
//						}
//					}
//				}
//				
//				//TODO oops just changed this
//				Node combine = new Node(small.frequency + smallest.frequency, smallest, small);
//				small.setBinary("0");
//				smallest.setBinary("1");
//				nodeSet.remove(smallest);
//				nodeSet.remove(small);
//				nodeSet.add(combine);
//				System.out.println(nodeSet.size());
//			}
			System.out.println("Finished Tree Making");
			//=============================================================================//
			
			
//			int newBytes = 16 + 32;
			
			
			//Binary path assigning
			//=============================================================================//
			System.out.println("Started Assigning Binary Paths");
//			String[] codes = new String[255];
//			Node firstNode = null;
//			for(Node node : nodeSet){
//				firstNode = node;
//				System.out.println("EAFAWFAWE");
//				System.out.println(nodeSet.size());
//				System.out.println(nodeSet);
				
				
//				node.binaryNum = "";
//				newBytes = calcBytesplusassignBinaryPath(node, "", codes);
//			}
			//=============================================================================//
			
			
			
			for(int i = 0; i < numberOfChars; i++){
				if(frequencyArray[i] != 0){
					Node node = new Node(Character.toString((char)i), frequencyArray[i]);
					//BYTE HANDLING
					newBytes += frequencyArray[i] * codes[i].length();
					System.out.println("adding " + frequencyArray[i] + " * " + codes[i]);
					System.out.println("new bytes: " + newBytes);
					
//					nodeSet.add(node);
				}
			}
			
			System.out.println("Old bytes " + newBytes);
			newBytes = (int) doThis(newBytes);
			System.out.println("New bytes " + newBytes);
			
			if(bytes < newBytes){
				if(!(forceCompress)){
					System.err.println("Cannot complete compressing");
				}else{
					System.err.println("Force compression flag detected, continuing program");
				}
			}
			
			
			System.err.println("Starting process of writing to file");
			
			//Output file writing
			//=============================================================================//
			BinaryFile writer = new BinaryFile(outputFile, 'w');
			for(char chr : magicNum.toCharArray()){
				writer.writeChar(chr);
			}
			writer.writeChar(' ');
			//=============================================================================//

			beMyWriterMinion(writer, heap.returnFirst());
			boolean t = true;
			if(t){
				System.exit(0);
			}
			writer.writeChar(' ');
			//=============================================================================//
			fileReader.rewind();
			while(!fileReader.EndOfFile()){
				char chr = fileReader.readChar();
				String chr2 = codes[(int)chr];
//				System.err.println("Writing " + chr2);
				for(char chr3 : chr2.toCharArray()){
					writer.writeChar(chr3);
				}
			}
			writer.close();
			//=============================================================================//
			System.out.println("Finished writing file!");
			System.out.println(outputFile);
			System.out.println("Here's the tree!");
			System.out.println("Value\t|Code");
			for(int i = 0; i < codes.length; i++){
				if(codes[i] != null){
					System.out.println((char)i + "\t|" + codes[i]);
				}
			}
//			System.out.println(nodeSet.size());
//			printTree(root);
			
		} else{ //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
			
			System.out.println("Found -u flag, starting uncompressing process");
			
			BinaryFile binaryReader = new BinaryFile(inputFile, 'r');
			
			String inputNum = "";
			char endNum = binaryReader.readChar();
			inputNum += (String.valueOf(endNum));
			System.out.println("Reading in magic number....");
			while(endNum != ' '){
				endNum = binaryReader.readChar();
//				System.out.println(endNum);
				if(endNum == ' '){
					break;
				}
				inputNum += (String.valueOf(endNum));
			}

			
			
			if(!inputNum.equals(Huffman.magicNum)){
				System.out.println("Attempted decompress operation on file with mismatch" +
						" magic number....");
				System.out.println(inputNum + "!");
				System.out.println(" does not match ");
				System.out.println(Huffman.magicNum);
				return;
			}else{
				System.out.println("Match!");
			}
			//==============================================================//
			String treeCode = "";
			endNum = 'n';
			System.out.println();
			System.out.println("Reading in tree code....");
			while(endNum != ' '){
				endNum = binaryReader.readChar();
				if(endNum == ' '){
					break;
				}
				treeCode += endNum;
			}
			System.out.println("Found code: " + treeCode);
//			treeCode = "IIL97L98L99";
			
			Node root = new Node("null", 7005);
			System.out.println("Attempting to decipher this mess...");
			treeCode = readNode(root, treeCode);
//			System.out.println("Head: " + root);
			
			
			System.out.println("Successfully (maybe) read in tree");
//			System.out.println("Would you like to print out the tree? (y/n)");
//			Scanner scanner = new Scanner(System.in);
//			String respond = scanner.nextLine();
//			if(respond.contains("y")){
//				printTree(root);
//			}
			
			//==============================================================//
			System.out.println();
			System.out.println("Assigning binary paths....");
			String[] codes = new String[255];
			codes[0] = "TEST";
			int bytesToEncode = calcBytesplusassignBinaryPath(root, "", codes);
//			System.out.println();
//			for(int i = 0; i < codes.length; i++){
//				if(codes[i] != null){
//					System.out.println((char)i + "\t|" + codes[i]);
//				}
//			}
			System.out.println("Finished table");
			
			//==============================================================//
			String inputText = "";
			String encodedText = "";
			
			TextFile fileWriter = new TextFile(outputFile, 'w');
			
//			endNum = fileReader.readChar();	//TODO why dafaq does commenting this out make work
			encodedText += endNum;
//			System.out.println("mark" + encodedText);
			while(!binaryReader.EndOfFile()){
				
				endNum = binaryReader.readChar();
				inputText += endNum;
			}
			System.out.println();
			System.out.println("Time to decipher:\n"  + inputText);
			
			char ch = 'n';
			while(!inputText.isEmpty()){
//				System.out.println(inputText);
				ch = decipherCode(inputText, codes);
//				System.out.println("Found a " + ch + "!");
				inputText = inputText.substring(codes[(int)ch].length());
				fileWriter.writeChar(ch);
				encodedText += ch;
			}
			System.out.println();
			System.out.println("text to write:\n" + encodedText);
			System.out.println(outputFile);
			fileWriter.close();
		}
	}
	
	private static void beMyWriterMinion(BinaryFile writer, Node node) {

		if(!node.isLeaf()){
			
			System.out.println();
			System.out.println(node);
			System.out.println("Not a leaf");
			
//			writer.writeChar('I');		
			writer.writeBit(false);
			System.out.println("call on left " + node.leftChild);
			beMyWriterMinion(writer, node.leftChild);
			System.out.println("call on right " + node.rightChild);
			beMyWriterMinion(writer, node.rightChild);
		}else{
			System.out.println();
			System.out.println(node);
			System.out.println("Is a leaf");
			writer.writeBit(true);
//			writer.writeChar('L');
			char[] chr = node.character.toCharArray();
			int chr2 = (int)chr[0];
			String chr3 = String.valueOf(chr2);
			for(char ch : chr3.toCharArray()){
				writer.writeChar(ch);
			}
		}
	}

	public static double doThis(int newBytes) {
		int i = 0;
		while(8 * i < newBytes){
			i++;
		}
		return 8 * i;
	}

	public static void Usage(){
		System.out.println("Proper usage: Huffman [-c | -u] (-v) (-f) -input -output");
		System.out.println("\t-c for compress operation, -u for uncompress operation");
		System.out.println("\t-v optional flag, turns on verbose debug statements");
		System.out.println("\t-f optional flag, forces compression even if compressed file "
				+ "\n\t\tis bigger than original");
		System.out.println("\t-input name of file to compress/uncompress");
		System.out.println("\t-output name of file to write to");
		System.out.println();
		System.out.println("Exiting program");
		System.exit(0);
	}
	
	/**
	 * (Compressing method) (Recursive)
	 * Holy fuck wtf does this even do I was hella tired when I wrote this
	 * 
	 * @param node
	 * @param binaryNum		this value is passed from parent to child, child being the argument node
	 * @param codeBook		a String array of yet-existant valid sequences which is filled by this recursive method
	 * @return				lol so this thing also calculates the number of bytes in a tree
	 */
	public static int calcBytesplusassignBinaryPath(Node node, String binaryNum, String[] codeBook){
		
		boolean lolDebug = false;
		
		if(node == null && node == null){
			if(lolDebug) System.out.println("adding " + (node.isLeaf() ? 9 : 1) + " for " + node.character);
			return 9;
		}
		
		String binaryPath = "" + binaryNum + node.binaryNum;
		
		if(lolDebug) System.out.println(node.character + " " + node.frequency + " has" + node.binaryNum + " id"  + binaryPath + " got" + binaryNum);
		
		if(node.character != "null"){
			String string = node.character;
			char[] cha = string.toCharArray();
			codeBook[(int)cha[0]] = binaryPath;
			if(lolDebug) System.out.println(codeBook[(int)cha[0]]);
			System.out.println("Set codes[" + (int)(cha[0]) + "] to " + binaryPath);
		}
		
		int treeBytes = 0;
		treeBytes += calcBytesplusassignBinaryPath(node.leftChild, binaryPath, codeBook);
		treeBytes += calcBytesplusassignBinaryPath(node.rightChild, binaryPath, codeBook);
		
		if(lolDebug) System.out.println("adding " + (node.isLeaf() ? 9 : 1) + " for " + node.character);
		
		return treeBytes + (node.isLeaf() ? 9 : 1);
	}

	
	/**
	 * Helper method for use in conjunction with idk//TODO
	 * Parses the provided String argument and finds 
	 * Does not require a codeBook[] argument
	 * 
	 * @param treeShit	String to parse
	 * @return	the longest valid thingy at the beginning of treeShit
	 */
	public static String getNext(String treeShit){	//TODO rename this to something more appropriate
		String toString = "";
		
		if(treeShit.isEmpty() || !(treeShit.startsWith("0") || treeShit.startsWith("1"))){
			System.out.println("Called getNext on invalid segment");
			return toString;
		}
		
		toString += treeShit.substring(0, 1);
		if(toString.equals("0")){
			return toString;
		} else{
			char[] tmp = treeShit.substring(1).toCharArray();
			toString += tmp[0] + "" + tmp[1];
			if(tmp[0] == '1' || tmp[0] == '2'){
				toString += tmp[2];
			}
			return toString;
		}
	}
	
	/**
	 * If you can't figure this method out, why are you reading my code
	 * 
	 * @param node	initially pass in root node, this method makes recursive calls to itself
	 */
	public static void printTree(Node node){
		
		boolean printTreeDebug = false;
		
		System.out.println(node);	//keep this
		if(!node.isLeaf()){
			printTree(node.leftChild);
			printTree(node.rightChild);
		} else{
			if (printTreeDebug) System.out.println("Called printTree() on leaf " + node.character);
		}
	}
	
	/**
	 * 
	 * 
	 * @param node
	 * @param treeText
	 * @return
	 */
	public static String readNode(Node node, String treeText){
		
		boolean readNodeDebug = true;
		
		if(readNodeDebug) System.out.println("\nTree text is now " + treeText);
		if(treeText.isEmpty()){
			System.out.println("No more shit in tree");
			return null;
		}
		String myText = getNext(treeText);
		treeText = treeText.substring(myText.length(), treeText.length());
//		if(myText.startsWith("I")){
		if(myText.startsWith("0")){
			if(readNodeDebug) System.out.println("Creating internal node");
			Node left = new Node("null", 1);
			left.setBinary("1");
			treeText = readNode(left, treeText);
			if(readNodeDebug) System.out.println("Got left " + left);
			Node right = new Node("null", 1);
			right.setBinary("0");
			treeText = readNode(right, treeText);
			if(readNodeDebug) System.out.println("Got right " + right);
			if(readNodeDebug) System.out.println("\t made internal node with children " + left.character + " and " + right.character);
			node.setLeft(left);
			node.setRight(right);
			return treeText;
		}else{
			int myChar = 0;
			try{
			myChar = Integer.valueOf(myText.substring(1));
			}catch(NumberFormatException e){
				System.err.println(myText + ", " + myText + ", " + myText.substring(1));
				System.exit(0);
			}
			char c = (char)myChar;
			if(readNodeDebug) System.out.println("Creating leaf node with char: " + c);
			node.setCharacter(String.valueOf(c));
			return treeText;
		}
	}
	
	/**
	 * Takes in a binary sequence and returns true if it is inside codeBook
	 * 
	 * @param codeToCheck	sequence to check
	 * @param codeBook		an array of existing valid sequences
	 * @return				true if code in book
	 */
	public static boolean isCode(String codeToCheck, String[] codeBook){
		
		boolean isCodeFlag = false;
		
		for(int i = 0; i < codeBook.length; i++){
			if (codeBook[i] != null){
				if(isCodeFlag)System.out.println("Checking on " + codeBook[i]);
				if(codeBook[i].equals(codeToCheck)){
					if(isCodeFlag)System.out.println(codeToCheck + " is a code!");
					return true;
				}
			}
		}
		
		if(isCodeFlag)System.out.println(codeToCheck + " isn't a code!");
		
		return false;
	}
	
	/**
	 * returns the char value associated with the int value associated with
	 * the provided sequence in the provided String array
	 * To be used in conjunction with isCode()
	 * 
	 * @param codeToCheck 	sequence to retrieve pair
	 * @param codeBook		a String array of existing valid sequences
	 * @return				a char associated with the memory slot ex. codeBook[97] yeilds 'a'
	 */
	public static char retrieveCode(String codeToCheck, String[] codeBook){
		for(int i = 0; i < codeBook.length; i++){
			if(codeBook[i] != null){
				if (codeBook[i].equals(codeToCheck)){
					return (char)i;
				}
			}
		}
		System.err.println("Cant find code in table");
		return 'X';		//who the hell is gonna have an uppercase X in their text
	}
	
	/**
	 * checks to see if the provided node is a leaf. if true, this method retrieves the ASCII
	 * number of node.character and stores node.binary at codeBook[ASCII number]
	 * 
	 * @param node		type Node
	 * @param codeBook	a String array of sequences to be filled up by this method
	 */
	public static void fillTable(Node node, String[] codeBook){
		
		boolean fillTableDebug = false;
		if(node.isLeaf()){
			String character = node.character;
			char chr = character.charAt(0);
			int numASCII = (int)chr;
			codeBook[numASCII] = node.binaryNum;
		}else{
			if(fillTableDebug) System.out.println("Called fillTable on non-leaf");
		}
	}

	/**
	 * Helper function for reading in a continuous sequence of Huffman encrypted text
	 * This method is called over and over again with a decrementing code argument each time
	 * 
	 * @param code		String to look up in codeBook
	 * @param codeBook	a String array of existing valid binary sequences
	 * @return	the value of the longest valid sequence found at the beginning of code
	 */
	public static char decipherCode(String code, String[] codeBook){
		
		char chr = ' ';
		String builder = "";
		while(!isCode(builder, codeBook)){
			if(code.length() == 1){
				builder += code;
				code = "";
				chr = retrieveCode(builder, codeBook);
				return chr;
			}
			if(code.length() > 1){
				builder += code.substring(0, 1);
				code = code.substring(1);
			}
		}
		builder.substring(0, builder.length() - 1);
		chr = retrieveCode(builder, codeBook);
		return chr;
		
	}
	
}