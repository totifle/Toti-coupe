package ch.totifle.toticoupe.bins;

import ch.totifle.toticoupe.Toticoupe;

import java.awt.*;

public class WorkerListes {

    private final Toticoupe main;
    public ListeManager[] listes;
    public int listeI = 0;
    public int planI = 0;

    public WorkerListes(Toticoupe main){
        this.main = main;
    }

    public void createListes(ListeManager[] listes){
        this.listes = listes;
        for(int i = 0; i<listes.length;i++){

            listes[i].trier();
            listes[i].binManager.optimise();

        }

    }

    public void paint(Graphics2D g, Rectangle b){

        if(listes==null)return;
        if(planI>=listes.length)planI=0;
        if(planI<0)planI=listes.length-1;
        if(listes[planI]==null)return;
        listes[planI].paint(g,b);

    }


}
