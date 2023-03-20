package ch.totifle.toticoupe.IO;

import ch.totifle.toticoupe.Toticoupe;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImportList {

    private final Toticoupe main;
    public FileInputStream liste;

    public HSSFWorkbook wb;
    public HSSFSheet sheet;


    public int designRN = 1;
    public int materiauxRN = 2;
    public int nombreRN = 3;
    public int longDebitRN = 4;
    public int largDebitRN = 5;
    public int longFinRN = 8;
    public int largFinRN = 9;
    public int epaisseurRN=6;

    public int firstRow;
    public int lastRow;

    public ImportList(Toticoupe main){
        this.main = main;


    }

    public void getList(){
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
        for(Row row:sheet){
            for(Cell cell : row){

                if(cell.getCellType()==CellType.FORMULA){
                }

                if(cell.getCellType()== CellType.NUMERIC){
                    if(cell.getColumnIndex()==0){
                        if(cell.getNumericCellValue()==1.0d){
                            firstRow = cell.getRowIndex();
                        }
                    }
                }else if(cell.getCellType()==CellType.STRING){
                    if(cell.getStringCellValue().contains("Remarques:")){
                        lastRow = cell.getRowIndex()-1;
                    }
                }
            }
        }
        for(int i = firstRow; i<= lastRow;i++){
            Row row = sheet.getRow(i);

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


            float chants[] = new float[4];

            for(int j = 0; j<nombreC.getNumericCellValue();j++) {
                //main.binManager.listeManager.addPiece(debWC.getNumericCellValue(), debHC.getNumericCellValue(), longFC.getNumericCellValue(), largFC.getNumericCellValue(), chants, matC.getStringCellValue(), nameC.getStringCellValue());
                ;
            }
        }

    //main.binManager.listeManager.sort();
        //main.binManager.optimise();
    }

}
