package libs;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


@SuppressWarnings("serial")
public class ItemListInterface extends JFrame {
	
	private static final String S_NPC = "NPC";
	private static final String S_ITEM = "Item";
	private static final String S_OBJ = "Object";
	private static final String ID_LBL = "ID:";
	private static final String NM_LBL = "NAME:";
	private static final String ERR_NO_NM = "No name matched.";
	private static final String ERR_NO_ID = "null";
	
	private static final String[] resultsColumnIdentifiers = {
		NM_LBL, ID_LBL
	};

	private static final int JFRAME_WIDTH = 600;
	private static final int JFRAME_HEIGHT = 600;
	
	private static final int SB_X = 15;
	private static final int SB_Y = 25;
	private static final int SB_BTN_X_OFST = 420;
	private static final int RES_X = 20;
	private static final int RES_Y = 100;
	
	//Positions
	private static final Point SB_POS = new Point(SB_X, SB_Y);
	private static final Point SB_BTN_POS = new Point(SB_X+SB_BTN_X_OFST, SB_Y+30);
	private static final Point RESULTS_POS = new Point(RES_X, RES_Y);
	
	private static final Point SS_POS = new Point(SB_X+SB_BTN_X_OFST, SB_Y);
	//Heights and widths
	private static final int SEARCH_BAR_WIDTH = 400;
	private static final int SEARCH_BAR_HEIGHT = 25;
	
	private static final int SEARCH_BTN_W = 150;
	private static final int SEARCH_BTN_H = SEARCH_BAR_HEIGHT;

	private static final int SS_BOX_W = SEARCH_BTN_W;
	private static final int SS_BOX_H = SEARCH_BTN_H;
	
	private static final int RESULTS_W = JFRAME_WIDTH - 40;
	private static final int RESULTS_H = JFRAME_HEIGHT - 150;
	
	private static final int RES_ROW_H = 15;
	
	//Dimensions
	private static final Dimension JFRAME_DIMENSION = new Dimension(JFRAME_WIDTH, JFRAME_HEIGHT);
	private static final Dimension SEARCH_BAR_DIM   = new Dimension(SEARCH_BAR_WIDTH, SEARCH_BAR_HEIGHT);
	private static final Dimension SEARCH_BTN_DIM   = new Dimension(SEARCH_BTN_W, SEARCH_BTN_H);
	private static final Dimension SS_DIM           = new Dimension(SS_BOX_W, SS_BOX_H); 
	private static final Dimension RESULTS_DIM      = new Dimension(RESULTS_W, RESULTS_H);
	
	private static final String itemList = "items_en.txt";
	private static final String npcList  = "npcs_en.txt";
	private static final String objectList  = "objects_en.txt";
	
	private static final HashMap<String,  ArrayList<Integer>> items = new HashMap<String,  ArrayList<Integer>>();
	private static final HashMap<String,  ArrayList<Integer>> npcs  = new HashMap<String,  ArrayList<Integer>>();
	private static final HashMap<String,  ArrayList<Integer>> objs  = new HashMap<String,  ArrayList<Integer>>();
	private static final HashMap<Integer, String> RObjs             = new HashMap<Integer, String>();
	private static final HashMap<Integer, String> RNpcs             = new HashMap<Integer, String>();
	private static final HashMap<Integer, String> RItems            = new HashMap<Integer, String>();
	
//	public static void main(String args[]) {
//		System.out.println("Starting item database.");
//		new ItemListInterface(true);
//		System.out.println("Deleting System32..");
//		System.out.println("Just kidding, enjoy.");
//	}
	
	/**
	 * Returns the first ID in the DB of the specified item. The Hashmap is backed by an ArrayList of integers, and this method
	 * returns ArrayList[0]
	 * 
	 * @param item
	 * @return
	 */
	public Integer getItem(String item) {
		ArrayList<Integer> ii = items.get(item);
		if (ii != null) {
			return items.get(item).get(0);
		}
		return -1;
	}
	
	public String getItem(Integer i) {
		return RItems.get(i);
	}
	/**
	 * Returns the first ID in the DB of the specified object. The hashmap is backed by an ArrayList of integers, and this method
	 * returns ArrayList[0]
	 * 
	 * @param obj
	 * @return
	 */
	public Integer getObject(String obj) {
		ArrayList<Integer> ii = objs.get(obj);
		if (ii != null) {
			return objs.get(obj).get(0);
		}
		return -1;
	}
	
	public String getObject(Integer i) {
		return RObjs.get(i);
	}
	/**
	 * Returns the IDs associated with said object.
	 * 
	 */
	public ArrayList<Integer> getObjects(String objzs) {
		return objs.get(objzs);
	}
	
	/**
	 * Returns the IDs associated with said npc name.
	 * 
	 * @param npcsz
	 * @return
	 */
	public ArrayList<Integer> getNPCs(String npcsz) {
		return npcs.get(npcsz);
	}
	
	/**
	 * Returns the first ID in the DB of the specified npc. The hashmap is backed by an ArrayList of integers, and this method
	 * returns ArrayList[0]
	 * 
	 * @param obj
	 * @return
	 */
	public Integer getNPC(String npc) {
		ArrayList<Integer> ii = npcs.get(npc);
		if (ii != null) {
			return npcs.get(npc).get(0);
		}
		return -1;
	}
	
	public String getNPC(Integer i) {
		return RNpcs.get(i);
	}
	
	public boolean containsNPC(String npc) {
		return npcs.containsKey(npc);
	}
	
	public boolean containsItem(String item) {
		return items.containsKey(item);
	}
	
	public boolean containsObject(String obj) {
		return objs.containsKey(obj);
	}
	
	public boolean containsNPC(Integer npc) {
		return RNpcs.containsKey(npc);
	}

	public boolean containsItem(Integer item) {
		return RItems.containsKey(item);
	}

	public boolean containsObject(Integer obj) {
		return RObjs.containsKey(obj);
	}
	
	public ItemListInterface(boolean b) {
		if (b) {
			supplyItems();
			supplyNPCs();
			supplyObjs();
			
			setVisible(true);
			setTitle("KingIDs");
			setSize(JFRAME_DIMENSION);
			setLocationRelativeTo(null);
			setLayout(null);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			JPanel jp = new JPanel();
			jp.setLayout(null);
			setContentPane(jp);
			
			final JTextField searchField = new JTextField("Search by name");
			searchField.setSize(SEARCH_BAR_DIM);
			searchField.setLocation(SB_POS);
			searchField.setVisible(true);
			
			final JComboBox searchSetting = new JComboBox();
			searchSetting.setSize(SS_DIM);
			searchSetting.setLocation(SS_POS);
			searchSetting.setVisible(true);
			searchSetting.setModel(new DefaultComboBoxModel(new String[] {
				S_NPC, S_ITEM, S_OBJ	
			}));
			
			TableColumn idColumn = new TableColumn();
			idColumn.setHeaderValue(ID_LBL);
			idColumn.setResizable(false);
			
			TableColumn nmColumn = new TableColumn();
			nmColumn.setHeaderValue(NM_LBL);
			nmColumn.setResizable(false);
			

			JTable resultsTable = new JTable();
			resultsTable.setModel(new DefaultTableModel());
			resultsTable.setRowHeight(RES_ROW_H);
			
			final DefaultTableModel resultsModel = (DefaultTableModel) resultsTable.getModel();
			resultsModel.setColumnCount(resultsColumnIdentifiers.length);
			resultsModel.setColumnIdentifiers(resultsColumnIdentifiers);
			
			JScrollPane resultsPane = new JScrollPane(resultsTable);
			resultsPane.setSize(RESULTS_DIM);
			resultsPane.setLocation(RESULTS_POS);
			resultsPane.setVisible(true);

			
			class SearchAction extends AbstractAction {

				public SearchAction(String name) {
					super.putValue(NAME, name);
				}
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					String ss = (String)searchSetting.getSelectedItem();
					String search = searchField.getText();
					
					int rows = resultsModel.getRowCount();
					for (int i = rows; i > 0; i--) {
						resultsModel.removeRow(i-1);
					}
					
					if (ss.equals(S_NPC)) {
						addRows(search, npcs, resultsModel);
					} else if (ss.equals(S_ITEM)) {
						addRows(search, items, resultsModel);
					} else if (ss.equals(S_OBJ)) {
						addRows(search, objs, resultsModel);
					}
				}
				
			};

			JButton searchButton = new JButton();
			searchButton.setAction(new SearchAction("Search"));
			searchButton.setSize(SEARCH_BTN_DIM);
			searchButton.setVisible(true);
			searchButton.setLocation(SB_BTN_POS);
			
			
			jp.add(resultsPane);
			jp.add(searchSetting);
			jp.add(searchButton);
			jp.add(searchField);
		} else {
			supplyItems();
			supplyNPCs();
			supplyObjs();
		}
	}
	
	private static void supplyItems() {
		supply(itemList, items, RItems);
	}
	
	private static void supplyNPCs() {
		supply(npcList, npcs, RNpcs);
	}
	
	private static void supplyObjs() {
		supply(objectList, objs, RObjs);
	}
	
	private static void supply(String filename, HashMap<String,  ArrayList<Integer>> db, HashMap<Integer, String> Rdb) {
		
		URL url = ItemListInterface.class.getClassLoader().getResource(filename);
		System.out.println("FILE: " + url.getPath());
		File text = new File(url.getPath());
		try {
			FileReader textReader = new FileReader(text);
			BufferedReader rd = new BufferedReader(textReader);
			
			String line = "";
			while ((line = rd.readLine()) != null)  {
				line = line.replace("\"", "");
				String[] split = line.split(":");
				if (split != null) {
					int id = Integer.parseInt(split[0]);
					String name = split[1];
					
					Rdb.put(id, name);
					if (db.containsKey(name)) {
						db.get(name).add(id);
					} else {
						ArrayList<Integer> a = new ArrayList<Integer>();
						a.add(id);
						db.put(name, a);

					}
				}
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void addRows(String search, HashMap<String, ArrayList<Integer>> db, DefaultTableModel resultsModel) {
		final ArrayList<Integer> a = db.get(search);
		
		if ( a != null ) {
		for (int i = 0; i < a.size(); i++) {
			resultsModel.addRow(new Object[] {search, a.get(i)+""});
		}
		} else {
			resultsModel.addRow(new Object[] {ERR_NO_NM, ERR_NO_ID});
		}
	}
	
	
}
