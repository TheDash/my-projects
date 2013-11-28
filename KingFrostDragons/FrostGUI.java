import java.awt.EventQueue;


public class FrostGUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtSaveName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrostGUI frame = new FrostGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrostGUI() {
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// GUI Panel
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 469, 286);
		setTitle("KingFrostDragons");
		setVisible(true);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBounds(0, 0, 467, 225);
		contentPane.add(tabbedPane);
		
		JButton btnNewButton = new JButton("Start Script");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(-2, 226, 469, 27);
		contentPane.add(btnNewButton);
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Prayer Panel
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		JPanel prayerPanel = new JPanel();
		tabbedPane.addTab("Prayer", null, prayerPanel, null);
		prayerPanel.setLayout(null);
		
		JScrollPane jsp0 = new JScrollPane();
		jsp0.setSize(156, -42);
		jsp0.setLocation(0, 57);
		prayerPanel.add(jsp0);
		
		JButton btnAncient = new JButton("Ancient");
		btnAncient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAncient.setBounds(0, 30, 94, 27);
		prayerPanel.add(btnAncient);
		
		JButton btnNormal = new JButton("Normal");
		btnNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNormal.setBounds(92, 30, 94, 27);
		prayerPanel.add(btnNormal);
		
		
		DefaultListModel prayerModel = new DefaultListModel();
		prayerModel.addElement("Strength");
		prayerModel.addElement("Prayer");
		prayerModel.addElement("J");
		prayerModel.addElement("KEOFJ");
		prayerModel.addElement("dkfjd");
		
		JButton btnNewButton_6 = new JButton("Add");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_6.setBounds(210, 2, 69, 27);
		prayerPanel.add(btnNewButton_6);
		
		JButton btnNewButton_7 = new JButton("Del");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_7.setBounds(279, 2, 74, 27);
		prayerPanel.add(btnNewButton_7);
		JScrollPane prayerScrollPane = new JScrollPane();
		prayerScrollPane.setBounds(0, 61, 168, 164);
		prayerPanel.add(prayerScrollPane);
		
		JLabel lblPrayerList = new JLabel("Prayer list:");
		prayerScrollPane.setColumnHeaderView(lblPrayerList);
		JList prayerList = new JList(prayerModel);
		prayerScrollPane.setViewportView(prayerList);
		
		DefaultListModel selectedPrayerModel = new DefaultListModel();
		 selectedPrayerModel.addElement("Strength");
		 selectedPrayerModel.addElement("Prayer");
		 selectedPrayerModel.addElement("J");
		 selectedPrayerModel.addElement("KEOFJ");
		 selectedPrayerModel.addElement("dkfjd");
		JScrollPane selectedPrayerScrollPane = new JScrollPane();
		selectedPrayerScrollPane.setBounds(170, 61, 201, 164);
		prayerPanel.add(selectedPrayerScrollPane);
		JList selectedPrayerList = new JList( selectedPrayerModel);
		selectedPrayerScrollPane.setViewportView(selectedPrayerList);
		
		JLabel lblSelectedPrayers = new JLabel("Selected prayers:");
		selectedPrayerScrollPane.setColumnHeaderView(lblSelectedPrayers);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Food panel
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		JPanel foodPanel = new JPanel();
		tabbedPane.addTab("Food", null, foodPanel, null);
		foodPanel.setLayout(null);
		
		JSpinner eatNumber = new JSpinner();
		eatNumber.setBounds(159, 0, 40, 26);
		foodPanel.add(eatNumber);
		
		JLabel lblPercentToEat = new JLabel("Percent to eat");
		lblPercentToEat.setBounds(6, 6, 113, 15);
		foodPanel.add(lblPercentToEat);
		
		JButton btnNewButton_4 = new JButton("Add");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_4.setBounds(223, 0, 65, 27);
		foodPanel.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Del");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_5.setBounds(289, 0, 65, 27);
		foodPanel.add(btnNewButton_5);
		
		DefaultListModel foodModel = new DefaultListModel();
		foodModel.addElement("Strength");
		foodModel.addElement("Prayer");
		foodModel.addElement("J");
		foodModel.addElement("KEOFJ");
		foodModel.addElement("dkfjd");
		JScrollPane foodScrollPane = new JScrollPane();
		foodScrollPane.setBounds(0, 55, 175, 170);
		foodPanel.add(foodScrollPane);
		
		JLabel lblFood = new JLabel("Food:");
		foodScrollPane.setColumnHeaderView(lblFood);
		JList foodList = new JList(foodModel);
		foodScrollPane.setViewportView(foodList);
		
		DefaultListModel selectedFoodModel = new DefaultListModel();
		selectedFoodModel.addElement("Strength");
		selectedFoodModel.addElement("Prayer");
		selectedFoodModel.addElement("J");
		selectedFoodModel.addElement("KEOFJ");
		selectedFoodModel.addElement("dkfjd");
		JScrollPane selectedFoodPane = new JScrollPane();
		selectedFoodPane.setBounds(179, 55, 188, 170);
		foodPanel.add(selectedFoodPane);
		
		JLabel lblSelected_1 = new JLabel("Selected:");
		selectedFoodPane.setColumnHeaderView(lblSelected_1);
		JList selectedFoodList = new JList(selectedFoodModel);
		selectedFoodPane.setViewportView(selectedFoodList);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(159, 27, 40, 26);
		foodPanel.add(spinner);
		
		JLabel lblfoodToWithdraw = new JLabel("#Food to withdraw");
		lblfoodToWithdraw.setBounds(0, 33, 147, 15);
		foodPanel.add(lblfoodToWithdraw);
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Potions Panel
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		JPanel potionsPanel = new JPanel();
		tabbedPane.addTab("Potions", null, potionsPanel, null);
		
		DefaultListModel potionsModel = new DefaultListModel();
		potionsModel.addElement("Strength");
		potionsModel.addElement("Prayer");
		potionsModel.addElement("J");
		potionsModel.addElement("KEOFJ");
		potionsModel.addElement("dkfjd");
		potionsPanel.setLayout(null);
		
		DefaultListModel selectedPotionsModel = new DefaultListModel();
		selectedPotionsModel.addElement("Strength");
		selectedPotionsModel.addElement("Prayer");
		selectedPotionsModel.addElement("J");
		selectedPotionsModel.addElement("KEOFJ");
		selectedPotionsModel.addElement("dkfjd");
		JScrollPane selectedPotionsPane = new JScrollPane();
		selectedPotionsPane.setBounds(186, 59, 185, 166);
		potionsPanel.add(selectedPotionsPane);
		JList selectedPotionsList = new JList(selectedPotionsModel);
		selectedPotionsPane.setViewportView(selectedPotionsList);
		
		JLabel lblSelectedPotions = new JLabel("Selected potions:");
		selectedPotionsPane.setColumnHeaderView(lblSelectedPotions);
		JScrollPane potionsPane = new JScrollPane();
		potionsPane.setBounds(6, 59, 174, 166);
		potionsPanel.add(potionsPane);
		JList potionsList = new JList(potionsModel);
		potionsPane.setViewportView(potionsList);
		
		JLabel lblPotionsList = new JLabel("Potions list:");
		potionsPane.setColumnHeaderView(lblPotionsList);
		
		JButton btnNewButton_8 = new JButton("Del");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_8.setBounds(271, 6, 82, 27);
		potionsPanel.add(btnNewButton_8);
		
		JButton btnNewButton_9 = new JButton("Add");
		btnNewButton_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_9.setBounds(189, 6, 82, 27);
		potionsPanel.add(btnNewButton_9);
		
		JLabel lblToDrink = new JLabel("% To drink potions");
		lblToDrink.setBounds(6, 12, 116, 15);
		potionsPanel.add(lblToDrink);
		
		JSpinner percentDrinkPotions = new JSpinner();
		percentDrinkPotions.setBounds(134, 6, 40, 26);
		potionsPanel.add(percentDrinkPotions);
		
		JSpinner numPrayerPotions = new JSpinner();
		numPrayerPotions.setBounds(134, 33, 40, 26);
		potionsPanel.add(numPrayerPotions);
		
		JLabel lblprayerPots = new JLabel("#Prayer pots");
		lblprayerPots.setBounds(6, 39, 88, 15);
		potionsPanel.add(lblprayerPots);
		
		JSpinner numPotionSets = new JSpinner();
		numPotionSets.setBounds(313, 33, 40, 26);
		potionsPanel.add(numPotionSets);
		
		JLabel lblsetsPerTrip = new JLabel("#Sets per trip");
		lblsetsPerTrip.setBounds(186, 39, 115, 15);
		potionsPanel.add(lblsetsPerTrip);
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Summoning Panel
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		JPanel summoningPanel = new JPanel();
		tabbedPane.addTab("Summoning", null, summoningPanel, null);
		summoningPanel.setLayout(null);
		
		JLabel label = new JLabel("#");
		label.setBounds(137, 25, 60, 15);
		summoningPanel.add(label);
		
		JLabel lblMonsterToSummon = new JLabel("Summon to bring:");
		lblMonsterToSummon.setBounds(6, 6, 150, 15);
		summoningPanel.add(lblMonsterToSummon);
		
		JSpinner summonNumber = new JSpinner();
		summonNumber.setBounds(149, 19, 40, 26);
		summoningPanel.add(summonNumber);
		
		JCheckBox chckbxUseScrolls = new JCheckBox("Pak Yak Banking");
		chckbxUseScrolls.setBounds(209, 23, 156, 18);
		summoningPanel.add(chckbxUseScrolls);
		
		JComboBox summoningMonsterBox = new JComboBox();
		summoningMonsterBox.setBounds(6, 20, 131, 25);
		summoningPanel.add(summoningMonsterBox);
		
		DefaultListModel dlm4 = new DefaultListModel();
		dlm4.addElement("Strength");
		dlm4.addElement("J");
		dlm4.addElement("KEOFJ");
		dlm4.addElement("dkfjd");
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Loot Panel
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		JPanel lootPanel = new JPanel();
		tabbedPane.addTab("Loot", null, lootPanel, null);
		lootPanel.setLayout(null);
		
		DefaultListModel lootList = new DefaultListModel();
		lootList.addElement("Strength");
		lootList.addElement("Prayer");
		lootList.addElement("J");
		lootList.addElement("KEOFJ");
		lootList.addElement("dkfjd");
		JScrollPane lootPane = new JScrollPane();
		lootPane.setBounds(0, 46, 158, 179);
		lootPanel.add(lootPane);
		
		JLabel lblLootTable = new JLabel("Loot table:");
		lootPane.setColumnHeaderView(lblLootTable);
		JList loot = new JList(lootList);
		lootPane.setViewportView(loot);
		
		DefaultListModel lootList2 = new DefaultListModel();
		lootList2.addElement("Strength");
		lootList2.addElement("Prayer");
		lootList2.addElement("J");
		lootList2.addElement("KEOFJ");
		lootList2.addElement("dkfjd");
		JScrollPane lootPane2 = new JScrollPane();
		lootPane2.setBounds(170, 46, 195, 179);
		lootPanel.add(lootPane2);
		
		JLabel lblSelected = new JLabel("Selected: ");
		lootPane2.setColumnHeaderView(lblSelected);
		ArrayList<Integer> f = new ArrayList<Integer>();
		f.add(3);
		f.add(4);
		JList loot2 = new JList(lootList2);
		lootPane2.setViewportView(loot2);
		
		JButton btnNewButton_2 = new JButton("Add");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_2.setBounds(216, 0, 68, 27);
		lootPanel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Del");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_3.setBounds(286, 0, 68, 27);
		lootPanel.add(btnNewButton_3);
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Settings Panel
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		JPanel settingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingsPanel, null);
		settingsPanel.setLayout(null);
		JScrollPane jsp4 = new JScrollPane();
		jsp4.setBounds(0, 38, 293, 181);
		settingsPanel.add(jsp4);
		JList savedSettings = new JList(dlm4);
		jsp4.setViewportView(savedSettings);
		
		JLabel lblNewLabel = new JLabel("Saved instances");
		jsp4.setColumnHeaderView(lblNewLabel);
		

		JButton btnNewButton_1 = new JButton("Save");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setBounds(187, 5, 106, 27);
		settingsPanel.add(btnNewButton_1);
		
		txtSaveName = new JTextField();
		txtSaveName.setText("Save name");
		txtSaveName.setBounds(59, 5, 122, 27);
		settingsPanel.add(txtSaveName);
		txtSaveName.setColumns(10);
		
		
		JLabel lblSaveAs = new JLabel("Save as:");
		lblSaveAs.setBounds(6, 11, 53, 15);
		settingsPanel.add(lblSaveAs);
		
	}
}

