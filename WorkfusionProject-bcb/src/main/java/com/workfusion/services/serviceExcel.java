package com.workfusion.services;

import com.workfusion.utils.baseClase;
import com.workfusion.entities.Account;
import com.workfusion.utils.Constant;

import com.workfusion.rpa.helpers.Excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.workfusion.utils.Utils;

public class serviceExcel extends baseClase {
	
	public final static String path = Constant.PATHOUTPUT;
	
	int fila = 1;
	
	public serviceExcel() {
		if (Utils.fileExists(path)) Utils.fileToDelete(path);
		crearExcel();
	}
	
	
	
	public void crearExcel() {
		
        // Instantiate a new Excel workbook instance
        Workbook ExcelWorkbook = new XSSFWorkbook();

        // Get reference to first worksheet in the workbook
        Sheet ExcelWorksheet = ExcelWorkbook.createSheet();

        // Get reference to Cells collection in the first worksheet
        Row row = ExcelWorksheet.createRow(0);
        
        CellStyle headerStyle = ExcelWorkbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
       // headerStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        //headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) ExcelWorkbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);
        
        int cols = 0;
        for (String cab : Constant.CABEXCEL) {
        	Cell cell = row.createCell(cols);
        	cell.setCellValue(cab);
        	cell.setCellStyle(headerStyle);
        	cols++;
        }
		
		
        try {
            //Se genera el documento
            FileOutputStream out = new FileOutputStream(new File(path));
            ExcelWorkbook.write(out);
            out.close();
        } catch (FileNotFoundException  e) {
            e.printStackTrace();
        } catch (IOException ex) {
        	 ex.printStackTrace();
        }
		
	}
	
	
	public void editarExcel(List<Account> lista) {
		
		Excel.openExcel(path);
		
		fila = 2;
		
		lista.forEach(reg -> {
			
			Excel.setCell(path, "A" + fila , reg.getAccountID());
			Excel.setCell(path, "B" + fila , reg.getAmount()); // .split(" ")[0]);
			Excel.setCell(path, "C" + fila , reg.getStatus());
			fila++;
			
		});
		
		Excel.saveExcel(path);
		
		Excel.closeExcel(path);
		
	}

}