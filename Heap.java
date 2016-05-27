
public class Heap {
	
	  public static int factorial(int n) {
	       if (n == 0) {
	           return 1;
	       } else {
	           return n * factorial(n - 1);
	       }
	   }
	
	public static void main(String[] args){
//		System.out.println(factorial(255));
		Heap newHeap = new Heap(255);
		newHeap.insert(new Node("a", 5));
//		newHeap.print();
		newHeap.insert(new Node("b", 55));
//		newHeap.print();
		newHeap.insert(new Node("c", 555));
//		newHeap.print();
		newHeap.insert(new Node("d", 56));
//		newHeap.print();
		newHeap.insert(new Node("e", 6));
//		newHeap.print();
		
		newHeap.combine();
		
		newHeap.printTree();
		
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
    
    private void printTree(){
    	printTree(Heap[1]);
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

	private void combine() {
		while(this.size != 1){
			Node tmp1 = this.removemin();
			Node tmp2 = this.removemin();
			int sum = tmp1.frequency + tmp2.frequency;
			this.insert(new Node(sum, tmp1, tmp2));
		}
	}
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

}


