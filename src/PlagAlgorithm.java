import java.util.ArrayList;

/** This class takes tokenized source codes and BetterBST object and search each tokenized word in BetterBST Tree for consecutive tokens. BetterBST returns the 
 * coordinates of similar framgments in source code
**/

public class PlagAlgorithm {
	
	private ArrayList<double[]> pg = new ArrayList<double[]>();
	private BetterBST bsf;
	private String str;
	
	public PlagAlgorithm(BetterBST bsfN, Tokenizer tk2, String str){
		
		this.bsf = bsfN;
		this.str = str;
		this.pg = Alg(this.bsf,tk2, this.str);		
	}
	
	public ArrayList<double[]> Alg(BetterBST bsf, Tokenizer tk2, String str){
		
		ArrayList<double[]> result = new ArrayList<double[]>();	
		
		int counter = 0, size, strLen = str.length(),sizer = 0; 
										
		Node itr = tk2.GetTokenizeText(); //get root Node of tokenized Node structure
		size = tk2.GetNodeCounter();
		
		BetterBST.ClearMarkedAreas(); //clears marked areas in BetterBST
						
		while(itr != null){	//loop throught all tokenized Nodes 
					
			 // to ensure that import and package wouldn't be compare they are not put to search in BetterBST Tree
			while(itr.getData()[1].equals("IMT") || itr.getData()[1].equals("LPG") || itr.getData()[1].equals("PNM") || itr.getData()[1].equals("LNM") || itr.getData()[1].equals("CED")){
								
				while(!itr.getData()[1].equals("SEM")) itr = itr.getNext();
				itr = itr.getNext();
				if(itr == null){
					
					double wsp =(double)100 * (double) counter/sizer; // coefficient is determined for smaller source code
					
					double[] coef = new double[7];
					
					coef[0] = wsp;
					result.add(coef);
					return result;
				}
			}
			
			sizer += (Double.parseDouble(itr.getData()[2]) - Double.parseDouble(itr.getData()[0]));
			
			double[] coords = bsf.Search(itr);  // put the token into the BetterBST Tree to get coordinates of plagiarised words
			int i = 1;			
												
			if (coords[1] != 0.0){				
				
				result.add(coords); //coords[1] must be different than 0, to avoid adding coordinates with all zeros to result array
				counter += coords[4];
				sizer += coords[6];
				while(i < coords[5] && itr !=null){ //move text token by length of founded one
					
					itr = itr.getNext();					
					i++;					
				}				
			}		
			if(itr != null) itr = itr.getNext();			
		}		
				
		BetterBST.ClearMarkedAreas(); //clears marked areas in BetterBST
		
		double wsp =(double)100 * (double)counter/sizer; // coefficient is determined for smaller source code (number of similar tokens in smaller source code over number of all tokens in smaller source code)
		
		double[] coef = new double[7];
		
		coef[0] = wsp;
		result.add(coef);
		
		return result;		
	}
	
	public ArrayList<double[]> GetArray(){
		
		return this.pg;		//returns the array of coordianter of similar framgments in two source codes
	}

}
