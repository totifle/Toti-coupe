package ch.totifle.toticoupe.IO;

import ch.totifle.toticoupe.Toticoupe;
import ch.totifle.toticoupe.bins.ListeManager;
import ch.totifle.toticoupe.bins.Piece;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class ExportCNC extends JFrame implements ActionListener {

    public String fileExtentions[];
    public String supported[];
    public int selectedType = 1;
    private Toticoupe main;

    private JFileChooser fCDir;

    private JPanel pannel;
    private JButton JBAnnuler;
    private JButton JBExporter;
    private JButton JBdir;

    private JLabel labelDir;
    private JLabel labelTemplate;

    private JTextField textDirectory;

    private JComboBox comboTemplates;

    private float surcoteW;
    private float surcoteH;
    private File directory;

    public ExportCNC(Toticoupe main){
        this.main = main;
        getTemplates();
        initSwing();
    }

    private void initSwing() {

        this.setSize(800,200);
        this.setLocation(800,600);
        this.setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.pannel = new JPanel();
        pannel.setLayout(null);
        this.setTitle("Toticoupe - Export CNC");
        this.setVisible(false);


        fCDir = new JFileChooser();
        fCDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        comboTemplates = new JComboBox(supported);
        comboTemplates.addActionListener(this);
        comboTemplates.setActionCommand("comboTemplates");
        comboTemplates.setSelectedIndex(selectedType);
        labelTemplate = new JLabel("Choisir le modèle:");

        JBAnnuler = new JButton("Annuler");
        JBAnnuler.setActionCommand("annuler");
        JBAnnuler.addActionListener(this);

        JBExporter = new JButton("Exporter");
        JBExporter.setActionCommand("exporter");
        JBExporter.addActionListener(this);

        JBdir = new JButton("\u2304");
        JBdir.setActionCommand("dir");
        JBdir.addActionListener(this);

        textDirectory = new JTextField("");
        labelDir = new JLabel("Dossier de destination:");
        int x = this.getWidth()-70;
        int y = this.getHeight()-100;
        JBAnnuler.setBounds(x-230,y,100,30);
        JBExporter.setBounds(x-100,y,100,30);


        labelTemplate.setBounds(x-230, 30,230,30);
        comboTemplates.setBounds(x-230,60,230,30);

        textDirectory.setBounds(50,60,300,30);
        JBdir.setBounds(360,60,30,30);
        labelDir.setBounds(50,30,300,30);

        pannel.add(JBAnnuler);
        pannel.add(JBExporter);
        pannel.add(JBdir);
        pannel.add(comboTemplates);
        pannel.add(labelTemplate);
        pannel.add(textDirectory);
        pannel.add(labelDir);

        this.add(pannel);
        setIconImage(main.img.getImage());
    }

    private void getTemplates() {

        File templateDir = new File("conf/templates");

        File[] listOfFiles = templateDir.listFiles();
        supported = new String[listOfFiles.length];
        fileExtentions = new String[listOfFiles.length];
        for(int i = 0; i<listOfFiles.length; i++){
            supported[i] = listOfFiles[i].getName().replace(".temcn", "");
            try {
                BufferedReader tempR = new BufferedReader(new FileReader(listOfFiles[i]));
                fileExtentions[i] = tempR.readLine().replace("/**/", "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void open(File dir){
        directory = dir;
        textDirectory.setText(dir.getPath());
        this.setVisible(true);
    }

    public void export(){
        this.setVisible(true);
        File template = new File("conf/templates/" + supported[selectedType] + ".temcn");

        for(int i = 0; i<main.workerListes.listes.length;i++){

            ListeManager li = main.workerListes.listes[i];
            String lastPce = "";
            for(int j = 0; j<li.pieces.size();j++){
                Piece pc = li.pieces.get(j);

                if(!lastPce.equals(pc.pieceName)) {
                    surcoteW =(pc.width+pc.chants[Piece.CHANT_G]+pc.chants[Piece.CHANT_D])- pc.finW;
                    surcoteH =(pc.height+pc.chants[Piece.CHANT_AV]+pc.chants[Piece.CHANT_AR])- pc.finH;

                    System.out.println(surcoteW);
                    String fileName = pc.pieceName.replace("/", "-");
                    fileName = fileName.replace(" ", "_");
                    fileName = fileName.replace("\"", "");
                    fileName = fileName.replace(":", "_");
                    File outFile = new File(directory.getPath() + "/" + fileName + "." + fileExtentions[selectedType]);

                    System.out.println(outFile.getPath());
                    try {
                        outFile.createNewFile();
                    } catch (IOException e) {
                    }
                    try {

                        FileWriter writer = new FileWriter(outFile);
                        PrintWriter pr = new PrintWriter(writer);
                        BufferedReader scan = new BufferedReader(new FileReader(template.getPath()));
                        String line = scan.readLine();
                        while (line != null) {
                            String str = line(line, pc);
                            if (!str.equals("")) {
                                pr.println(str);
                            }
                            line = scan.readLine();

                            // pr.println(pc.width + " " + pc.height);

                        }
                        pr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                lastPce = pc.pieceName;
            }
        }
    }

    private String line(String line, Piece pce) {
        if(line.contains("/**/"))return "";



        line = line.replace("{finW}", String.valueOf(pce.finW));
        line = line.replace("{finH}", String.valueOf(pce.finH));
        line = line.replace("{epaisseur}", String.valueOf(pce.epaisseur));
        line = line.replace("{decX}", String.valueOf(surcoteW / 2));
        line = line.replace("{decY}", String.valueOf(surcoteH / 2));
        line = line.replace("{debW}", String.valueOf(pce.width));
        line = line.replace("{debH}", String.valueOf(pce.height));

        line = line.replace("{Chant_AV}", String.valueOf(pce.chants[Piece.CHANT_AV]));
        line = line.replace("{Chant_AR}", String.valueOf(pce.chants[Piece.CHANT_AR]));
        line = line.replace("{Chant_G}", String.valueOf(pce.chants[Piece.CHANT_G]));
        line = line.replace("{Chant_D}", String.valueOf(pce.chants[Piece.CHANT_D]));

        line = line.replace("{outil}", "100");
        line = line.replace("{surCX}", String.valueOf(surcoteW));
        line = line.replace("{surCY}", String.valueOf(surcoteH));



        return line;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();

        if(action.equalsIgnoreCase("exporter")){
            this.setVisible(false);
            main.fenetre.requestFocus();
            export();
        }else if(action.equalsIgnoreCase("annuler")) {
            this.setVisible(false);
        }else if(action.equalsIgnoreCase("comboTemplates")){
            selectedType = comboTemplates.getSelectedIndex();
        }else if(action.equalsIgnoreCase("dir")){
            changeDir();
        }
    }

    private void changeDir() {
        int val = fCDir.showOpenDialog(this);
        if(val == JFileChooser.APPROVE_OPTION) {
            directory = fCDir.getSelectedFile();
            textDirectory.setText(directory.getName());
        }
    }
}
