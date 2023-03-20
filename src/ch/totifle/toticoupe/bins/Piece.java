package ch.totifle.toticoupe.bins;

public class Piece implements Comparable {

    public static final int CHANT_AV = 0;
    public static final int CHANT_AR = 1;
    public static final int CHANT_G = 2;
    public static final int CHANT_D = 3;
    public float width, height;

    public String pieceName;
    public String materiaux;
    public float finW, finH, epaisseur;
    public float chants[] = new float[4];



    public Piece(float width, float height, String pieceName){

        this.width = width;
        this.height = height;
        this.pieceName = pieceName;
    }

    public Piece(double debW, double debH, double fW, double fH,double epaisseur, float chants[], String mat, String name){
        this.width = (float)debW;
        this.height = (float)debH;
        this.finW = (float)fW;
        this.finH = (float)fH;
        this.epaisseur = (float)epaisseur;
        this.chants = chants;
        this.materiaux = mat;
        this.pieceName = name;
    }

    @Override
    public int compareTo(Object o) {
        Piece other = (Piece)o;
        float calc;
        float oCalc;
        if(width>height){
            calc = width;
        }else{
            calc=height;
        }

        if(other.width>other.height){
            oCalc = other.width;
        }else{
            oCalc=other.height;
        }

        //return(Math.round(other.width*other.height-this.width*this.height));
        return(Math.round(oCalc-calc));
    }
}
