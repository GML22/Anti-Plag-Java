import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

/** This class creates my own implemetation of BST Tree to efficiently store and search for the hashcodes of tokens created in Tokenizer class. My own implematition of BST was needed,
 * because the names of tokens can repeat so usual binary tree was not sufficient. The main idea of my BST Tree is to provide the BST Tree, which at all Nodes has root of anothor BST
 * Tree so when one token is found, and algorithm look for the next it is searching through the BST Tree that start in the current Node, not the moin BST Tree. That provides better 
 * complexity of searching than usual dummy algorithms
**/

public class BetterBST {
	
	private static int PLAG_PARAM = FirstFrame.GetJSliderState(); //user-defined variable of smallest length of sequence of tokens that can be classified as plagiarism
	
	private int max = 0, counter;	
	private Node tokens, Mroot, found, found2;
	private LinkedHashSet<String> used = new LinkedHashSet<String>();
	private ArrayList<Node> nNodes = new ArrayList<Node>();	
	private ArrayList<Node> Compare = new ArrayList<Node>();
	private LinkedHashSet<Node> temp = new LinkedHashSet<Node>();
	
	private static HashSet<String> markedAreas = new HashSet<String>();
	
	public BetterBST(Node str){
	
		this.tokens = str;
		this.Mroot = str;
		this.CreateTree(this.tokens);
	}

	public void CreateTree(Node str){ //algorithm to build a binary search tree
		
		boolean direction = false; //sets direction of inserting nodes to vertical (i.e. left and right nodes are used)		
		this.used.add(this.Mroot.getData()[1]);
		while(str != null ){
			
			Insert(this.Mroot,(str = str.getNext()),direction);	//till the end of node structure
			counter++;
		}
		return;
	}	
	
	public void Insert(Node root, Node nod, boolean direction){	// algorithm to inserting a node
		
        if(root == null|| nod == null) return; //ends algorithm when root or nod is null
		
		Node n = new Node(nod); //creates a copy of node "nod"
	             
        int RootHC = root.getHashCode(), NthHC = n.getHashCode(); //gets a Hashcode reprezentation of strings inside root and n - copy of node
         
        if(RootHC >  NthHC ){ // if hashcode of string in root node is greater then hashcode of string in n node
        	
        	if(!direction){ //check direction of building tree
        		
        		if(root.getLeft() == null){
        			
        			root.setLeft(n); //if left node is empty then it set n node to left of current root  
        			if(!this.used.contains(n.getData()[1])) this.used.add(n.getData()[1]);
        		}
                else Insert(root.getLeft(),n,direction); //else recurrection is used to get deeper in tree
        	}
        	else{
        		
        		if(root.getDown() == null){
        			
        			root.setDown(n);
        			if(!this.used.contains(n.getData()[1])) this.used.add(n.getData()[1]);
        			direction = false; //the direction is setting back to vertical
        		}         
                else Insert(root.getDown(),n,direction); //else recurrection is used to get deeper in tree        	
        	}                
        }
        else if(RootHC < NthHC){
        	
        	if(!direction){
        		
        		if(root.getRight() == null){
        			
        			root.setRight(n); 
        			if(!this.used.contains(n.getData()[1])) this.used.add(n.getData()[1]);
        		}
                else Insert(root.getRight(),n,direction);        		
        	}
        	else{
        		
        		if(root.getUp() == null){ 
        			
        			root.setUp(n);
        			if(!this.used.contains(n.getData()[1])) this.used.add(n.getData()[1]);
        			direction = false;  
        		}    
                else Insert(root.getUp(),n,direction);
        		
        	}                         
        }
        else{
        	
        	if(!direction) direction = true;    // changes direction
        	else direction = false;
        	Insert(root,n.getNext(),direction); 
        }     
	}
		
	public void RecSearch(Node root, Node n, boolean direction){	//recursive algorithm for finding longest equal sequences in root and n, direction marks the direction of actual searching (top-down or right-left)	
		
		if(root == null || n == null) return;		//if the next element of n is not found the the algorithm stops		
		
		int rtHC = root.getHashCode(), nthHC = n.getHashCode();
		
		if(rtHC > nthHC){
			
			if(direction) RecSearch(root.getDown(), n, direction);
			else RecSearch(root.getLeft(), n, direction);
		}
		else if(rtHC < nthHC){
			
			if(direction) RecSearch(root.getUp(), n, direction);
			else RecSearch(root.getRight(),n, direction);
		}
		else{				//if the string is found on the tree algorithm checks if it has identical next elements as root next elements
			
			if(root.getData()[1].equals("IMT") || root.getData()[1].equals("LNM") ||root.getData()[1].equals("LPG") || root.getData()[1].equals("PNM") || root.getData()[1].equals("CED")) return;	
		
			if(!this.temp.contains(n)) this.temp.add(n);			
			this.Compare.addAll(this.temp);						
			this.found2 = root;			
			Node next1 = root,  next2 = this.Compare.get(this.temp.size() -1);
			
			if(next1.getNext() != null) next1 = next1.getNext();
			if(next2.getNext() != null) next2 = next2.getNext();
			
			while(next1 != null && next2 != null && next1.getData()[1].equals(next2.getData()[1])){ //checks in each found node if next elements of n is equal to next elements of root and add eqaul elements to vector Compare
				
				if(next1.getData()[1].equals("IMT") || next1.getData()[1].equals("LNM") ||next1.getData()[1].equals("LPG") || next1.getData()[1].equals("PNM") || root.getData()[1].equals("CED")) break;	
								
				this.found2 = next1;
				this.Compare.add(next2);
				next1 = next1.getNext();
				next2 = next2.getNext();
			}
			
			int sizeC = this.Compare.size(); 
			
			if(sizeC > this.max){				//finding the longest number of equal elements during searching
				
				this.nNodes.clear();
				this.nNodes.addAll(this.Compare);
				this.found = this.found2;
				this.max = sizeC; 
				if(next1 == null || next2 == null) return; //if next1 or next2 is equal to null it means that algorithm reach the end of tokenized sorce code and it can stops
			}
			this.Compare.clear();
			
			if(!direction){
				
				if(root.getUp() != null || root.getDown() != null){
						
					direction = true;  
					RecSearch(root,n.getNext(), direction);
				}
				else if(n.getNext()!=null){ //if n is equal to n->next
					
					if(n.getData()[1].equals(n.getNext().getData()[1])){
					
						direction = true;
						RecSearch(root,n.getNext(), direction);		
					}
				}
				else return;  // null is used to end function in next move
			}
			else{
				
				if(root.getLeft() != null || root.getRight() != null){
					
					direction = false;
					RecSearch(root,n.getNext(), direction);
				}
				else if(n.getNext() != null){ //if n is equal to n->next
					
					if(n.getData()[1].equals(n.getNext().getData()[1])){
					
						direction = false;
						RecSearch(root,n.getNext(), direction);
					}					
				}
				else return; // null is used to end function in next move
			}
		}	
	}	
	
	public double[] Search(Node n){ //the main search function
		
		double[] coords = new double[7]; // first four elements of int array will be coordinates of plag text - coords[0] begin of plag text in first source code1, coords[1] end of plag text in first source code1, coords[4] - size of jump in loop over nodes from first text
		boolean direction = false;
				
		if(!this.used.contains(n.getData()[1])) return coords;// checks if in the BST tree there is a string that we are looking for (string from n node)	
		
		RecSearch(this.Mroot,n,direction); //recursive algorithm used to find the longest sequence of equal tokens
					
		int sizeN = this.nNodes.size();
				
		if(sizeN >= BetterBST.PLAG_PARAM){ //if size of found sequence is greater or equal to user-defined length then the plagiarism is found
		
			Node begin = this.found;
			ArrayList<Node> Roots = new ArrayList<Node>();
			int i = 1;
						
			while(begin != null && sizeN >= i){ //double-checks if everything is OK and adds to Vector Roots previous tokens from this.found
				
				if(this.nNodes.get(sizeN - i).getData()[1].equals(begin.getData()[1])) Roots.add(begin);
				else{
					
					this.nNodes.remove(sizeN - i);
					if(Roots.size() < BetterBST.PLAG_PARAM) Roots.clear();					
				}
				begin = begin.getPrev();
				i++;
			}	
			
			int sizeR = Roots.size();
			
			if(sizeR >= BetterBST.PLAG_PARAM){
				
				String liczba1 = Roots.get(sizeR-1).getData()[0];
				String liczba2 = Roots.get(0).getData()[2];
				
				if(!markedAreas.contains(liczba1) && !markedAreas.contains(liczba2)){	//the place where coordinates of found similar consecutive tokens are stored to be used in GUI to highlight
					coords[5] = sizeN;
					sizeN = sizeR;
					coords[0] = Double.parseDouble((this.nNodes.get(0).getData()[0]));						
					coords[1] = Double.parseDouble(this.nNodes.get(sizeN-1).getData()[2]);												
					coords[2] = Double.parseDouble(liczba1);
					coords[3] = Double.parseDouble(liczba2);
					coords[4] = coords[1] - coords[0];	
					coords[6] = coords[1] - Double.parseDouble((this.nNodes.get(0).getData()[2]));
										
					for(int j=0;j<sizeR;j++){ //markedAreas linkedhashset ensures that if one fragment of source code is found then it wouldn't be found again to do soo all marked coordinates are added to set
						
						markedAreas.add(Roots.get(j).getData()[0]); 
						markedAreas.add(Roots.get(j).getData()[2]);
					}					
				}				
			}			
		}	
			
		this.nNodes.clear();
		this.max = 0;
		this.found = null;
		this.temp.clear();	
		
		return coords;
	}	
	
	public static void SetPlagValue(int val){
		
		PLAG_PARAM = val;
	}
	public int GetCounter(){
		
		return this.counter;
	}
	public static void ClearMarkedAreas(){
		
		markedAreas.clear();
	}
}
