package ch.totifle.toticoupe.bins;

import ch.totifle.toticoupe.Toticoupe;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ListeManager {

    private Toticoupe main;
    public ArrayList<Piece> pieces;
    public boolean rot;
    public float heightCap, height;
    public float widthCap, width, widhtAff;
    public float thickness;
    public BinManager binManager;
    
    public String matUrl="";
    public String matName="pp19";

    private File file;

    public ListeManager(String url, String matName, Toticoupe main){
        pieces = new ArrayList<>();
        this.matUrl = url;
        this.matName = matName;

        this.main = main;
    }

    public ListeManager(File prop, String matName, Toticoupe main){
        pieces = new ArrayList<>();
        this.matName = matName;

        this.main = main;

        this.file = prop;
        getProperties();
    }

    private void getProperties() {

        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if(line.contains("width")) {
                this.width = Float.valueOf(line.replace("width=", "").trim());
            }

            if(line.contains("height")) {
                this.height = Float.valueOf(line.replace("height=", "").trim());
            }

            if(line.contains("thickness")) {
                this.thickness = Float.valueOf(line.replace("thickness=", "").trim());
            }

            if(line.contains("affranchissement")) {
                this.widhtAff = Float.valueOf(line.replace("affranchissement=", "").trim());
            }

            if(line.contains("rotatable")) {
                String s = line.replace("rotatable=", "").trim();
                this.rot = (s.equalsIgnoreCase("true"));
            }
        }

        widthCap = width-2*widhtAff;
        heightCap = height-2*widhtAff;
        this.binManager = new BinManager(this, main);


    }

    public void addPiece(float width, float height, String name){
        pieces.add(new Piece(width, height, name));
    }
    public void addPiece(double debW, double debH, double fW, double fH, double ep,float chants[], String mat, String name){
        pieces.add(new Piece(debW, debH, fW,fH,ep,chants, mat, name));
    }
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }
    public void randomise(int nbPiece, int maxW, int maxH){
        for(int i = 0; i<nbPiece;i++){
            int w = Math.round((float)Math.random()*maxW+1000);
            int h = Math.round((float)Math.random()*maxH+100);
            addPiece(w,h, w + " x " + h );
        }
    }

    public void trier() {

        Collections.sort(pieces);
    }

    public void paint(Graphics2D g, Rectangle b){
        binManager.paint(g, b);
    }


}
