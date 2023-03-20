package ch.totifle.toticoupe;

import ch.totifle.toticoupe.GUI.ImportWizzard;
import ch.totifle.toticoupe.GUI.Scene;
import ch.totifle.toticoupe.IO.ExportCNC;
import ch.totifle.toticoupe.IO.PlanDeCoupePrnt;
import ch.totifle.toticoupe.bins.WorkerListes;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.SQLOutput;

public class Toticoupe extends Thread{

    public JFrame fenetre;
    public Scene scene;
    private long lastTime;
    //public BinManager binManager = new BinManager(true, 2800.0f, 2070.0f, this);
    public WorkerListes workerListes;
    private FileDialog fc;
    private JFileChooser exportDirFc;

    private PlanDeCoupePrnt pdcPrinter;


    public String path;
    public String file;

    public ImageIcon img = new ImageIcon(getClass().getResource("/utils/ico.png"));

    private ImportWizzard importWizzard;

    private ExportCNC postProcess;




    public Toticoupe(){

    }

    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
        }


        fenetre = new JFrame("toti coupe");
        fenetre.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenetre.setSize(1000,800);
        fenetre.setLocationRelativeTo(null);

        fc  = new FileDialog(fenetre, "Choisir liste de débitage", FileDialog.LOAD);
        exportDirFc = new JFileChooser();
        exportDirFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        fenetre.setResizable(true);
        fenetre.setFocusable(true);
        fenetre.requestFocusInWindow();
        fenetre.setIconImage(img.getImage());

        fenetre.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                if(e.getKeyChar()=='a'){
                    workerListes.planI++;
                }else if (e.getKeyChar()=='d'){
                    workerListes.planI--;
                }
                try{
                    workerListes.listeI = Integer.valueOf(""+(e.getKeyChar()));

                }catch (Exception ignored){
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });


        JMenuBar mb = new JMenuBar();

        JMenu mbFile = new JMenu("Fichier");
        JMenu mbExport = new JMenu("Export");
        JMenuItem exporter = new JMenuItem(new AbstractAction("CNC") {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(workerListes.listes==null||workerListes.listes.length==0){
                    infoBox("Aucune liste de débit: Importez une liste de débit via l'onglet Fichier", "Aucune liste de débit importée");
                    return;
                }
                int retVal = exportDirFc.showOpenDialog(fenetre);
                if(retVal == JFileChooser.APPROVE_OPTION) {
                    postProcess.open(exportDirFc.getSelectedFile());
                }


            }
        });

        JMenuItem opti = new JMenuItem(new AbstractAction("Optimisation") {
            @Override
            public void actionPerformed(ActionEvent e) {

                pdcPrinter.print();


            }
        });

        JMenuItem ouvrir = new JMenuItem(new AbstractAction("Ouvrir") {
            @Override
            public void actionPerformed(ActionEvent e) {

                fc.setVisible(true);
                if(fc.getFile()!=null) {

                    path = fc.getDirectory();
                    file = fc.getFile();

                    importWizzard.importList();
                }else{

                }

            }
        });

        JMenuItem parametre = new JMenuItem(new AbstractAction("Paramètres") {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

        JMenuItem stock = new JMenuItem(new AbstractAction("Stock") {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });

        mb.add(mbFile);
        mb.add(mbExport);
        mbFile.add(ouvrir);
        mbFile.add(parametre);

        mbExport.add(exporter);
        mbExport.add(opti);

        fenetre.setJMenuBar(mb);
        this.scene = new Scene(this);

        fenetre.setContentPane(scene);

        scene.setVisible(true);
        //scene.revalidate();
        fenetre.setVisible(true);

        workerListes = new WorkerListes(this);
        importWizzard = new ImportWizzard(this);
        postProcess = new ExportCNC(this);

        pdcPrinter = new PlanDeCoupePrnt(this);

        Thread thread = new Thread(this);
        thread.start();

    }

    public void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(fenetre, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
    }

    public void tick(){
        scene.resized();
        scene.repaint();
        if(importWizzard.isVisible()){
            fenetre.setEnabled(false);
        }else if(postProcess.isVisible()){
            fenetre.setEnabled(false);
        }else{
            fenetre.setEnabled(true);
        }
    }

    @Override
    public void run() {
        while (true) {
            tick();
            while (System.nanoTime() - lastTime < 1000000000 / 25) {
            }
            lastTime = System.nanoTime();
        }

    }

    public static void log(int status, String message){
        String out = "[Toti-coupe]";
        if(status == 0){
            out+= "[INFO]";
        }else if(status == 1){
            out+= "[WARN]";
        }else if(status == 2){
            out+= "[ERROR]";
        }
        long milliseconds = System.currentTimeMillis();


        int hours   = (int) ((milliseconds / (1000*60*60)));
        int minutes = (int) ((milliseconds / (1000*60)));
        float seconds = (int) (milliseconds  - minutes*60*1000)/ 1000 ;
        out += "[" + hours%24 + ":" + minutes%60 + ":" + seconds+"] : " + message;
        System.out.println(out);
    }

}
