package com.vein.util;

import java.awt.Color;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIUtil {
	public static Font setFont(XSSFWorkbook workbook,short fontSize,String fontName,boolean bold){
		Font font = workbook.createFont();
		//设置字体样式
		font.setFontName(fontName);
		//设置字体大小
		font.setFontHeightInPoints(fontSize);
		//是否加粗
		font.setBold(bold);
		return font;
	}
	
	public static XSSFCellStyle setStyle(XSSFWorkbook workbook,HorizontalAlignment alignment,VerticalAlignment verticalAlignment,Font font,short index){
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(alignment);
		style.setVerticalAlignment(verticalAlignment);
		style.setFont(font);
		style.setFillBackgroundColor(index);
		return style;
	}
	
	public static XSSFCellStyle setStyle(XSSFWorkbook workbook,HorizontalAlignment alignment,VerticalAlignment verticalAlignment,Font font){
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(alignment);
		style.setVerticalAlignment(verticalAlignment);
		style.setFont(font);
		return style;
	}
	
	
	public static XSSFCellStyle addBorder(XSSFCellStyle style,int top,int bottom,int left,int right){
		if(top==0){
			style.setBorderTop(BorderStyle.MEDIUM);
		}
		if(bottom==0){
			style.setBorderBottom(BorderStyle.MEDIUM);
		}
		if(left==0){
			style.setBorderLeft(BorderStyle.MEDIUM);
		}
		if(right==0){
			style.setBorderRight(BorderStyle.MEDIUM);
		}
		return style;
	}
	
	
	
	
	
	
	public static void mergeCell(XSSFSheet sheet,CellRangeAddress cellRangeAddress){
		sheet.addMergedRegion(cellRangeAddress);
	}
	
	
	public static XSSFRow createRow(XSSFSheet sheet,int rowNum){
		XSSFRow row = sheet.createRow(rowNum);
		return row;
	}
	
	
	public static void cellFilling(XSSFCell cell,XSSFCellStyle style,String value){
		cell.setCellStyle(style);
		cell.setCellValue(value);
	}
	
}
