package ydh.upload.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelUploader {

	public static String getCellValue(Cell cell){
		String cellValue = "";
		if (null != cell) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
				cellValue = new BigDecimal(cell.getNumericCellValue()) + "";
				break;
			case HSSFCell.CELL_TYPE_STRING: // 字符串
				cellValue = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				cellValue = cell.getBooleanCellValue() + "";
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				cellValue = cell.getCellFormula() + "";
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
				cellValue = "";
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 故障
				cellValue = "非法字符";
				break;
			default:
				cellValue = "未知类型";
				break;
			}
		}
		return cellValue;
	}
	
	public static Workbook getWorkbookByFile(MultipartFile fileUpload) throws IOException{
		String filename = fileUpload.getOriginalFilename();
		String type = filename.substring(filename.lastIndexOf(".") + 1);
		InputStream inputStream = fileUpload.getInputStream();
		Workbook workbook = null;
		if ("xls".equals(type)) {
			workbook = new HSSFWorkbook(inputStream);
		} else if ("xlsx".equals(type)) {
			workbook = new XSSFWorkbook(inputStream);
		}
		return workbook;
	}
	
	public static int getTotalCellNum(Workbook workbook){
		Sheet sheet = workbook.getSheetAt(0);
		int totalRows = sheet.getPhysicalNumberOfRows();
		int totalCells = 0;
		if (totalRows >= 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		totalCells = totalCells > 6 ? 6 : totalCells;
		return totalCells;
	}
}
