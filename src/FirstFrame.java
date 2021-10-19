import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Class that creates first frame of GUI, it contains also the method to unzip, join, and store in String the source codes of projects choose to check for 
 * plagiarism (see  private class AddProjectsEvent)
 * 
 */

public class FirstFrame extends JFrame  {
	
	private static JFrame frame;
	
	private static ArrayList<ArrayList<String>> Texts = new ArrayList<ArrayList<String>>();
	private static ArrayList<String> WholeCodes = new ArrayList<String>();
	private static ArrayList<String> WCNames = new ArrayList<String>();
	private static ArrayList<DefaultMutableTreeNode> TreeChilds = new  ArrayList<DefaultMutableTreeNode>();
	private static ArrayList<ArrayList<double[]>> pg = new ArrayList<ArrayList<double[]>>();
	private static ArrayList<int[]> ClassBorders = new ArrayList<int[]>();
	private static ArrayList<ArrayList<int[]>> ProjectBorders = new ArrayList<ArrayList<int[]>>();
	
	private static JButton begin;
	private JDialog d;
	private JTextArea textArea;
	private static JSlider plagMinLength;
	private JLabel Instr;
	
	private static JTree tree;
	private static DefaultMutableTreeNode top = new DefaultMutableTreeNode("Selected Files");
	private static DefaultMutableTreeNode category = null;

	private static DefaultMutableTreeNode book = null;
    private static int counter = 0, counter2 = 0;
    private static double MaxCoef = 0.0, MinCoef = 100.0;
	
	public FirstFrame(){
			
		frame = new JFrame("Anti-Plagiarism Application for Java source code");		
		
		BackGroundPanel main = new BackGroundPanel();
		main.setLayout(new BorderLayout());
		main.setBackground(Color.WHITE);
		main.add(new WelcomPanel(),BorderLayout.NORTH);		
		main.add(new SecondPanel(),BorderLayout.EAST);	
						
		frame.add(main);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(950, 700);
		frame.setVisible(true);
	}
	
	private class BackGroundPanel extends JPanel{
		
		private Image img;
		
		public BackGroundPanel(){
			
			setLayout(new BorderLayout());
			try {
				
				this.img = ImageIO.read(new File("images\\background2.png"));
			} 
			catch (IOException e) {
				
				JOptionPane.showMessageDialog(frame, "There is no image \"background.png\" in \"\\images\\\" folder. Please put right image to the folder and restart the application later","Background image not found!",JOptionPane.WARNING_MESSAGE);
				System.exit(0); 
			}
					
		}
		public void paintComponent(Graphics g){
	      
			super.paintComponent(g);
	        g.drawImage(this.img, 0, 0, null);
	    }
	}
		
	private class WelcomPanel extends JPanel{		
		
		public WelcomPanel(){
			
			setOpaque(false); //necessary to have one JPanel on another
			JPanel panel = new JPanel(); 
			panel.setLayout(new GridBagLayout());
			panel.setOpaque(true); //to have ability to change background color
			panel.setBackground(new Color(0,0,0,0)); //transparent color
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			JLabel welcome = new JLabel("<html><big>Welcome to the Anti-Plagiarism App for Java source codes!</big><br><center><br>Press \"Add project\" button to choose zipped projects (min. 2) to analyze for similarity</center></html>");
			welcome.setFont(new Font("Serif", Font.BOLD, 14));
			gbc.gridx = 0;
	        gbc.gridy = 0;
	        gbc.weightx = 1;
	        gbc.weighty = 1;
			panel.add(welcome,gbc);
			
			JButton button = new JButton("Add project");
			button.setFont(new Font(null,Font.BOLD, 14));
			button.setBackground(new Color(139,35,35));
			button.setForeground(Color.WHITE);
			button.setFocusPainted(false);
			button.setSize(10,30);
			button.setToolTipText("Select projects you would like to check for plagiarism");
			gbc.gridx = 1;
	        gbc.gridy = 0;
	        gbc.weightx = 0;
	        gbc.weighty = 0;
	        gbc.anchor = GridBagConstraints.SOUTHEAST;
			panel.add(button,gbc);			
			
			add(panel);	
			
			button.addActionListener(new AddProjectsEvent());
		}
	}	
	private class SecondPanel extends JPanel{
				
		public SecondPanel(){
			
			setOpaque(false); //necessary to have one JPanel on another
			JPanel panel1 = new JPanel();				
			panel1.setLayout(new GridBagLayout());
			panel1.setOpaque(true); //to have ability to change background color
			panel1.setBackground(new Color(0,0,0,0)); //transparent color
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			JLabel label0 = new JLabel("Selected Projects");
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			label0.setFont(new Font(null,Font.BOLD, 15));
			panel1.add(label0,gbc);
											 
			tree = new JTree(top);
			tree.setToolTipText("<html>Single click on project name - see whole project's source code<br>Double click on project name - see all class with its source codes<br>Keyboard Navigation - navigate using arrows</html>");
			tree.setEnabled(false);
			tree.setFont(new Font(null,Font.BOLD, 12));
			tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);    
			tree.addTreeSelectionListener(new TreeSelectionListener() {
				    
				 public void valueChanged(TreeSelectionEvent e) {
				    	
					 DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					 if (node == null) return;					 
					 
					 String longMessage = "", title ="";
				     int pos, nDepth = node.getDepth();
					 					 
					 if(nDepth == 1){
						 
						 pos = Integer.parseInt(node.getUserObject().toString().substring(0,node.getUserObject().toString().indexOf('.')));
						 longMessage = WholeCodes.get(pos - 1);
						 title = "Whole source code of: " + node.getUserObject().toString().substring(node.getUserObject().toString().indexOf('.') + 1);
					 }
					 else if(nDepth == 0) {
						 
						 int start = node.getUserObject().toString().indexOf('.');
						 pos = Integer.parseInt(node.getUserObject().toString().substring(0,start));
						 int pos2 = Integer.parseInt(node.getUserObject().toString().substring(start + 1,node.getUserObject().toString().indexOf('.',start+1)));
						 longMessage = Texts.get(pos-1).get(pos2-1);
						 title = "Source code of class: " + node.getUserObject().toString().substring(node.getUserObject().toString().indexOf('.', start+1) + 1);
					 }
					 else if (nDepth == 2) {
						 
						 if(FindOpenDialogs() != null) FindOpenDialogs().dispose();
						 return;
					 }
					 					 
					if(FindOpenDialogs() == null){  //checking if any JDialog is open, if null then no JDialog is open		
					
				 		d = new JDialog(frame,title);
				 		textArea = new JTextArea();
					    textArea.setEditable(false);
						textArea.setText(longMessage);
						textArea.setCaretPosition(0);
						JScrollPane scrollPane = new JScrollPane(textArea);					 
						scrollPane.setPreferredSize(new Dimension(380,637)); //wrap scrollpane around message dialog
						JOptionPane pane = new JOptionPane(scrollPane,JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null); //default_option and next options provides that there won't be ok button
						 					 
						d.getContentPane().add(pane);
						d.setModal(false); //ensures that the window of JDialog will show without selectiong it
						d.pack();
						d.setLocation(950,0);
						d.setVisible(true);
						frame.toFront(); // brings to front
						tree.requestFocus(); //focus selection on Jtree window
					}
					else{ //if JDialog is open then change its innerText and title
										    
						textArea.setText(longMessage);
						textArea.setCaretPosition(0);						
						d.setTitle(title);					
					}
				 }
			});
									
			JScrollPane treeView = new JScrollPane(tree);
			treeView.setPreferredSize(new Dimension(370,300));			
			gbc.gridx = 1;
	        gbc.gridy = 1;
	        gbc.weightx = 1;
	        gbc.weighty = 1;
			panel1.add(treeView,gbc);			
			
			begin = new JButton("Start the similarity test");
			begin.setFont(new Font(null,Font.BOLD, 12));
			begin.setBackground(new Color(139,35,35));
			begin.setForeground(Color.WHITE);
			begin.setFocusPainted(false);			
			begin.addActionListener(null);
			begin.setToolTipText("Start the test for similarity of all selected projects");
			gbc.gridx = 1;
	        gbc.gridy = 3;	
	        gbc.weightx = 0;
	        gbc.weighty = 0;
	        gbc.anchor = GridBagConstraints.SOUTHEAST;
	        gbc.insets = new Insets(5,0,0,0);
			panel1.add(begin,gbc);
			gbc.insets = new Insets(0,0,0,0);
			
			begin.getModel().addActionListener(new RunPlagAlgEvent());
			
			plagMinLength = new JSlider(JSlider.HORIZONTAL,2,18,10);
						
			JPanel panelSlid = new JPanel();
			panelSlid.setLayout(new BorderLayout());
			panelSlid.setOpaque(true);
			panelSlid.setBackground(new Color(0,0,0,0));			
			
			JLabel slidTitle = new JLabel("Minimum number of words for plagiarism");
			slidTitle.setOpaque(true);
			slidTitle.setBackground(new Color(0,0,0,0));
			slidTitle.setFont(new Font(null,Font.BOLD, 10));
			panelSlid.add(slidTitle,BorderLayout.NORTH);
						
			plagMinLength.setMajorTickSpacing(10);
			plagMinLength.setMinorTickSpacing(1);
			plagMinLength.setToolTipText("<html>Specify the minimum number of consecutive words to determine plagiarism<br>Ten words is default and adviced, because of construction of algorithm</html>");
			plagMinLength.setPaintTicks(true);
			plagMinLength.setPaintLabels(true);
			plagMinLength.getModel().setValueIsAdjusting(true);	
			
			Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
			JLabel nr1 = new JLabel("2");
			nr1.setFont(new Font(null,Font.BOLD, 10));
			labelTable.put(2 , nr1);
			JLabel nr2 = new JLabel("6");
			nr2.setFont(new Font(null,Font.BOLD, 10));
			labelTable.put(6 , nr2);
			JLabel nr3 = new JLabel("10");
			nr3.setFont(new Font(null,Font.BOLD, 10));
			labelTable.put(10, nr3);
			JLabel nr4 = new JLabel("14");
			nr4.setFont(new Font(null,Font.BOLD, 10));
			labelTable.put(14, nr4);
			JLabel nr5 = new JLabel("18");
			nr5.setFont(new Font(null,Font.BOLD, 10));
			labelTable.put(18, nr5);
			plagMinLength.setLabelTable(labelTable);
			gbc.gridx = 1;
	        gbc.gridy = 3;	 
	        gbc.anchor = GridBagConstraints.SOUTHWEST;
	        panelSlid.add(plagMinLength,BorderLayout.SOUTH);
			panel1.add(panelSlid,gbc);
			
			JButton clearAll = new JButton("Clear All");
			clearAll.setFont(new Font(null,Font.BOLD, 12));
			clearAll.setBackground(new Color(139,35,35));
			clearAll.setForeground(Color.WHITE);
			clearAll.setFocusPainted(false);
			clearAll.setToolTipText("Delete all previous selected projects");
			gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.weightx = 0;
	        gbc.weighty = 0;
	        gbc.anchor = GridBagConstraints.NORTHEAST;
			panel1.add(clearAll,gbc);
			
			clearAll.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent event){	ClearAll();	}				
			});	
			
			Instr = new JLabel("<html>You can click at the name of the project<br> to inspect its source code and classes<br>You can also navigate using arrows!</html>");
			Instr.setForeground(Color.GRAY);
			Instr.setVisible(false);
			gbc.gridx = 0;
			gbc.gridy = 1;	
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.NORTHEAST;
			gbc.insets = new Insets(50,0,0,5);
			panel1.add(Instr,gbc);
			gbc.insets = new Insets(0,0,0,0);			
			
			JButton cncl = new JButton("Quit");
			cncl.setBackground(new Color(139,35,35));
			cncl.setFont(new Font(null,Font.BOLD, 16));
			cncl.setForeground(Color.WHITE);
			cncl.setFocusPainted(false);
			cncl.setToolTipText("Quit the application");
			gbc.gridx = 1;
			gbc.gridy = 4;	
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.SOUTHEAST;
			gbc.insets = new Insets(140,0,0,0);
			panel1.add(cncl,gbc);
			
			cncl.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent event){
					
					Window[] windows = Window.getWindows();
					    
				    if( windows != null ) { 
				    	
				       for( Window w : windows ) {
				        	
				            w.dispose();
				       }
				   }
				}
			});
			
			add(panel1);			
		}		
	}
	
	private class RunPlagAlgEvent implements ActionListener{ //the class for runinig the plagiarism algorithm
		
		private ArrayList<String> str = new ArrayList<String>();	
		private ArrayList<Tokenizer> tokenized = new ArrayList<Tokenizer>();
		private ArrayList<Double> tokenCoef = new ArrayList<Double>();
		private ArrayList<BetterBST> bsf = new ArrayList<BetterBST>();
		private Vector<Vector<Object>> data = new  Vector<Vector<Object>>(); //this vector will store the data of table
		private String path1, path2;
		private double minSize, tempMin = Double.POSITIVE_INFINITY;
		private int minNr = 0, licznik22 = 0, MaximumNr = 0, MinimumNr = 0, sum = 0;
		private double tokCoef1, tokCoef2;		
		
		public void actionPerformed(ActionEvent event) {
			
			 if(counter < 2){
				
				 int numb = 2 - counter;
				 JOptionPane.showMessageDialog(frame, "You've added to few projects, add " + numb + " more to be able to start the similarity test","To few projects",JOptionPane.WARNING_MESSAGE);
				 return;
			 }			 
			
			if(FindOpenDialogs() != null) FindOpenDialogs().dispose(); //closing innecesary windows
			begin.setEnabled(false);
			int plVal = plagMinLength.getValue();			 
			BetterBST.SetPlagValue(plVal); //refreshing the minimum length of tokenized text in the class BST
			 
			int ChildNr = tree.getModel().getChildCount(top);
						 
			for(int i=0; i<ChildNr; i++){				
				
				TreeChilds.add((DefaultMutableTreeNode) tree.getModel().getChild(top, i));
			}
			 
							
				int Fsize = WholeCodes.size();
				
				for(int i=0;i<Fsize;i++){				
					
					String str0 = WholeCodes.get(i).toLowerCase();
					this.str.add(str0);					
					Tokenizer 	tok0 = new Tokenizer(str0);					
					this.tokenCoef.add(tok0.GetTokenCoef());
					this.tokenized.add(tok0);
					this.minSize = tok0.GetNodeCounter();
					if(this.minSize < this.tempMin){ this.minNr = i; this.tempMin = this.minSize; }																
				}
				
				for(int i=0;i<Fsize;i++){
					
					if(i != this.minNr){	
						
						BetterBST Bnode2 = new BetterBST(this.tokenized.get(i).GetTokenizeText());
						this.bsf.add(Bnode2); 
					}
					else this.bsf.add(null);			
				}
				
				for(int i=0;i<Fsize;i++){  //the loop that compare each file with each to find similar fragemnts of code
					
					this.path1 = WCNames.get(i);
					this.tokCoef1 = tokenCoef.get(i);
										
					if(this.bsf.get(i) != null){										
										
						for(int j=i+1;j<Fsize;j++){
							
							if(j == this.minNr) continue; //to not compare with min element, because it has to be treated differently than others
							
							Vector<Object> row = new Vector<Object>();
							this.path2 = WCNames.get(j);
							this.tokCoef2 = tokenCoef.get(j);
							
							if(this.tokenized.get(i).GetNodeCounter() >= this.tokenized.get(j).GetNodeCounter()) {
															
								PlagAlgorithm alg = new PlagAlgorithm(this.bsf.get(i),tokenized.get(j),str.get(j));
								pg.add(alg.GetArray());
								ArrayList<double[]> wynik = new ArrayList<double[]>();
								wynik = pg.get(licznik22++);
								row.add(String.valueOf(licznik22));
								row.add(this.path2);
								row.add(String.valueOf(String.format("%.2f",this.tokCoef2) + "%"));
								row.add(this.path1);
								row.add(String.valueOf(String.format("%.2f",this.tokCoef1) + "%"));										
								String coef = String.valueOf(String.format("%.2f",wynik.get(wynik.size()-1)[0])) + "%";									
								row.add(coef);
								sum+=wynik.get(wynik.size()-1)[0];
								if(wynik.get(wynik.size()-1)[0] < MinCoef){ MinCoef = Math.round(wynik.get(wynik.size()-1)[0]*100)/(double)100; MinimumNr = licznik22;}
								if(wynik.get(wynik.size()-1)[0] > MaxCoef){ MaxCoef = Math.round(wynik.get(wynik.size()-1)[0]*100)/(double)100; MaximumNr = licznik22;}
								data.add(row);								
							}				
							else{
								
								PlagAlgorithm alg = new PlagAlgorithm(this.bsf.get(j),tokenized.get(i),str.get(i));
								pg.add(alg.GetArray());
								ArrayList<double[]> wynik = new ArrayList<double[]>();
								wynik = pg.get(licznik22++);
								row.add(String.valueOf(licznik22));									
								row.add(this.path1);
								row.add(String.valueOf(String.format("%.2f",this.tokCoef1) + "%"));
								row.add(this.path2);
								row.add(String.valueOf(String.format("%.2f",this.tokCoef2) + "%"));										
								String coef = String.valueOf(String.format("%.2f",wynik.get(wynik.size()-1)[0])) + "%";									
								row.add(coef);
								sum+=wynik.get(wynik.size()-1)[0];
								if(wynik.get(wynik.size()-1)[0] < MinCoef){ MinCoef = Math.round(wynik.get(wynik.size()-1)[0]*100)/(double)100; MinimumNr = licznik22;}
								if(wynik.get(wynik.size()-1)[0] > MaxCoef){ MaxCoef = Math.round(wynik.get(wynik.size()-1)[0]*100)/(double)100; MaximumNr = licznik22;}
								data.add(row);								
							}
						}
					}
					else{
											
						for(int j=0;j<Fsize;j++){
							
							if(j == this.minNr) continue;									

							Vector<Object> row = new Vector<Object>();
							this.path2 = WCNames.get(j);
							this.tokCoef2 = tokenCoef.get(j);
							PlagAlgorithm alg = new PlagAlgorithm(this.bsf.get(j),tokenized.get(i),str.get(i));
							pg.add(alg.GetArray());
							ArrayList<double[]> wynik = new ArrayList<double[]>();
							wynik = pg.get(licznik22++);
							row.add(String.valueOf(licznik22));															
							row.add(this.path1);
							row.add(String.valueOf(String.format("%.2f",this.tokCoef1) + "%"));
							row.add(this.path2);
							row.add(String.valueOf(String.format("%.2f",this.tokCoef2) + "%"));							
							String coef = String.valueOf(String.format("%.2f",wynik.get(wynik.size()-1)[0])) + "%";									
							row.add(coef);
							sum+=Math.round(wynik.get(wynik.size()-1)[0]*100)/(double)100;
							if(wynik.get(wynik.size()-1)[0] <= MinCoef){ MinCoef = Math.round(wynik.get(wynik.size()-1)[0]*100)/(double)100; MinimumNr = licznik22;}
							if(wynik.get(wynik.size()-1)[0] >= MaxCoef){ MaxCoef = Math.round(wynik.get(wynik.size()-1)[0]*100)/(double)100; MaximumNr = licznik22;}
							data.add(row);				
						}					
					}
				}
				SecondFrame.AddList(data);
				SecondFrame.SetSumm("Statistics of all comparisons:\nNumber of comparisions: " + licznik22 + "\nMax similarity coefficient: " + MaxCoef + "\nat comparision number: " + MaximumNr + "\nMin similarity coefficient: " + MinCoef + "\nat comparision number: " + MinimumNr + "\nAverage similiraty coeficient: " +  Math.round((sum/(double)licznik22)*100)/(double)100);
				data.removeAllElements();
				WCNames.clear();
				tokenCoef.clear();;
				tokenized.clear();
				bsf.clear();
				str.clear();
				licznik22 = 0;
				sum = 0;
					
			SecondFrame.setFrame2Visible(); //sets second frame visible
			frame.setVisible(false);
		}		
	}	
	
	private class AddProjectsEvent implements ActionListener{ //the class for adding the project and save its surce codes to String
		
		JFileChooser chooser;
		 
	    public AddProjectsEvent() { chooser = new JFileChooser("."); } // the constructor is created to rember the path to last choosed folder
		
		public void actionPerformed(ActionEvent event) {			
									
        	FileNameExtensionFilter filter = new FileNameExtensionFilter("ZIP files", "zip");	    // file chooser    	 
	    	chooser.setFileFilter(filter);	 
	    	chooser.setMultiSelectionEnabled(true);
	    	chooser.setAcceptAllFileFilterUsed(false);
	    	int returnVal = chooser.showOpenDialog(null);
	    	int InnerCounter;
	    	 
	    	if(returnVal != JFileChooser.APPROVE_OPTION) return;
	    	
    		final File[] files = chooser.getSelectedFiles();   // get the list of files  			    
        		        	 
        	 for(File f: files){
        		 
        		 tree.setEnabled(true);
        		String name = f.getName(); 
        		InnerCounter = 0;
    			
    		 	ZipFile FileZ = null;
    		 	StringBuffer TextBuild = new StringBuffer();
    		 	
				try {
					
					FileZ = new ZipFile(f.getPath()); //opens the zip file
				} 
				catch (IOException e) {
					
					JOptionPane.showMessageDialog(frame, "There are no zip files in given localisation! Please try again to select zip files.","No zip files in given localisation!",JOptionPane.WARNING_MESSAGE);
				}					
				
    		    Enumeration<? extends ZipEntry> ent = FileZ.entries();   //definition of entries in zip object
    		   
    		    ArrayList<String> TempTexts = new ArrayList<String>();

    		    while(ent.hasMoreElements()){ //loop which is searching for entries containing specific extension - the parent folder to java codes in Eclipse
    		    	
    		        ZipEntry entry = ent.nextElement();
    		        String StrEntry = entry.toString();
    		           		        
    		        if(StrEntry.matches(".*(.*\\.java$).*") == false) continue; //searching by regular expressions files that ends with .java    	
    		        
    		        if (InnerCounter == 0) createNodes(name.substring(0, name.length() - 4), 1,0); 
    		        
		        	InnerCounter++;
		        	
		        	int[] border = new int[2];
		        	border[0] = TextBuild.toString().length();
		        			        	
		        	createNodes(StrEntry.substring(StrEntry.lastIndexOf("/") + 1, StrEntry.length() - 5), 2,InnerCounter); 
		        	
		        	String TempText = "";
		    		boolean flag = false;		   
		    		StringBuffer TempBuild = new StringBuffer();
		    		BufferedReader BuffR = null;
		    		
					try {
						
						BuffR = new BufferedReader(new InputStreamReader(FileZ.getInputStream(entry),"UTF-8"));
					} 
					catch (IOException e1) {
					
						JOptionPane.showMessageDialog(frame, "BufferReader couldn't fined zip files in given localisation. Please try again to select zip files","No zip  files in given localisation!",JOptionPane.WARNING_MESSAGE);
					}			    
    			    		
					try {
						while((TempText = BuffR.readLine()) != null){ //loop for getting rid off comments
							
							int ind1 = TempText.indexOf("/*");
							int ind2 = TempText.indexOf("//");
							int ind3 = TempText.indexOf("*/");
							int id1=0, bound, bracketCount = 0;;
							
							if(ind1 >= 0) bound = ind1;
							else if(ind3 >= 0) bound = ind3;
							else bound = 0;					
							
							if (bound > 0){
							
								while(TempText.substring(id1,bound).contains("\"")) {		//used to count the number of " before comment sign is not in String to be sure that comment sign is not in String						
										
										id1 += TempText.substring(id1).indexOf("\"") + 1;
										bracketCount++;											
								}
							}
				    		
				    		if (ind1 >= 0 && bracketCount%2 == 0 ) flag = true; // all down conditions ensures to cut down the comments from the source code 
				    		
				    		if (ind2 > 0 && flag == false) TempBuild.append(TempText.subSequence(0,ind2) + "\n");
				    		else if (ind2 == 0 && flag == false);
				    		else if (flag == false && ind2 == -1) TempBuild.append(TempText + "\n");
				    		
				    		if (ind3 >= 0 && bracketCount%2 == 0)	flag = false;	    				    		    			    		
				    	}
					} catch (IOException e) {
						
						JOptionPane.showMessageDialog(frame, "BufferReader couldn't fined zip files in given localisation. Please try again to select zip files","No zip  files in given localisation!",JOptionPane.WARNING_MESSAGE);
					}	
					
					TempBuild.append("\n\nCLASS_END;\n\n");
					
					String str0 = TempBuild.toString();
					
					TempTexts.add(str0); 
					TextBuild.append(str0);
					border[1] = TextBuild.toString().length() - 1;
					ClassBorders.add(border);
    		    } 
    		    
    		    if(InnerCounter > 0){
    		    	
    		    	String finalStr = TextBuild.toString();
    		    	WholeCodes.add(finalStr); //adds to the Arraylist whole source codes of project (without comments)
    		    	ArrayList<int[]> tempik = new ArrayList<int[]>();
    		    	tempik.addAll(ClassBorders);    		    	
    		    	ProjectBorders.add(tempik);
    		    	ClassBorders.clear();    		    	
        		    Texts.add(TempTexts); //adds to the ArrayList ArrayList of class source codes
        			WCNames.add(name.substring(0, name.length() - 4)); //adds to the ArrayList the name of file without .zip
    		    }
    		    else{
    		    	
    				JOptionPane.showMessageDialog(frame, "There is no java source codes in selected .zip files","No java source codes in projects ",JOptionPane.WARNING_MESSAGE);
    				return;
    		    }    		    
        	}
        	         	 
        	if(counter >= 2) begin.setBackground(new Color(0,139,0));
 	    	tree.requestFocus(); //focus selection on Jtree window
 	    	tree.setSelectionPath( new TreePath(top)); //select top root
 	    	Instr.setVisible(true);   	    	
		}		
	}
	
	private static void createNodes(String str, int flag, int sc) {
			    
	    if(flag == 1){
	    	
	    	counter++;	    	
	    	category = new DefaultMutableTreeNode(counter +  ". " + str);
		    top.add(category);		    
	    }
	    else if(flag == 2){
	    	
	    	book = new DefaultMutableTreeNode(counter + "." + sc + ". " + str);
	    	category.add(book);
	    	counter2++;
	    }
	    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();	  
	    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
	    root.setUserObject("Selected Files (" + counter + " projects, " + counter2 + " source codes)");
	    model.nodeChanged(root);	    
	    model.reload(); //necesary to reload tree view after inserting a node to tree
	}
	
	public static void ClearAll(){ //loop for clearing all data structurs and other important variables
		
		if(counter > 0){
			
			//clearing tree
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		    root.setUserObject("Selected Files");
		    model.nodeChanged(root);
			top.removeAllChildren();						
			model.reload();	
			tree.setEnabled(false);
			//clearing sets
			Texts.clear();
			WholeCodes.clear();	
			//clearing counters
			counter = 0;
			counter2 = 0;
			//closing innecesary windows
			if(FindOpenDialogs() != null) FindOpenDialogs().dispose();
			//changeing color of start button to normal
			begin.setBackground(new Color(139,35,35));
			// setting JSlider to default
			plagMinLength.setValue(10);
			//clearing the childs of tree
			TreeChilds.clear();
			pg.clear();
			MaxCoef = 0;
			MinCoef = 100;
		}
		else{
			
			JOptionPane.showMessageDialog(frame, "You've added no projects!","Nothing to clear",JOptionPane.WARNING_MESSAGE);					 
		}
	}
	
	public static JDialog FindOpenDialogs() //check if Dialog not modeal window is open
	{
	    Window[] windows = Window.getWindows();
	    
	    if( windows != null ) { 
	    	
	        for( Window w : windows ) {
	        	
	            if( w.isShowing() && w instanceof JDialog && !((JDialog)w).isModal()) return (JDialog) w;
	        }
	    }
	    return null;
	}
	public static int GetJSliderState(){
		
		return plagMinLength.getValue();
	}
	public static ArrayList<DefaultMutableTreeNode> getTree(){
		
		return TreeChilds;
	}
	public static ArrayList<String> GetWholeCode(){
		
		return WholeCodes;
	}
	public static void SetBeginEnabled(){
		
		begin.setEnabled(true);
	}
	public static void setFrame1Visible(){
		
		frame.setVisible(true);
	}
	public static ArrayList<ArrayList<double[]>> GetPlagArrL(){
		
		return pg;		
	}	
	public static ArrayList<ArrayList<int[]>> GetProjectBorders(){
		
		return ProjectBorders;
	}
	public static ArrayList<ArrayList<String>> GetTexts(){
		
		return Texts;
	}	
}

