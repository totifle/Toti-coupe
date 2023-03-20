package ch.totifle.toticoupe.bins;

import ch.totifle.toticoupe.Toticoupe;

import java.awt.*;

public class SubBin {

    public SubBin subBin1;
    public SubBin subBin2;

    public Piece fitted;
    public boolean cutted = false;
    public boolean horisontalCut;

    public float width, height, posX, posY;
    public float cut1height, cut2height;
    public Toticoupe main;

    public float lame = 0;


    public SubBin(float width, float height, float posX, float posY, Toticoupe main) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.main = main;
    }

    public float getChutte(){
        if(!cutted){
            if(width *height/1000.0/1000.0>0){
                Toticoupe.log(0, "Cutoff : " + width *height/1000.0/1000.0);
            }

            return this.width/1000*this.height/1000;
        }
        return subBin1.getChutte() + subBin2.getChutte();
    }

    public void cut(Piece piece, boolean rotatable) {
        float remW = this.width - piece.width;
        float remH = this.height - piece.height;
        float remWR = this.width - piece.height;
        float remHR = this.height - piece.width;
        boolean rotate = false;

        if (rotatable) {
            float area1 = (this.height - piece.height) * (piece.width);
            float area2 = (this.width - piece.width) * (this.height);

            float area1R = (this.height - piece.width) * (piece.height);
            float area2R = (this.width - piece.height) * (this.height);

            float difNR = area2 + area1;
            float difR = area2R + area1R;

            if (difR > difNR || area1 <0 || area2<0) {

                if(this.width >= piece.height && this.height >= piece.width) {
                    rotate = true;
                }
            }



        }
        if (rotate) {
            if (remWR>remHR) {
                //System.out.print(piece.pieceName + " rotated, vertical");
                this.horisontalCut = false;
                this.subBin1 = new SubBin(this.width - piece.height - this.lame, this.height, this.posX + piece.height + lame, this.posY, main);
                this.subBin2 = new SubBin(piece.height, this.height - piece.width - this.lame, this.posX, this.posY + piece.width, main);
                this.cut1height = piece.height;
                this.cut2height = piece.width;

            } else {
                //System.out.print(piece.pieceName + " rotated, horisontal");
                this.horisontalCut = true;
                this.subBin1 = new SubBin(this.width, this.height - piece.width - this.lame, this.posX, this.posY + piece.width + this.lame, main);
                this.subBin2 = new SubBin(this.width - piece.height - this.lame, piece.width, this.posX + this.lame + piece.height, this.posY, main);
                this.cut1height = piece.width;
                this.cut2height = piece.height;

            }
        } else {
            if (remW>remH) {
                //System.out.print(piece.pieceName + " not, vertical");
                this.horisontalCut = false;
                this.subBin1 = new SubBin(this.width- piece.width - this.lame, this.height, this.posX + piece.width + this.lame, this.posY, main);
                this.subBin2 = new SubBin(piece.width, this.height - piece.height - this.lame, this.posX, this.posY + piece.height + this.lame, main);
                this.cut1height = piece.width;
                this.cut2height = piece.height;

            } else {
                //System.out.print(piece.pieceName + " not, horisontal");
                this.horisontalCut = true;
                this.subBin1 = new SubBin(this.width, this.height - piece.height - this.lame, this.posX, this.posY + piece.height + this.lame, main);
                this.subBin2 = new SubBin(this.width - piece.width - this.lame, piece.height, this.posX + this.lame + piece.width, this.posY, main);
                this.cut1height = piece.height;
                this.cut2height = piece.width;

            }
        }
        /*System.out.print(" in bin dim:" + width + " x " + height);
        System.out.println("");*/
            cutted = true;
            this.fitted = piece;
            /*System.out.println(subBin1.width + "  " + subBin1.height);
            System.out.println(subBin2.width + "  " + subBin2.height);*/


    }



    public boolean canFit(Piece piece, boolean rot) {
        if(cutted) {
            if (this.subBin2.canFit(piece, rot) == true) return true;
            if (this.subBin1.canFit(piece, rot) == true) return true;



        }else {
            if (rawFit(this.width, this.height, piece, rot)) return true;
        }

        return false;
    }

    /*private boolean fitUncutted(Piece piece, boolean rot) {
        float remX;
        float remY;
        if(horisontalCut){
            remX = this.width - this.fitted.width;
            remY = (this.height - cutHeight) - this.fitted.height;
        }else{
            remX = this.width-this.cutHeight-this.fitted.width;
            remY = this.height - this.fitted.height;
        }


        return rawFit(remX, remY, piece.width, piece.height, rot);



    }
*/
    /*******************
     *
     * Optimisation necessaire !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     *
     * ***********************/

    private boolean rawFit(float remX, float remY, Piece piece, boolean rot) {

        if(rot){
            if((remX>=piece.height&&remY>=piece.width)||(remX>=piece.width&&remY>=piece.height)){
                cut(piece, rot);
                return true;
            }
        }else{
            if(remX>=piece.width&&remY>=piece.height){
                cut(piece, rot);
                return true;
            }
        }

        return false;

    }

    public void paint(Graphics g, Rectangle bounds, float scale, int xOff, int yOff){

        if(cutted) {

            if (horisontalCut) {
                g.setColor(main.scene.panDec);
                g.fillRect(Math.round((posX)*scale) + xOff, Math.round((posY)*scale) + yOff, Math.round((cut2height)*scale), Math.round((cut1height)*scale));
                //g.fillRect(Math.round(posX), Math.round(posY), Math.round(cut2height), Math.round(height));
                g.setColor(main.scene.scie);
                g.drawLine(Math.round((posX)*scale) + xOff, Math.round((posY + cut1height)*scale) + yOff, Math.round((posX + width)*scale) + xOff, Math.round((posY + cut1height)*scale) + yOff);
                g.drawLine(Math.round((posX + cut2height)*scale) + xOff, Math.round((posY)*scale) + yOff, Math.round((posX + cut2height)*scale) + xOff, Math.round((posY + cut1height)*scale) + yOff);


            } else {
                g.setColor(main.scene.panDec);
                g.fillRect(Math.round((posX)*scale) + xOff, Math.round((posY)*scale) + yOff, Math.round((cut1height)*scale), Math.round((cut2height)*scale));
                //g.fillRect(Math.round(posX), Math.round(posY), Math.round(width), Math.round(cut2height));
                g.setColor(main.scene.scie);
                g.drawLine(Math.round((posX + cut1height)*scale) + xOff, Math.round((posY)*scale) + yOff, Math.round((posX + cut1height)*scale) + xOff, Math.round((posY + height)*scale) + yOff);
                g.drawLine(Math.round((posX)*scale) + xOff, Math.round((posY + cut2height)*scale) + yOff, Math.round((posX + cut1height)*scale) + xOff, Math.round((posY + cut2height)*scale + yOff));


            }
            g.drawString(fitted.pieceName, Math.round((posX)*scale+xOff) + 5, Math.round((posY)*scale+yOff) + 20);
        }


        if(this.subBin1!=null)subBin1.paint(g, bounds, scale, xOff, yOff);
        if(this.subBin2!=null)subBin2.paint(g, bounds, scale, xOff, yOff);
    }
}
