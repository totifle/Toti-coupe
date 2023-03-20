package ch.totifle.toticoupe.IO;

import ch.totifle.toticoupe.Toticoupe;

import javax.print.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;

public class PlanDeCoupePrnt {

    private Toticoupe main;
    private final float DPI = 72;
    public PlanDeCoupePrnt(Toticoupe main) {
        this.main = main;
    }

    public void print(){
        PrinterJob pj = PrinterJob.getPrinterJob();

        Image imag = getImageFromPannel();


        float width = cmToPixel(29.7f, DPI);
        float height = cmToPixel(21f, DPI);

        Paper paper = new Paper();
        float margin = cmToPixel(1, DPI);
        paper.setImageableArea(margin, margin, width - (margin * 2), height - (margin * 2));
        PageFormat pf = new PageFormat();
        pf.setPaper(paper);

        pj.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex != 0) {
                    return NO_SUCH_PAGE;
                }

                graphics.drawImage(imag, 0, 0, imag.getWidth(null), imag.getHeight(null), null);

                return PAGE_EXISTS;
            }
        });

        if(pj.printDialog()) {
            try {
                pj.print();
            } catch (PrinterException ex) {
                System.out.println(ex);
            }
        }

    }

    private Image getImageFromPannel() {
        BufferedImage image = new BufferedImage(main.scene.getWidth(), main.scene.getHeight(), BufferedImage.TYPE_INT_RGB);

        main.scene.print(image.getGraphics());
        return image;


    }

    public float cmToPixel(float cm, float dpi) {

        return (dpi / 2.54f) * cm;

    }
}

