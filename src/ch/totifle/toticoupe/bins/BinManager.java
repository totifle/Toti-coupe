package ch.totifle.toticoupe.bins;

import ch.totifle.toticoupe.Toticoupe;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BinManager {

    public Toticoupe main;
    public ArrayList<SubBin> bins;
    public float width, height, widthAff;
    public boolean rotatable, init = false;

    public ListeManager listeManager;

    public BinManager(ListeManager listeManager, Toticoupe main){
        this.listeManager = listeManager;
        this.main=main;

        this.width = listeManager.widthCap;
        this.height = listeManager.heightCap;
        this.rotatable = listeManager.rot;
        this.widthAff = listeManager.widhtAff;
        this.bins = new ArrayList<>();
    }

    public BinManager(boolean rotatable, float width, float height, Toticoupe main){
        this.rotatable = rotatable;
        this.width = width;
        this.height = height;

        this.main = main;

        this.bins = new ArrayList<>();

        //this.listeManager = new ListeManager();
        /*listeManager.addPiece(2100,200,"2050");
        listeManager.addPiece(1800, 200,"1800");
        listeManager.addPiece(200, 150,"200 150 1");
        listeManager.addPiece(200, 150,"200 150 2");*/



        //listeManager.randomise(10, 1500, 500);
        //listeManager.randomise(10, 1, 500);
       //listeManager.sort();

    }

    public void optimise(){
        Toticoupe.log(0, "Optimisation start");
        for(int i = 0; i<listeManager.pieces.size(); i++){
            boolean binIndex = false;
            for(int j = 0; j<bins.size();j++){
                if(bins.get(j).canFit(listeManager.pieces.get(i), rotatable)){
                    binIndex = true;
                }
            }
            if(!binIndex){
                bins.add(new SubBin(width, height, 0,0, main));
                bins.get(bins.size()-1).canFit(listeManager.pieces.get(i), rotatable);
            }
        }

        init=true;

        float totalAera = 0;
        for(int i = 0; i<bins.size(); i++){
            Toticoupe.log(0, "Cutoffs for bin " + i + " : " + (bins.get(i).getChutte()));
            totalAera += bins.get(i).getChutte();

        }
        Toticoupe.log(0, "Total aera of cutoffs: " + (totalAera));

        Toticoupe.log(0, "Optimisation successfully ended");
    }

    public void paint(Graphics g, Rectangle bounds){
       /* for(int i = 0; i<bins.size(); i++){
            bins.get(i).paint(g);
        }*/
        if(!init) return;
        float scale1 =  (bounds.width-50)/width;
        float scale2 =  (bounds.height-50)/height;
        float scale = (scale1<scale2 ? scale1 : scale2);

        int xOff = bounds.x + Math.round((bounds.width-width*scale)/2);
        int yOff = bounds.y + Math.round((bounds.height-height*scale)/2);

        g.setColor(main.scene.bgPan);

        g.fillRect(xOff, yOff, Math.round(width*scale), Math.round(height*scale));
        g.drawRect(xOff, yOff, Math.round(width*scale/0), Math.round(height*scale));

        if(main.workerListes.listeI<bins.size()){
            bins.get(main.workerListes.listeI).paint(g, bounds, scale, xOff, yOff);
        }else{
            try{
                bins.get(0).paint(g, bounds, scale, xOff, yOff);
            }catch (IndexOutOfBoundsException e){

            }
        }
    }
}
