/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ferhat
 */
public class ExcelReader {

    public static List<Hastalık> readHastalık(String fileName) {
        List<Hastalık> list = new ArrayList<>();

        try {
            File file = new File(fileName);   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file
            while (itr.hasNext()) {
                Row row = itr.next();
                String name = getCell(row, 0);
                String b1 = getCell(row,1);
                String b2 = getCell(row,2);
                String n1 = getCell(row,3);
                String n2 = getCell(row,4);
                list.add(new Hastalık(name, b1, b2, n1, n2));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return list;
    }

    public static String getCell(Row row, int index) {
        if (row.getCell(index) != null) {
            return row.getCell(index).getStringCellValue();
        } else {
           return  null;
        }
    }

}
