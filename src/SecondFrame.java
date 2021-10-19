import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Vector;

import javax.swing.*;
import javax.swing.text.*;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * Class that creates second frame, no algoritmic operations are stored here - just sipmle GUI staff
 * 
 */

public class SecondFrame {

	private static JFrame frame2;
	private BackGroundPanel main2;
	private static ArrayList<DefaultMutableTreeNode> TreeChilds = new  ArrayList<DefaultMutableTreeNode>();
	private static ArrayList<ArrayList<String>> Texts = new ArrayList<ArrayList<String>>();
	private static ArrayList<String> tempWC = new ArrayList<String>();
	private static ArrayList<ArrayList<double[]>> TempPlag = new ArrayList<ArrayList<double[]>>();	
	private static ArrayList<ArrayList<int[]>> ProjectBorders = new ArrayList<ArrayList<int[]>>();
	private static ArrayList<double[]> classesPlag = new ArrayList<double[]>();
	
	private static JTextArea label2;
	private static JTable table;
	private static DefaultTableModel tableModel;
	private static Vector<Vector<Object>> data;
	private static Vector<String> header;
	
	private JTextField label3;
	private JTextArea text1;
	private JTree first;
	private JTree second;
	
	private JTextField label4;
	private JTextArea text2;
	
	private int mainCounter = -1;
	private static int Row = -1;
	private boolean levelflag;	
	
	public SecondFrame(){		
		
		frame2 = new JFrame("Results of test");	
		GridBagConstraints gbc = new GridBagConstraints();		
		
		main2 = new BackGroundPanel();	
		main2.setLayout(new BorderLayout());
		main2.setBackground(Color.WHITE);
		main2.add(new Panel0(), BorderLayout.NORTH);
		main2.add(new Panel1(), BorderLayout.WEST);		
		main2.add(new Panel2(), BorderLayout.CENTER);	
		main2.add(new Panel3(), BorderLayout.EAST);
		
		frame2.add(main2);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setSize(1050, 710);
		frame2.setLocationRelativeTo(null);
		frame2.setVisible(false);
						
		KeyStroke rightArrowKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false);	
		
		Action rightArAction = new AbstractAction() {
			
	         public void actionPerformed(ActionEvent e) {
	        	 
	        	 NextHandler();
	         }
	    };
	    
		frame2.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(rightArrowKeyStroke, "RIGHT_AR");
		frame2.getRootPane().getActionMap().put("RIGHT_AR", rightArAction);		
			
		KeyStroke leftArrowKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false);	 //adds keyboard navigation in frame - > left and right arrow	

		Action leftArAction = new AbstractAction() {
			
	         public void actionPerformed(ActionEvent e){
	        	 
	            PrevHandler();
	         }
	    };	    

		frame2.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(leftArrowKeyStroke, "LEFT_AR");
		frame2.getRootPane().getActionMap().put("LEFT_AR", leftArAction);
	    
	    KeyStroke UpArrowKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false);
		
		Action UpArAction = new AbstractAction() {
			
	         public void actionPerformed(ActionEvent e) {
	        	 
	        	if (Row > 0){
	        		
	        		table.setRowSelectionInterval(Row-1, Row-1);
	        		table.scrollRectToVisible(new Rectangle(table.getCellRect(Row-1, 0, true)));
	        	}
	        	else if(Row == 0){
	        		
	        		table.setRowSelectionInterval(Row, Row);
	        		table.scrollRectToVisible(new Rectangle(table.getCellRect(Row, 0, true)));
	        	}	        	
	         }
	    };
	    
		frame2.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(UpArrowKeyStroke, "UP_AR");
		frame2.getRootPane().getActionMap().put("UP_AR", UpArAction);
		
		KeyStroke DownArrowKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false);
		
		Action DownArAction = new AbstractAction() {
			
	         public void actionPerformed(ActionEvent e) {
	        	 
	        	 if(Row == table.getRowCount() -1){
	        		 
	        		 table.setRowSelectionInterval(Row, Row);
	        		 table.scrollRectToVisible(new Rectangle(table.getCellRect(Row, 0, true)));
	        	 }
	        	 else if(Row == - 1) table.setRowSelectionInterval(0, 0);
	        	 else{
	        		 
	        		table.setRowSelectionInterval(Row + 1, Row + 1);
	        		table.scrollRectToVisible(new Rectangle(table.getCellRect(Row + 1, 0, true)));
	        	 }
	         }
	    };
		
		frame2.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(DownArrowKeyStroke, "DOWN_AR");
		frame2.getRootPane().getActionMap().put("DOWN_AR", DownArAction);	
	}	
	
	private class BackGroundPanel extends JPanel{
		
		private Image img;
		
		public BackGroundPanel(){
			
			try {
				
				this.img = ImageIO.read(new File("images\\background2.png"));
			} 
			catch (IOException e) {
				
				JOptionPane.showMessageDialog(frame2, "There is no image \"background.png\" in \"\\images\\\" folder. Please put right image to the folder and restart the application later","Background image not found!",JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}					
		}
		public void paintComponent(Graphics g){
	      
			super.paintComponent(g);
	        g.drawImage(this.img, 0, 0, null);
	    }
	}
	
	private class Panel0 extends JPanel{	
		
		public Panel0(){
			
			setOpaque(false);
			JPanel panel0 = new JPanel();
			panel0.setLayout(new GridBagLayout());
			panel0.setOpaque(true); //to have ability to change background color
			panel0.setBackground(new Color(0,0,0,0)); //transparent color	
						
			GridBagConstraints gbc = new GridBagConstraints();			

			JButton CAll = new JButton("Go back to the first frame");
			CAll.setBackground(new Color(139,35,35));
			CAll.setFont(new Font(null,Font.BOLD, 13));
			CAll.setForeground(Color.WHITE);
			CAll.setFocusPainted(false);
			CAll.setToolTipText("Move back to the beginning of the application");
			gbc.gridx = 0;
			gbc.gridy = 0;	
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			panel0.add(CAll,gbc);
			
			CAll.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent event){
					
					ClearAll2();
					FirstFrame.SetBeginEnabled();					
					FirstFrame.ClearAll();
					frame2.dispose();
					FirstFrame.setFrame1Visible();
				}
			});	
			
			Image img1 = null;
			try {
				
				img1 = ImageIO.read((new File("images//up_arrow.png")));
			} 
			catch (IOException e1) {
				
				JOptionPane.showMessageDialog(frame2, "There no \"up_arrow\" image in \"images\" folder","No such image!",JOptionPane.WARNING_MESSAGE);
			}

			ImageIcon icon = new ImageIcon(img1);
			JLabel imageLabel = new JLabel("", icon, JLabel.CENTER);
			imageLabel.setToolTipText("Click up arrow button on your keyboard to select row above");		
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.insets = new Insets(0,20,0,0);
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			panel0.add(imageLabel,gbc);
			
			Image img2 = null;
			try {
				
				img2 = ImageIO.read((new File("images//down_arrow.png")));
			} 
			catch (IOException e1) {
				
				JOptionPane.showMessageDialog(frame2, "There no \"down_arrow\" image in \"images\" folder","No such image!",JOptionPane.WARNING_MESSAGE);
			}
			
			ImageIcon icon2 = new ImageIcon(img2);
			JLabel imageLabel2 = new JLabel("", icon2, JLabel.CENTER);
			imageLabel2.setToolTipText("Click down arrow button on your keyboard to select row below");
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.insets = new Insets(0,20,0,0);
			gbc.anchor = GridBagConstraints.LAST_LINE_START;
			panel0.add(imageLabel2,gbc);
			
			JLabel label2a = new JLabel("InfoBox");
			label2a.setFont(new Font(null,Font.BOLD,15));
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.PAGE_END;
			panel0.add(label2a,gbc);
			
			label2 = new JTextArea("");
			label2.setEditable(false);
			label2.setBackground(new Color(51,102,153));
			label2.setFont(new Font(null,Font.BOLD,12));
			label2.setForeground(Color.WHITE);
			label2.setToolTipText("InfoBox shows informations about comparisons");
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.insets = new Insets(0,20,0,0);
			gbc.anchor = GridBagConstraints.LINE_END;
			panel0.add(label2,gbc);
						
			JLabel label1 = new JLabel("<html><big>Results of comparisons</big></html>");
			label1.setFont(new Font(null,Font.BOLD,14));
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			panel0.add(label1,gbc);
						
			table = new BetterTable();			
			   
			data = new  Vector<Vector<Object>>(); //this vector will store the data of table
			header = new Vector<String>();	
			header.add("Nr");
			header.add("First Project (FP)");
			header.add("FP's tokenization coef.");
			header.add("Second Project (SP)");
			header.add("SP's tokenization coef.");
			header.add("Similarity coefficient");			
						
			tableModel = new DefaultTableModel();
			tableModel = (DefaultTableModel) table.getModel();
			table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			//table.setToolTipText("<html>The results of similarity tests between all selected projects<br>Select the pair of projects to see similar fragments</html>");
			table.setBackground(Color.WHITE);	
			
			// Properties of header of table
			table.getTableHeader().setFont(new Font(null, Font.BOLD,12)); 
			table.getTableHeader().setBackground(new Color(139,35,35));
			table.getTableHeader().setForeground(Color.WHITE);			
			//table.getColumn("Similarity coefficient").setCellRenderer(new SimilarityShower());
			
			table.addKeyListener(new KeyAdapter() {
				
				public void keyPressed(KeyEvent e) {
					
					if(e.getKeyCode() == KeyEvent.VK_RIGHT){
						
						NextHandler();
						frame2.setFocusable(true);
					}
					if(e.getKeyCode() == KeyEvent.VK_LEFT){
						
						PrevHandler();
						frame2.setFocusable(true);
					}
				}				
			});
			
			JScrollPane textView0 = new JScrollPane(table);
			textView0.getViewport().setBackground(Color.WHITE);
			textView0.setPreferredSize(new Dimension(724,119));
			gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.weightx = 1;
	        gbc.weighty = 1;
	        gbc.anchor = GridBagConstraints.CENTER;
	        gbc.insets = new Insets(0,60,0,0);
			panel0.add(textView0,gbc);		
			
			ListSelectionModel tableSelectModel = table.getSelectionModel();
			
			tableSelectModel.addListSelectionListener(new TableSelector());
			
			JLabel label43 = new JLabel("Click on the row to see similar fragments of code or navigate by keybord arrows");
			label43.setForeground(Color.GRAY);
			gbc.gridx = 0;
	        gbc.gridy = 2;
	        gbc.weightx = 0;
	        gbc.weighty = 0;
	        gbc.anchor = GridBagConstraints.EAST;
			panel0.add(label43,gbc);				
			
			add(panel0,gbc);			
		}		
	}
	
	private class Panel1 extends JPanel{	
		
		public Panel1(){
			
			setOpaque(false);
			JPanel panel2 = new JPanel();
			panel2.setLayout(new GridBagLayout());
			panel2.setOpaque(true); //to have ability to change background color
			panel2.setBackground(new Color(0,0,0,0)); //transparent color	
		
			GridBagConstraints gbc = new GridBagConstraints();
			
			JLabel label0 = new JLabel("Result Tree");
			label0.setForeground(Color.WHITE);
			label0.setFont(new Font(null,Font.BOLD, 14));
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.PAGE_START;
			panel2.add(label0,gbc);
			
			//yo dawg i heard you like JTrees so i put a JTree into a JTree so you can climb while you are climb			
			JPanel panM = new JPanel();
			panM.setLayout(new GridBagLayout());
			panM.setBackground(Color.WHITE);
			
			JPanel pan1 = new JPanel();
			pan1.setLayout(new BorderLayout());
			pan1.setBackground(Color.WHITE);
			JPanel pan2 = new JPanel();
			pan2.setLayout(new BorderLayout());
			pan2.setBackground(Color.WHITE);
			
			DefaultMutableTreeNode topik0 = new DefaultMutableTreeNode("First Tree");
			first = new JTree(topik0);
			first.setFont(new Font(null,Font.BOLD, 12));
			first.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			first.setToolTipText("<html>Trees of projects selected in \"Result\" window<br>Select the class names to see similar fragments in certain class</html>");
			first.setEnabled(false);			
			JScrollPane firstView = new JScrollPane(first);
			firstView.setPreferredSize(new Dimension(152,395));
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
	        gbc.weighty = 1;
			pan1.setPreferredSize(new Dimension(152,395));
			pan1.add(firstView);
			panM.add(pan1,gbc);
			
			first.addTreeSelectionListener(new TreeSelectionListener() {
			    
				 public void valueChanged(TreeSelectionEvent e) {
					 
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) first.getLastSelectedPathComponent();
					removeHighlights(text1);
					removeHighlights(text2);
					text1.moveCaretPosition(0);
					text2.moveCaretPosition(0);
					 
					if (node == null) return;
					
					String title;
					int nDepth = node.getDepth();
					 
					if(nDepth == 0){
						
						int start = node.getUserObject().toString().indexOf('.');
						int pos = Integer.parseInt(node.getUserObject().toString().substring(0,start));
						int pos2 = Integer.parseInt(node.getUserObject().toString().substring(start + 1,node.getUserObject().toString().indexOf('.',start+1)));
						text1.setText(Texts.get(pos-1).get(pos2-1));
						text1.moveCaretPosition(0);
						
						DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) node.getParent();
						String subTitle = tempNode.getUserObject().toString().substring(tempNode.getUserObject().toString().indexOf(".") + 2);						
						title = node.getUserObject().toString().substring(node.getUserObject().toString().indexOf('.', start+1) + 1) + " (" + subTitle + ")";
						
						label3.setText(title);
						if(second.getSelectionPath() != null && second.getSelectionPath().getPathCount() == 2) ColorSelected(0);
						else if(second.getSelectionPath() != null && second.getSelectionPath().getPathCount() == 1) ColorSelected(1);
						else if(second.getSelectionPaths() == null){ text2.setText(""); ; label4.setText(""); }
					}
					else{
						
						int start = node.getUserObject().toString().indexOf('.');
						int pos = Integer.parseInt(node.getUserObject().toString().substring(0,start));
						text1.setText(tempWC.get(pos-1));
						text1.moveCaretPosition(0);
						title = node.getUserObject().toString().substring(node.getUserObject().toString().indexOf('.') + 1);
						
						label3.setText(title);
						if(second.getSelectionPath() != null && second.getSelectionPath().getPathCount() == 2) ColorSelected(2);
						else if(second.getSelectionPath() != null && second.getSelectionPath().getPathCount() == 1){ levelflag = false; ColorAll();}
						else if(second.getSelectionPaths() == null){ text2.setText(""); ; label4.setText(""); }
					}					 
				}			 
			});			
			
			DefaultMutableTreeNode topik1 = new DefaultMutableTreeNode("Second Tree");
			second = new JTree(topik1);
			second.setFont(new Font(null,Font.BOLD, 12));
			second.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			second.setToolTipText("<html>Trees of projects selected in \"Result\" window<br>Select the class names to see similar fragments in certain class</html>");
			second.setEnabled(false);
			JScrollPane secondView = new JScrollPane(second);
			secondView.setPreferredSize(new Dimension(152,395)); 
			pan2.add(secondView);
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 1;
	        gbc.weighty = 1;
			pan2.setPreferredSize(new Dimension(152,395));
			panM.add(pan2,gbc);		
			
			second.addTreeSelectionListener(new TreeSelectionListener() {
			    
				 public void valueChanged(TreeSelectionEvent e) {
					 
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) second.getLastSelectedPathComponent();
					removeHighlights(text1);
					removeHighlights(text2);
					 
					if (node == null) return;
					
					String title;
					int pos = 0, nDepth = node.getDepth();
					 
					if(nDepth == 0){
						
						int start = node.getUserObject().toString().indexOf('.');
						pos = Integer.parseInt(node.getUserObject().toString().substring(0,start));
						int pos2 = Integer.parseInt(node.getUserObject().toString().substring(start + 1,node.getUserObject().toString().indexOf('.',start+1)));
						text2.setText(Texts.get(pos-1).get(pos2-1));
						text2.moveCaretPosition(0);

						DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) node.getParent();
						String subTitle = tempNode.getUserObject().toString().substring(tempNode.getUserObject().toString().indexOf(".") + 2);						
						title = node.getUserObject().toString().substring(node.getUserObject().toString().indexOf('.', start+1) + 1) + " (" + subTitle + ")";
						
						label4.setText(title);
						if(first.getSelectionPath() != null && first.getSelectionPath().getPathCount() == 2) ColorSelected(0);
						if(first.getSelectionPath() != null && first.getSelectionPath().getPathCount() == 1) ColorSelected(2);	
						else if(first.getSelectionPaths() == null){ text1.setText(""); label3.setText(""); }
					}
					else{
						
						int start = node.getUserObject().toString().indexOf('.');
						pos = Integer.parseInt(node.getUserObject().toString().substring(0,start));
						text2.setText(tempWC.get(pos-1));
						text2.moveCaretPosition(0);
						title = node.getUserObject().toString().substring(node.getUserObject().toString().indexOf('.') + 1);
						label4.setText(title);							
						if(first.getSelectionPath() != null && first.getSelectionPath().getPathCount() == 2) ColorSelected(1);
						else if(second.getSelectionPath() != null && second.getSelectionPath().getPathCount() == 1){ levelflag = false; ColorAll();}
						else if(first.getSelectionPaths() == null){ text2.setText(""); ; label4.setText(""); }
					}
				 }			 
			});			
			
			JScrollPane Treetext = new JScrollPane(panM);
			Treetext.setPreferredSize(new Dimension(310,400));					
			gbc.gridx = 0;
	        gbc.gridy = 3;
	        gbc.weightx = 1;
	        gbc.weighty = 1;
			panel2.add(Treetext,gbc);	
			
			JButton SAll = new JButton("Show All");
			SAll.setBackground(new Color(139,35,35));
			SAll.setFont(new Font(null,Font.BOLD, 12));
			SAll.setForeground(Color.WHITE);
			SAll.setFocusPainted(false);
			SAll.setToolTipText("Mark all similar fragments of source codes");
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.insets = new Insets(20,0,0,0);
			gbc.anchor = GridBagConstraints.SOUTHWEST;
			panel2.add(SAll,gbc);
			
			SAll.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					
					ColorAll();
				}
			});
			
			JButton SF = new JButton("Show First");
			SF.setBackground(new Color(139,35,35));
			SF.setFont(new Font(null,Font.BOLD, 12));
			SF.setForeground(Color.WHITE);
			SF.setFocusPainted(false);
			SF.setToolTipText("Show first similarity in source code");
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.weightx = 1;
	        gbc.weighty = 1;
			gbc.insets = new Insets(20,0,0,0);
			gbc.anchor = GridBagConstraints.SOUTHEAST;
			panel2.add(SF,gbc);
			
			SF.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent e) {
					
					if(Row < 0) return;
					
					ArrayList<double[]> TempPlag2= new ArrayList<double[]>();			
					TempPlag.addAll(FirstFrame.GetPlagArrL());
					
					removeHighlights(text1);
					removeHighlights(text2);
					
					if(!levelflag) TempPlag2.addAll(TempPlag.get(Row));	
					else{	
					
						if(classesPlag.size() == 0) return;
						TempPlag2.addAll(classesPlag);		
					}					
					
					double[] plag = new double[6];
					
					plag = TempPlag2.get(0);
					
					mainCounter = -1;
					
					DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,127,36));							
					
					try {
						
						text1.getHighlighter().addHighlight((int)plag[0], (int)plag[1], highlightPainter);
						//int carPos1 = ((int)plag[1] + (int)plag[0])/2;
						text1.moveCaretPosition((int)plag[0]);
						centerLineInScrollPane(text1); //centering the carpos in Jscrollpane
						text2.getHighlighter().addHighlight((int)plag[2], (int)plag[3], highlightPainter);
						//int carPos2 = ((int)plag[3] + (int)plag[2])/2;
						text2.moveCaretPosition((int)plag[2]);
						centerLineInScrollPane(text2); //centering the carpos in Jscrollpane				
					} 
					catch (BadLocationException e1) {
						
						JOptionPane.showMessageDialog(frame2, "<html>An error ocurred in application! Please run application again later! The source of error:" + " <br>(" + e.toString() + ")</html>","Undefined error!",JOptionPane.WARNING_MESSAGE);
						System.exit(0);
					}
				}
			});
			
			add(panel2);						
		}
	}
	
	private class Panel2 extends JPanel{
		
		public Panel2(){
			
			setOpaque(false);
			GridBagConstraints gbc = new GridBagConstraints();
			
			JPanel panel3 = new JPanel();
			panel3.setLayout(new GridBagLayout());
			panel3.setOpaque(false); //to have ability to change background color
			panel3.setBackground(new Color(0,0,0,0)); //transparent color	
						
			label3 = new JTextField();			
			label3.setDocument(new LimitJTextField(40));
			label3.setText("First Source Code");
			label3.setFont(new Font(null,Font.BOLD, 14));
			label3.setHorizontalAlignment(JTextField.CENTER);
			label3.setPreferredSize(new Dimension(300,20));
			label3.setBackground(Color.WHITE);
			label3.setEditable(false);
			label3.setBorder(javax.swing.BorderFactory.createEmptyBorder());			
			gbc.gridx = 0;
			gbc.gridy = 0;	
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.CENTER;	
			panel3.add(label3,gbc);		
			
			text1 = new JTextArea();
			text1.setEditable(false);
			//text1.getDocument().putProperty("ZOOM_FACTOR", new Double(0.5));
			text1.setToolTipText("Source code of the first selected project with marked similarity");
			JScrollPane textView1 = new JScrollPane(text1);
			textView1.setPreferredSize(new Dimension(330,400));				
			gbc.gridx = 0;
	        gbc.gridy = 1;
	        gbc.weightx = 1;
	        gbc.weighty = 1;
			panel3.add(textView1,gbc);			
			
			JButton prev = new JButton("Previous similarity");
			prev.setBackground(new Color(139,35,35));
			prev.setFont(new Font(null,Font.BOLD, 14));
			prev.setForeground(Color.WHITE);
			prev.setFocusPainted(false);
			prev.setToolTipText("Show previous similar fragment of source code");
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			panel3.add(prev,gbc);		
			
			prev.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					
					PrevHandler();					
				}
			});
			
			Image img3 = null;
			try {
				img3 = ImageIO.read((new File("images//left_arrow.png")));
			} catch (IOException e1) {
				
				JOptionPane.showMessageDialog(frame2, "There no down_arrow image in images folder","No such image!",JOptionPane.WARNING_MESSAGE);
			}
			
			ImageIcon icon3 = new ImageIcon(img3);
			JLabel imageLabel2 = new JLabel("", icon3, JLabel.CENTER);
			imageLabel2.setToolTipText("Click left arrow button on your keyboard to mark previous similar fragments of source code");
			gbc.gridx = 0;
			gbc.gridy = 2;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.LINE_END;
			panel3.add(imageLabel2,gbc);			
		
			add(panel3);
		}
	}
	
	private class Panel3 extends JPanel{	
		
		public Panel3(){
			
			setOpaque(false);
			GridBagConstraints gbc = new GridBagConstraints();
			
			JPanel panel4 = new JPanel();
			panel4.setLayout(new GridBagLayout());
			panel4.setOpaque(false); //to have ability to change background color
			panel4.setBackground(new Color(0,0,0,0)); //transparent color	
				
			label4 = new JTextField();			
			label4.setDocument(new LimitJTextField(40));
			label4.setText("Second Source Code");
			label4.setFont(new Font(null,Font.BOLD, 14));
			label4.setHorizontalAlignment(JTextField.CENTER);
			label4.setPreferredSize(new Dimension(300,20));
			label4.setBackground(Color.WHITE);
			label4.setEditable(false);
			label4.setBorder(javax.swing.BorderFactory.createEmptyBorder());			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.PAGE_START;
			panel4.add(label4,gbc);
			
			text2 = new JTextArea();
			text2.setEditable(false);
			//text2.getDocument().putProperty("i18n", Boolean.FALSE);
			//text2.getDocument().putProperty("ZOOM_FACTOR", new Double(0.5)); //zooming propery Double number is equal to Double*100%, so for example Double(2.5) = 250%
			text2.setToolTipText("Source code of the second selected project with marked similarity");
			JScrollPane textView2 = new JScrollPane(text2);
			textView2.setPreferredSize(new Dimension(330,400));	
			gbc.gridx = 0;
	        gbc.gridy = 2;
	        gbc.weightx = 1;
	        gbc.weighty = 1;
			panel4.add(textView2,gbc);	
			
			JButton cncl = new JButton("Quit");
			cncl.setBackground(new Color(139,35,35));
			cncl.setFont(new Font(null,Font.BOLD, 16));
			cncl.setForeground(Color.WHITE);
			cncl.setFocusPainted(false);
			cncl.setToolTipText("Quit the application");
			gbc.gridx = 0;
			gbc.gridy = 4;	
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.SOUTHEAST;
			panel4.add(cncl,gbc);
			
			cncl.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent event){
					
					Window[] windows = Window.getWindows();
					    
				    if( windows != null ) { 
				    	
				        for(Window w : windows ) {
				        	
				            w.dispose();
				        }
				    }
				}
			});					
			
			JButton next = new JButton("Next similarity");
			next.setBackground(new Color(139,35,35));
			next.setFont(new Font(null,Font.BOLD, 14));
			next.setForeground(Color.WHITE);
			next.setFocusPainted(false);
			next.setToolTipText("Show next similar fragment of source code");
			gbc.gridx = 0;
			gbc.gridy = 3;		
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.CENTER;			
			panel4.add(next,gbc);	
			
			next.addActionListener(new ActionListener() {
			
				public void actionPerformed(ActionEvent e) {
					
					NextHandler();
				}
			});
			
			Image img3 = null;
			try {
				img3 = ImageIO.read((new File("images//right_arrow.png")));
			} catch (IOException e1) {
				
				JOptionPane.showMessageDialog(frame2, "There no down_arrow image in images folder","No such image!",JOptionPane.WARNING_MESSAGE);
			}
			
			ImageIcon icon3 = new ImageIcon(img3);
			JLabel imageLabel2 = new JLabel("", icon3, JLabel.CENTER);
			imageLabel2.setToolTipText("Click right arrow button on your keyboard to mark next similar fragments of source code");
			gbc.gridx = 0;
			gbc.gridy = 3;
			gbc.weightx = 0;
	        gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.LINE_START;
			panel4.add(imageLabel2,gbc);
			
			add(panel4);
		}
	}
	
	public static void AddList(Vector<Vector<Object>> str){				
		
		TreeChilds.addAll(FirstFrame.getTree());		
		data.addAll(str);
		tableModel.setDataVector(data,header);			
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
					
		table.getColumn("Nr").setCellRenderer(centerRenderer);		
		table.getColumn("First Project (FP)").setCellRenderer(centerRenderer);
		table.getColumn("FP's tokenization coef.").setCellRenderer(centerRenderer);
		table.getColumn("Second Project (SP)").setCellRenderer(centerRenderer);
		table.getColumn("SP's tokenization coef.").setCellRenderer(centerRenderer);		
		table.getColumn("Similarity coefficient").setCellRenderer(centerRenderer);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumn("Nr").setPreferredWidth(20);		
		table.getColumn("First Project (FP)").setPreferredWidth(135);
		table.getColumn("FP's tokenization coef.").setPreferredWidth(140);
		table.getColumn("Second Project (SP)").setPreferredWidth(135);
		table.getColumn("SP's tokenization coef.").setPreferredWidth(140);	
		table.getColumn("Similarity coefficient").setPreferredWidth(150);
		
		Texts.addAll(FirstFrame.GetTexts());			
		tempWC.addAll(FirstFrame.GetWholeCode());		
		ProjectBorders.addAll(FirstFrame.GetProjectBorders());
	}	
	public static void SetSumm(String str){
		
		label2.setText(str);
	}
	
	public static void setFrame2Visible(){
		
		frame2.setVisible(true);
	}
	private DefaultMutableTreeNode CopyNode(DefaultMutableTreeNode OriginNode){
	   
		DefaultMutableTreeNode Copy = new DefaultMutableTreeNode(OriginNode.toString());
		
	    if(OriginNode.isLeaf()){
	        
	    	return Copy;
	    }
	    else{
	    	
	        int cc = OriginNode.getChildCount();
	        
	        for(int i=0;i<cc;i++){
	        	
	            Copy.add(CopyNode((DefaultMutableTreeNode)OriginNode.getChildAt(i)));
	        }
	        
	        return Copy;
	    }
	}
	public void ClearAll2(){
		
		DefaultTreeModel model = (DefaultTreeModel) first.getModel();							
		first.removeAll();
		DefaultMutableTreeNode temp1 = new DefaultMutableTreeNode("First Tree");
		model.setRoot(temp1);
		first.setEnabled(false);
		
		DefaultTreeModel model2 = (DefaultTreeModel) second.getModel();
		second.removeAll();		
		DefaultMutableTreeNode temp2 = new DefaultMutableTreeNode("Second Tree");
		model2.setRoot(temp2);		
		second.setEnabled(false);
		
		removeHighlights(text1);
		removeHighlights(text2);
		
		text1.setText("");
		label3.setText("First Source Code");
		text2.setText("");
		label4.setText("Second Source Code");
		mainCounter = -1;
		Row = -1;
		TreeChilds.clear();
		data.removeAllElements();
		ProjectBorders.clear();
		Texts.clear();
		TempPlag.clear();
		tempWC.clear();
	}
	public static void removeHighlights(JTextArea textComp) {
		
	    Highlighter hilite = textComp.getHighlighter();
	    Highlighter.Highlight[] hilites = hilite.getHighlights();
	
	    for (int i = 0; i < hilites.length; i++) {
	    	
	    	if (hilites[i].getPainter() instanceof DefaultHighlightPainter) {
	    	  
	    		hilite.removeHighlight(hilites[i]);
	      	}
	    }
	}
	
	private class BetterTable extends JTable { //my own modification of JTable
		
		private Border outside = new MatteBorder(2, 2, 2, 2, new Color(0,139,0));
		private Border inside = new EmptyBorder(0, 0, 0, 0);
		private Border highlight = new CompoundBorder(outside, inside);	
													
	    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	            
	    	Component c = super.prepareRenderer(renderer, row, column);	
	        JComponent jc = (JComponent)c;
	        			          			         
	        if (!isRowSelected(row)){
	        	
	        	c.setBackground(row % 2 == 0 ? getBackground() : new Color(51,102,153));			            	 
		        c.setForeground(row % 2 == 0 ? getForeground() : Color.WHITE);
	        }
	        			           			                	
	    	if(column == 5){
	    		
	    		c.setFont(c.getFont().deriveFont(Font.BOLD));
	    		
	    		String part = this.getModel().getValueAt(row, column).toString();
	    		part = part.substring(0, part.length() - 4);
	    		jc.setBorder(highlight);
	    		//jc.setBackground(new Color(255,127,36));
	    		//jc.setForeground(Color.BLACK);
	    	}	    		   	
	    				       
	        return c;
	    }
	    
		public String getToolTipText(MouseEvent event){ //overrides getToolTipText
			
			String str = "";
			
			if (columnAtPoint(event.getPoint()) == 0) str = "The number of similarity test";
			else if(columnAtPoint(event.getPoint()) == 1) str = "Full name of the first project";
			else if (columnAtPoint(event.getPoint()) == 2 || columnAtPoint(event.getPoint()) == 4) str = "<html>Tokenization coefficient shows how efficient the source code of the project was translated<br>to the tokens which were used to find similarities<br>The higher value of this coefficient the better precision of anti-plagiarism algorithm</html>";
			else if (columnAtPoint(event.getPoint()) == 3) str = "Full name of the second project";
			else if (columnAtPoint(event.getPoint()) == 5) str = "<html>Similarity coefficient shows how many substrings of first project's source<br>code are similar to substrings of the second project's source code</html>";			    		
			
			return str;
		}
		public boolean isCellEditable(int row, int column) { //ensures that all celles are not editable
		     
		       return false;
		}
	}
	
	private class TableSelector implements ListSelectionListener {  //selection event on bettertable
	    
		public void valueChanged(ListSelectionEvent e) {
			
			mainCounter = -1;			
			first.setEnabled(true);
			second.setEnabled(true);
			
			Row = table.getSelectedRow();
			levelflag = false;
			if(Row < 0) return;
			
			int licznik = 0, wholecode1,wholecode2;						
			String proj1 = (String) table.getValueAt(Row, 1);
			label3.setText(proj1);	
			String proj2 = (String) table.getValueAt(Row, 3);
			label4.setText(proj2);			

			boolean flaga = false;
										
			for(DefaultMutableTreeNode node: TreeChilds){
								
				if(licznik > 2) break;
				
				if(node.getUserObject().toString().contains(proj1) && flaga == false){							
					
					DefaultTreeModel model = (DefaultTreeModel) first.getModel();							
					wholecode1 = Integer.parseInt(node.getUserObject().toString().substring(0,node.getUserObject().toString().indexOf(".")));							
					DefaultMutableTreeNode temp1 = new DefaultMutableTreeNode();
					temp1 = CopyNode(node);
					temp1.setUserObject(node.getUserObject().toString());
				    first.removeAll();
				    model.setRoot(temp1);							        
				    model.reload();
				    flaga = true;
				    
				    text1.setText(tempWC.get(wholecode1-1));
					text1.setCaretPosition(0);
											    
				    licznik++;
				}
				else if(node.getUserObject().toString().contains(proj2)){
					
					DefaultTreeModel model = (DefaultTreeModel) second.getModel();
					wholecode2 = Integer.parseInt(node.getUserObject().toString().substring(0,node.getUserObject().toString().indexOf(".")));							
					DefaultMutableTreeNode temp1 = new DefaultMutableTreeNode();
					temp1 = CopyNode(node);
					temp1.setUserObject(node.getUserObject().toString());
					second.removeAll();
					model.setRoot(temp1);					  	    
				    model.reload();	
				    
				    text2.setText(tempWC.get(wholecode2-1));
					text2.setCaretPosition(0);
				    
				    licznik++;
				}						
			}	
			
			TempPlag.addAll(FirstFrame.GetPlagArrL());												
			ColorAll();	
	    }			    
	}
	
	public void ColorAll(){
		
		if(Row < 0) return;
		
		LinkedHashSet<int[]> Colors = new LinkedHashSet<int[]>();
		ArrayList<double[]> TempPlag2 = new ArrayList<double[]>();
		removeHighlights(text1);
		removeHighlights(text2);
		text1.moveCaretPosition(0);
		text2.moveCaretPosition(0);
		int maxLen = 0, minLen = 20, similarityNr = 0;
		double sumSim = 0;
		
		mainCounter = -1;
		
		if(!levelflag) TempPlag2.addAll(TempPlag.get(Row));	
		else{	
		
			if(classesPlag.size() == 0) return;
			TempPlag2.addAll(classesPlag);		
		}
		
		similarityNr = TempPlag2.size();

		for(double[] plag: TempPlag2){
			
			if(plag[1] == 0.0) continue;
			
			int[] colors = new int[3];
			
			int x1 = (int) (Math.random() * 255);
			int x2 = (int) (Math.random() * 255);
			int x3 = (int) (Math.random() * 255);
			int x4 = 100;
			
			colors[0] = x1;
			colors[1] = x2;
			colors[2] = x3;
									
			while(Colors.contains(colors)){ //ensures that the Colors will be different
				
				x1 = (int) (Math.random() * 255);
				x2 = (int) (Math.random() * 255);
				x3 = (int) (Math.random() * 255);
				
				colors[0] = x1;
				colors[1] = x2;	
				colors[2] = x3;													
			}
			
			Colors.add(colors);	
			
			if(plag[4] >= maxLen) maxLen = (int) plag[4];
			if(plag[4] <= minLen) minLen = (int) plag[4];
			sumSim+=plag[4];
			
			DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(x1,x2,x3,x4));							
			
			try {
				
				text1.getHighlighter().addHighlight((int)plag[0], (int)plag[1], highlightPainter);
				text2.getHighlighter().addHighlight((int)plag[2], (int)plag[3], highlightPainter);
			} 
			catch (BadLocationException e1) {
				
				JOptionPane.showMessageDialog(frame2, "<html>An error ocurred in application! Please run application again later! The source of error:" + " <br>(" + e1.toString() + ")</html>","Undefined error!",JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}			
		}
		if(!levelflag) label2.setText("Statistics of comparison nr " + (Row+1) + ":\nSimilarity coeficient: " + Math.round(TempPlag2.get(similarityNr - 1)[0]*100)/(double)100 + "%\nNr of found similarities: " + similarityNr + "\nShortest smilirarity: " + minLen + "\nLongest similarity: " + maxLen + "\nAverage similariry length: " + Math.round((sumSim/(double)similarityNr)*100)/(double)100);
		else{
			
			label2.setText("Statistics of comparison:\nSimilarity coeficient: " + Math.round((sumSim/(double)text1.getText().length())*100)/(double)100 + "%\nNr of found similarities: " + similarityNr + "\nShortest smilirarity: " + minLen + "\nLongest similarity: " + maxLen + "\nAverage similariry length: " + Math.round((sumSim/(double)similarityNr)*100)/(double)100);
		}
	}
	public void ColorSelected(int option){
		
		if(Row < 0) return;
		
		LinkedHashSet<int[]> Colors = new LinkedHashSet<int[]>();
		double sumSim= 0.0, sumSim2 = 0.0;
		int similarityNr = 0, minLen = text1.getText().length(), maxLen = 0; 
				
		mainCounter = -1;
		levelflag = true;
		classesPlag.clear();		

		DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) first.getLastSelectedPathComponent();
		int[] similar1 = null;
		
		int start1 = node1.getUserObject().toString().indexOf('.');
		int pos = Integer.parseInt(node1.getUserObject().toString().substring(0,start1));
		if(option == 0 || option == 1){
			
			int pos2 = Integer.parseInt(node1.getUserObject().toString().substring(start1 + 1,node1.getUserObject().toString().indexOf('.',start1 + 1)));
			similar1  = ProjectBorders.get(pos - 1).get(pos2-1);
		}
		
		DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) second.getLastSelectedPathComponent();
		int[] similar2 = null;
		
		int start2 = node2.getUserObject().toString().indexOf('.');
		int pos3 = Integer.parseInt(node2.getUserObject().toString().substring(0,start2));
		if(option == 0 || option == 2){ 
			
			int pos4 = Integer.parseInt(node2.getUserObject().toString().substring(start2 + 1,node2.getUserObject().toString().indexOf('.',start2 + 1)));
			similar2  = ProjectBorders.get(pos3 - 1).get(pos4-1);	
		}		

		for(double[] plag: TempPlag.get(Row)){
			
			if(plag[1] == 0.0) continue;
			
			int[] colors = new int[3];
			
			int x1 = (int) (Math.random() * 255);
			int x2 = (int) (Math.random() * 255);
			int x3 = (int) (Math.random() * 255);
			int x4 = 100;
			
			colors[0] = x1;
			colors[1] = x2;
			colors[2] = x3;
									
			while(Colors.contains(colors)){ //ensures that the Colors will be different
				
			x1 = (int) (Math.random() * 255);
			x2 = (int) (Math.random() * 255);
			x3 = (int) (Math.random() * 255);
			
			colors[0] = x1;
			colors[1] = x2;	
			colors[2] = x3;													
			}
		
			Colors.add(colors);	
		
			DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(x1,x2,x3,x4));			
					
			if(option == 0){				
			
				if((int) plag[0] >= similar1[0] && (int) plag[1] <= similar1[1] && plag[2] >= similar2[0] && plag[3] <= similar2[1]){
					
					int first1 = (int)plag[0] - similar1[0];
					int second1 = (int)plag[1] - similar1[0];
					int first2 = (int)plag[2] - similar2[0];
					int second2 = (int)plag[3] - similar2[0];
					
					double[] insert = new double[6];
					insert[0] = first1;
					insert[1] = second1;
					insert[2] = first2;
					insert[3] = second2;
					
					
					if(plag[4] >= maxLen) maxLen = (int) plag[4];
					if(plag[4] <= minLen) minLen = (int) plag[4];
					sumSim+=plag[4];
					sumSim2 += plag[1] - plag[0];
					similarityNr++;
					
					classesPlag.add(insert);
					
					try {
						
						text1.getHighlighter().addHighlight(first1,second1, highlightPainter);
						text2.getHighlighter().addHighlight(first2, second2, highlightPainter);
					} 
					catch (BadLocationException e) {
					
						JOptionPane.showMessageDialog(frame2, "<html>An error ocurred in application! Please run application again later! The source of error:" + " <br>(" + e.toString() + ")</html>","Undefined error!",JOptionPane.WARNING_MESSAGE);
						System.exit(0);
					}					
				}			
			}
			else if(option == 1){
				
				if((int) plag[0] >= similar1[0] && (int) plag[1] <= similar1[1]){
					
					int first1 = (int)plag[0] - similar1[0];
					int second1 = (int)plag[1] - similar1[0];
					int first2 = (int)plag[2];
					int second2 = (int)plag[3];
					
					double[] insert = new double[6];
					insert[0] = first1;
					insert[1] = second1;
					insert[2] = first2;
					insert[3] = second2;
										
					if(plag[4] >= maxLen) maxLen = (int) plag[4];
					if(plag[4] <= minLen) minLen = (int) plag[4];
					sumSim+=plag[4];	
					sumSim2 += plag[1] - plag[0];
					similarityNr++;
					
					classesPlag.add(insert);
					
					try {
						
						text1.getHighlighter().addHighlight(first1,second1, highlightPainter);
						text2.getHighlighter().addHighlight(first2, second2, highlightPainter);
					} 
					catch (BadLocationException e) {
					
						JOptionPane.showMessageDialog(frame2, "<html>An error ocurred in application! Please run application again later! The source of error:" + " <br>(" + e.toString() + ")</html>","Undefined error!",JOptionPane.WARNING_MESSAGE);
						System.exit(0);
					}					
				}				
			}
			else if(option == 2){
											
				if((int) plag[2] >= similar2[0] && (int) plag[3] <= similar2[1]){
					
					int first1 = (int)plag[0];
					int second1 = (int)plag[1];
					int first2 = (int)plag[2] - similar2[0];
					int second2 = (int)plag[3] - similar2[0];
					
					double[] insert = new double[6];
					insert[0] = first1;
					insert[1] = second1;
					insert[2] = first2;
					insert[3] = second2;
										
					if(plag[4] >= maxLen) maxLen = (int) plag[4];
					if(plag[4] <= minLen) minLen = (int) plag[4];
					sumSim+=plag[4];
					sumSim2 += plag[3] - plag[2];
					similarityNr++;
					
					classesPlag.add(insert);
					
					try {
						
						text1.getHighlighter().addHighlight(first1,second1, highlightPainter);
						text2.getHighlighter().addHighlight(first2, second2, highlightPainter);
					} 
					catch (BadLocationException e) {
					
						JOptionPane.showMessageDialog(frame2, "<html>An error ocurred in application! Please run application again later! The source of error:" + " <br>(" + e.toString() + ")</html>","Undefined error!",JOptionPane.WARNING_MESSAGE);
						System.exit(0);
					}					
				}
			}
		}	
		int tknLen = 0;		
		
		if(text1.getText().length() <= text2.getText().length()){	
			
			int len1 = text2.getText().length();
			String temp2 = text2.getText().replaceAll("import.*?;", "").replaceAll("package.*?;", "");
			
			int i = 0;
			while(!Character.isLetter(temp2.charAt(i))) i++;			
			tknLen = temp2.length() - i;
		}
		else{
			
			String temp1 = text1.getText().replaceAll("import.*?;", "").replaceAll("package.*?;", "");
			
			int i = 0;
			while(!Character.isLetter(temp1.charAt(i))) i++;
			tknLen = temp1.length() - i;
		}
						
		if(similarityNr > 0) label2.setText("Statistics of comparisons:\nSimilarity coeficient: " + Math.round((sumSim2/(double)(tknLen-2))*10000)/(double)100 + "%\nNr of found similarities: " + similarityNr + "\nShortest smilirar substring: " + minLen + "\nLongest similar substring: " + maxLen + "\nAverage similariry length: " + Math.round((sumSim/(double)similarityNr)*100)/(double)100);
		else label2.setText("Statistics of comparisons:\nNo similarity found");
	}
	
	class LimitJTextField extends PlainDocument{ 
		  
		private int limit;
		
		LimitJTextField(int limit) {
		    super();
		    this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
						 
		    if (str == null) return;
		
		    if (str.length() >= limit) {
		    	
		    	String nowy = str.substring(0,limit - 3) + "...";
		    	
		    	super.insertString(offset, nowy, attr);
		    }
		    else{
		    	
		    	super.insertString(offset, str, attr);
		    }
		}
	}
	public static void centerLineInScrollPane(JTextComponent component)
	{
	    Container container = SwingUtilities.getAncestorOfClass(JViewport.class, component);

	    if (container == null) return;

	    try
	    {
	        Rectangle r = component.modelToView(component.getCaretPosition());
	        JViewport viewport = (JViewport)container;

	        int extentWidth = viewport.getExtentSize().width;
	        int viewWidth = viewport.getViewSize().width;

	        int x = Math.max(0, r.x - (extentWidth / 2));
	        x = Math.min(x, viewWidth - extentWidth);

	        int extentHeight = viewport.getExtentSize().height;
	        int viewHeight = viewport.getViewSize().height;

	        int y = Math.max(0, r.y - (extentHeight / 2));
	        y = Math.min(y, viewHeight - extentHeight);

	        viewport.setViewPosition(new Point(x, y));
	    }
	    catch(BadLocationException ble) {
	    	
	    	JOptionPane.showMessageDialog(frame2, "<html>An error ocurred in application! Please run application again later! The source of error:" + " <br>(" + ble.toString() + ")</html>","Undefined error!",JOptionPane.WARNING_MESSAGE);
			System.exit(0);
	    }
	}
	public void NextHandler(){
				
		if(Row < 0) return;
		
		double[] plag = new double[6];		

		removeHighlights(text1);
		removeHighlights(text2);
		
		DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,127,36));
				
		mainCounter++;
				
		if(!levelflag){
			
			if(mainCounter >= TempPlag.get(Row).size() - 1 || mainCounter < -1) mainCounter = 0; //second condition because at the end of each AL in TempPlag there is different sort of information
			plag = TempPlag.get(Row).get(mainCounter);	
		}
		else{	
		
			if(classesPlag.size() == 0) return;
			if(mainCounter >= classesPlag.size() || mainCounter < -1) mainCounter = 0; //second condition because at the end of each AL in TempPlag there is different sort of information
			plag = classesPlag.get(mainCounter);		
		}	

		
		try {
							
			text1.getHighlighter().addHighlight((int)plag[0], (int)plag[1], highlightPainter);
			//int carPos1 = ((int)plag[1] + (int)plag[0])/2;
			text1.moveCaretPosition((int)plag[0]);
			centerLineInScrollPane(text1); //centering the carpos in Jscrollpane
			text2.getHighlighter().addHighlight((int)plag[2], (int)plag[3], highlightPainter);
			//int carPos2 = ((int)plag[3] + (int)plag[2])/2;
			text2.moveCaretPosition((int)plag[2]);
			centerLineInScrollPane(text2); //centering the carpos in Jscrollpane
			
		} 
		catch (BadLocationException e1) {
			
			JOptionPane.showMessageDialog(frame2, "<html>An error ocurred in application! Please run application again later! The source of error:" + " <br>(" + e1.toString() + ")</html>","Undefined error!",JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}			
	}
	
	private void PrevHandler(){
			
		if(Row < 0) return;
		
		double[] plag = new double[6];
		
		removeHighlights(text1);
		removeHighlights(text2);
		
		DefaultHighlighter.DefaultHighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,127,36));
		
		mainCounter--;	
				
		if(!levelflag){
			
			if(mainCounter < 0) mainCounter = TempPlag.get(Row).size()-2; //-3, because at the and of each AL in TempPlag is different sort of information -> similiry coefficient
			plag = TempPlag.get(Row).get(mainCounter);	
		}
		else{	
		
			if(classesPlag.size() == 0) return;
			if(mainCounter < 0) mainCounter = classesPlag.size()-1; 
			plag = classesPlag.get(mainCounter);		
		}
				
		try {
							
			text1.getHighlighter().addHighlight((int)plag[0], (int)plag[1], highlightPainter);
			//int carPos1 = ((int)plag[1] + (int)plag[0])/2;
			text1.moveCaretPosition((int)plag[0]);
			centerLineInScrollPane(text1); //centering the carpos in Jscrollpane
			text2.getHighlighter().addHighlight((int)plag[2], (int)plag[3], highlightPainter);
			//int carPos2 = ((int)plag[3] + (int)plag[2])/2;
			text2.moveCaretPosition((int)plag[2]);
			centerLineInScrollPane(text2); //centering the carpos in Jscrollpane
			
		} 
		catch (BadLocationException e1) {
			
			JOptionPane.showMessageDialog(frame2, "<html>An error ocurred in application! Please run application again later! The source of error:" + " <br>(" + e1.toString() + ")</html>","Undefined error!",JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}		
	}
}
	
	