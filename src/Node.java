/**
 * Class that creates key data structure of this application - Node
 * 
 */


public class Node {

	 private String[] data;
     private Node left; //Node connected left
     private Node right; //Node connected right
     private Node next; // next Node
     private Node prev; // previous Node
     private Node up; //Node connected up
     private Node down; //Node connected down
          
 	/**
      * Constructor of Node
      *         
      * @param  Array of data stored by node
      **/
     
     public Node(String[] data){
     	
         this.data = data;
         this.left = null;
         this.right = null;
         this.next = null;
         this.prev = null;
         this.up = null;
         this.down = null;
     }
     
     /**
      * Copy constructor of Node
      *         
      * @param  Node which will be copied
      **/
     
     public Node(Node node){ //copy constructor
    	 
    	 this.data = node.data;
    	 this.down = node.down;
    	 this.left = node.left;
    	 this.next = node.next;
    	 this.prev = node.prev;
    	 this.right = node.right;
    	 this.up = node.up;   	 
     }
     
     /**
      * Set pointer to left Node
      *         
      * @param Node that will be left node of current
      **/
   
	public void setLeft(Node left){
     	
         this.left = left;
     }
	  /**
     * Set pointer to right Node
     *         
     * @param Node that will be right node of current
     **/
     public void setRight(Node right){
     	
         this.right = right;
     }
     /**
      * Get pointer to left Node
      *         
      * @return Node that is left Node of current
      **/
     
     public Node getLeft(){
     	
         return this.left;
     }      
     /**
      * Get pointer to right Node
      *         
      * @return Node that is right Node of current
      **/
     
     public Node getRight(){
     	
         return this.right;
     }
     /**
      * Set pointer to up Node
      *         
      * @param Node that will be up node of current
      **/
     public void setUp(Node up){
      	
         this.up = up;
     }
     /**
      * Get pointer to up Node
      *         
      * @return Node that is up Node of current
      **/
     
     public Node getUp(){
     	
         return this.up;
     }
     /**
      * Set pointer to down Node
      *         
      * @param Node that will be down node of current
      **/
     public void setDown(Node down){
       	
         this.down = down;
     }
     /**
      * Get pointer to down Node
      *         
      * @return Node that is down Node of current
      **/
          
     public Node getDown(){
     	
         return this.down;
     }
     /**
      * Set pointer to next Node
      *         
      * @param Node that will be next node of current
      **/
     
     public void setNext(Node next){
     	
         this.next = next;
     }
     /**
      * Get pointer to next Node
      *         
      * @return Node that is next Node of current
      **/
     
     public Node getNext(){
     	
         return this.next;
     }
     /**
      * Set pointer to prev Node
      *         
      * @param Node that will be prev node of current
      **/
     
     public void setPrev(Node prev){
      	
         this.prev = prev;
     }
     /**
      * Get pointer to previous Node
      *         
      * @return Node that is previous Node of current
      **/
     
     public Node getPrev(){
     	
         return this.prev;
     }
     /**
      * Function that returns Array of strings of data stored by current Node 
      *         
      * @return Array of strings of data stored by Node
      **/
          
     public String[] getData(){     	

    	 return this.data;
     }
     /**
      * Set the token in the node
      *         
      * @param str token that should be stored by Node
      **/
          
     public void setData(String str){
      	
         data[1] = str;
     }
     
     /**
      * Function that returnsthe hashcode of token stored in the Node
      *         
      * @return Hashcode of token stored in the Node
      **/
     
     public int getHashCode(){
      	
         return this.data[1].hashCode();
     }
}