package ch.totifle.toticoupe.GUI;

import ch.totifle.toticoupe.Toticoupe;

import javax.swing.*;
import java.awt.*;


public class Scene extends JPanel{

    public Toticoupe main;
    private JPanel plansDeCoupe;
    private JPanel panneaux;
    private JPanel statistiques;
    private JPanel decoupes;
    private long lastResize;
    private Rectangle rect = new Rectangle(0,0,0,0);

    public Color bgPan = new Color(180,180,180);
    public Color panDec = new Color(100,100,100);
    public Color scie = Color.black;

    private JTable tablePlansCoupes;


    public Scene(Toticoupe main){

        setVisible(false);
        this.main = main;

        init();

        setBackground(Color.decode("#2f3136"));
        //setLayout(new BorderLayout());


    }
    private void init(){

        this.statistiques = new JPanel();

        this.panneaux = new JPanel();

        this.decoupes = new JPanel();

        this.plansDeCoupe = new JPanel();

        tablePlansCoupes = new JTable();



        this.add(statistiques);
        this.add(panneaux);
        this.add(decoupes);
        this.add(plansDeCoupe);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));

        g2.drawLine(rect.x, 0, rect.x, main.fenetre.getHeight());
        g2.drawLine(0, rect.y, main.fenetre.getWidth(), rect.y);

        if(main.workerListes!=null) main.workerListes.paint(g2, rect);


        g2.dispose();
        if(System.nanoTime()-lastResize<20){
            resized();
        }

    }


    public void resized() {
        lastResize = System.nanoTime();

        float sepHor = 0.4f;
        float sepVer = 0.3f;
        int heightTop = Math.round(getHeight()* sepHor);
        int heightBot = Math.round(getHeight()-heightTop);

        int widthL = Math.round(getWidth()* sepVer);
        int widthR = Math.round(getWidth()-widthL);

        statistiques.setBounds(0,0, widthL ,heightTop);

        panneaux.setBounds(widthL,0, widthR,heightTop);

        decoupes.setBounds(0,heightTop, widthL ,heightBot);

        plansDeCoupe.setBounds(widthL,heightTop, widthR ,heightBot);

        rect.setBounds(widthL, heightTop, widthR, heightBot);

    }
}
