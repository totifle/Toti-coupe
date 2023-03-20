package ch.totifle.toticoupe.GUI;

import ch.totifle.toticoupe.Toticoupe;
import ch.totifle.toticoupe.bins.ListeManager;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class ImportWizzard extends JFrame implements ActionListener {

    /*************
     * Swings Elements
     *****************/
    private JTable tableMat;
    private JScrollPane matSC;
    private DefaultTableModel modelMat;
    private JComboBox<String> matList;
    private JComboBox<String> matListJTable;
    private JPanel panel;
    private JTextField nomMat;
    private JCheckBox rotatable;
    private JTextField widhtMat;
    private JTextField heightMat;
    private JTextField widhtAff;
    private JTextField thickness;

    private JButton sauvegarderMat;
    private JButton appliquerMat;

    private JLabel labelList;
    private JLabel labelWidth;
    private JLabel labelHeight;
    private JLabel labelAff;
    private JLabel labelThick;
    private JLabel labelNom;
    private JLabel labelSaveName;


    /******
     * parametres materiaudx
     */

    File stockFolder;

    String stockList[], selectStkList[];



    /*************
     * liste Debit
     *****************/
    public HashMap<String, ListeManager> listes = new HashMap<>();

    private Toticoupe main;
    private FileInputStream liste;

    private HSSFWorkbook wb;
    private HSSFSheet sheet;

    private Properties properties = new Properties();
    private FileInputStream propFile;


    private int designRN = 1;
    private int materiauxRN = 2;
    private int nombreRN = 3;
    private int longDebitRN = 4;
    private int largDebitRN = 5;
    private int longFinRN = 8;
    private int largFinRN = 9;
    private int epaisseurRN=6;

    private int[] chantsRN = {11, 12, 13, 14};

    public ImportWizzard(Toticoupe main){
        this.setSize(main.fenetre.getSize());
        this.setVisible(false);
        this.setLocation(800,600);
        this.setResizable(false);
        this.main=main;
        this.setTitle("Toticoupe - Paramètre de la liste");
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        //init folders

        stockFolder = new File("conf/stock");
        updateStock();

        loadConf();
        initSwing();
        updateTextFields();
    }

    private void updateStock(){
        File[] listOfFiles = stockFolder.listFiles();

        assert listOfFiles != null;
        stockList = new String[listOfFiles.length];
        selectStkList = new String[listOfFiles.length+1];
        selectStkList[0] = "Aucun";
        for(int i = 0; i<listOfFiles.length; i++){
            stockList[i] = listOfFiles[i].getName().replace(".totipan", "");
            selectStkList[i+1] = listOfFiles[i].getName().replace(".totipan", "");
        }
    }
    private void initSwing() {
        panel=new JPanel();
        panel.setLayout(null);

        matList = new JComboBox(stockList);
        matList.setSelectedIndex(0);
        matList.addActionListener(this);

        matListJTable = new JComboBox(selectStkList);
        matListJTable.setSelectedIndex(0);
        matListJTable.addActionListener(this);
        labelList = new JLabel("Choisir un matériaux");

        nomMat = new JTextField();
        rotatable = new JCheckBox("Panneau veiné");
        widhtMat = new JTextField();
        heightMat = new JTextField();
        widhtAff = new JTextField();
        thickness = new JTextField();

        labelWidth = new JLabel("Dimentions du panneau");
        labelHeight = new JLabel("X");
        labelThick = new JLabel("X");
        labelAff = new JLabel("Epaisseur affranchissement");
        labelNom = new JLabel("Selectionner un panneau");
        labelSaveName = new JLabel("Nom du panneau");

        sauvegarderMat = new JButton("Sauvgarder");
        sauvegarderMat.addActionListener(this);
        sauvegarderMat.setActionCommand("save");
        appliquerMat = new JButton("Lancer optimisation");
        appliquerMat.addActionListener(this);
        appliquerMat.setActionCommand("opti");


        /***JTables***/

        this.tableMat = new JTable();


        modelMat = new DefaultTableModel();
        matSC = new JScrollPane(tableMat);
        tableMat.setModel(modelMat);
        modelMat.addColumn("Nom liste Débit");
        modelMat.addColumn("Matériaux choisis");

        matSC.setBounds(0,0,getWidth(), 200);
        matList.setBounds(100,300,200,30);
        labelList.setBounds(100,275,200,25);
        labelNom.setBounds(350, 275, 200,25);
        nomMat.setBounds(350,302,200,25);
        labelSaveName.setBounds(350,275,200,25);
        rotatable.setBounds(600,302,200,25);
        widhtMat.setBounds(100,400,200,25);
        heightMat.setBounds(350,400,200,25);
        thickness.setBounds(600,400,50,25);
        widhtAff.setBounds(700,400,200,25);

        labelWidth.setBounds(100,375,200,25);
        labelHeight.setBounds(320,400,200,25);
        labelThick.setBounds(570,400,200,25);
        labelAff.setBounds(700,375,200,25);

        sauvegarderMat.setBounds(600,500,150,30);
        appliquerMat.setBounds(800,500,150,30);

        panel.add(matSC);
        panel.add(matList);
        panel.add(labelList);
        panel.add(nomMat);
        panel.add(rotatable);
        panel.add(widhtMat);
        panel.add(heightMat);
        panel.add(widhtAff);
        panel.add(thickness);
        panel.add(sauvegarderMat);
        panel.add(appliquerMat);
        panel.add(labelWidth);
        panel.add(labelHeight);
        panel.add(labelAff);
        panel.add(labelThick);
        panel.add(labelSaveName);

        add(panel);

        tableMat.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(matListJTable));
        tableMat.setColumnSelectionAllowed(true);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        tableMat.getColumnModel().getColumn(1).setCellRenderer(renderer);
        setIconImage(main.img.getImage());
    }

    public void makeTable(){
        Object[] keys =  listes.keySet().toArray();
        for(int i = 0; i<keys.length;i++){
            modelMat.addRow(new Object[0]);
            ListeManager liste = listes.get(keys[i]);
            modelMat.setValueAt(liste.matName, i, 0);
        }


    }

    private void loadConf() {

        try {
            propFile = new FileInputStream("conf/listeDebit.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            properties.load(propFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.designRN = Integer.valueOf(properties.getProperty("nomPiece").trim());
        this.materiauxRN = Integer.valueOf(properties.getProperty("materiaux").trim());
        this.nombreRN = Integer.valueOf(properties.getProperty("quantite").trim());
        this.longDebitRN = Integer.valueOf(properties.getProperty("longDebit").trim());
        this.largDebitRN = Integer.valueOf(properties.getProperty("largDebit").trim());
        this.longFinRN = Integer.valueOf(properties.getProperty("longFini").trim());
        this.largFinRN = Integer.valueOf(properties.getProperty("largFini").trim());
        this.epaisseurRN = Integer.valueOf(properties.getProperty("epaisseur").trim());

        this.chantsRN[0] = Integer.valueOf(properties.getProperty("chantAv").trim());
        this.chantsRN[1] = Integer.valueOf(properties.getProperty("chantAr").trim());
        this.chantsRN[2] = Integer.valueOf(properties.getProperty("chantG").trim());
        this.chantsRN[3] = Integer.valueOf(properties.getProperty("chantD").trim());




    }

    public void importList() {
        try {
            liste = new FileInputStream(new File(main.path + main.file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            wb = new HSSFWorkbook(liste);
            sheet = wb.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Row row : sheet) {
            if(row.getCell(0).getCellType()==CellType.NUMERIC&&row.getCell(0).getNumericCellValue()>0){
                Cell debWC = row.getCell(longDebitRN);
                debWC.setCellType(CellType.NUMERIC);

                Cell debHC = row.getCell(largDebitRN);
                debHC.setCellType(CellType.NUMERIC);

                Cell nombreC = row.getCell(nombreRN);
                nombreC.setCellType(CellType.NUMERIC);

                Cell matC = row.getCell(materiauxRN);

                Cell epC = row.getCell(epaisseurRN);
                epC.setCellType(CellType.NUMERIC);

                Cell longFC = row.getCell(longFinRN);
                longFC.setCellType(CellType.NUMERIC);

                Cell largFC = row.getCell(largFinRN);
                largFC.setCellType(CellType.NUMERIC);

                Cell nameC = row.getCell(designRN);

                if(nombreC.getNumericCellValue()>0) {


                    float chants[] = new float[4];
                    for (int i = 0; i < chantsRN.length; i++) {
                        chants[i] = (float) row.getCell(chantsRN[i]).getNumericCellValue();
                    }

                    String mat = matC.getStringCellValue().toLowerCase() + (int) epC.getNumericCellValue();

                    getListFromMatName(mat);
                    for (int j = 0; j < nombreC.getNumericCellValue(); j++) {


                        listes.get(mat).addPiece(debWC.getNumericCellValue(), debHC.getNumericCellValue(), longFC.getNumericCellValue(), largFC.getNumericCellValue(),epC.getNumericCellValue(), chants, matC.getStringCellValue(), nameC.getStringCellValue());

                    }
                }

            }
        }
        makeTable();
        setVisible(true);
    }

    private void getListFromMatName(String mat) {
        if(!listes.containsKey(mat)){

            File file = new File("conf/stock/" + mat.toLowerCase() + ".totipan");
            listes.put(mat.toLowerCase(), new ListeManager(file, mat, main));

        }


    }

    public void paint(){

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();

        if(action.equalsIgnoreCase("opti")){

            opti();
        }else if(action.equalsIgnoreCase("save")) {
            String mName = nomMat.getText();
            String mThick = thickness.getText();
            String mW = widhtMat.getText();
            String mH = heightMat.getText();
            String mAff = widhtAff.getText();
            String mR = rotatable.isSelected()==true?"true":"false";
            File matFile = new File("conf\\stock\\"+mName+".totipan");
            try {
                FileOutputStream fos = new FileOutputStream(matFile);
                OutputStreamWriter stringWriter = new OutputStreamWriter(fos);

                stringWriter.write("name="+mName);
                stringWriter.write("\n");
                stringWriter.write("thickness="+mThick);
                stringWriter.write("\n");
                stringWriter.write("width="+mW);
                stringWriter.write("\n");
                stringWriter.write("height="+mH);
                stringWriter.write("\n");
                stringWriter.write("affranchissement="+mAff);
                stringWriter.write("\n");
                stringWriter.write("rotatable="+mR);
                stringWriter.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            updateStock();
            int mli = matList.getSelectedIndex();
            int mji = matListJTable.getSelectedIndex();
            matList.removeAllItems();
            matListJTable.removeAllItems();

            for(String s:stockList) {
                matList.addItem(s);
            }

            for(String s:selectStkList) {
                matListJTable.addItem(s);
            }
            matListJTable.setSelectedIndex(mji);
            matList.setSelectedIndex(mli);


        }else{
            updateTextFields();
        }
    }

    private void updateTextFields(){
        if(matList.getSelectedItem()!=null){
        String selectedPan = ((String)matList.getSelectedItem()).toLowerCase();

        File file = new File("conf/stock/" + selectedPan + ".totipan");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {

            this.nomMat.setText(selectedPan);

            String line = sc.nextLine();

            if (line.contains("width")) {
                this.widhtMat.setText(line.replace("width=", "").trim());
            }

            if (line.contains("height")) {
                this.heightMat.setText(line.replace("height=", "").trim());
            }

            if (line.contains("thickness")) {
                this.thickness.setText(line.replace("thickness=", "").trim());
            }

            if (line.contains("affranchissement")) {
                this.widhtAff.setText(line.replace("affranchissement=", "").trim());
            }

            if (line.contains("rotatable")) {
                String s = line.replace("rotatable=", "").trim();
                this.rotatable.setSelected(s.equalsIgnoreCase("true"));
            }

        }
        }
    }

    private void opti(){
/**
 * avoir le nombre de panneaux differents et les mettre dans une array de listemanager
**/
        HashMap<String, ListeManager> listeManagers = new HashMap<>();
        boolean set = false;

        for(int i = 0; i < tableMat.getRowCount();i++){
            if(tableMat.getValueAt(i,1)!=null) {
                if (!tableMat.getValueAt(i, 1).equals("Aucun")) {

                    /**deeeep copy***/
                    set = true;
                    String userInput = (String) tableMat.getValueAt(i, 1);
                    String idPan = (String) tableMat.getValueAt(i, 0);
                    ListeManager li = listeManagers.get(userInput);
                    HashMap<String, ListeManager> temp = new HashMap<>();
                    Set<Map.Entry<String, ListeManager>> entries = listes.entrySet();

                    for(Map.Entry<String, ListeManager> mapEntry : entries) {
                        temp.put(mapEntry.getKey(), mapEntry.getValue());
                    }


                    if (li == null) {

                        listeManagers.put(userInput, temp.get(idPan));
                    }else{
                        ListeManager liIf = temp.get(idPan);
                        for(int j = 0; j<liIf.pieces.size();j++) {
                            li.addPiece(liIf.pieces.get(j));
                        }


                    }
                }
            }
        }

        if(set) {
            this.setVisible(false);
            main.fenetre.requestFocus();
            ListeManager[] li = listeManagers.values().toArray(new ListeManager[listeManagers.size()]);
            main.workerListes.createListes(li);
        }else{

            JOptionPane.showMessageDialog(this, "Aucun matériaux défini: cliquez sur la colonne de droite pour définir un matériau ou créez en un.", "Aucun matériaux défini", JOptionPane.ERROR_MESSAGE);

        }


    }



}
