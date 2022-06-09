package com.thales.ERB.Excel;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.thales.ERB.entity.Referral;

public class ReferralDataExcelExport {

	private XSSFWorkbook workbook;
	
	private XSSFSheet sheet;
	
	private List<Referral> listReferrals;

	// create new workbook and sheet
	public ReferralDataExcelExport(List<Referral> listReferrals) {
		this.listReferrals = listReferrals;
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Referrals");
	}

	// creating header row
	private void writeHeaderRow() {
		Row row = sheet.createRow(0);

		row.createCell(0).setCellValue("Referee TGI/SGI");
		row.createCell(1).setCellValue("Referee Name");
		row.createCell(2).setCellValue("Date of Referral");
		row.createCell(3).setCellValue("Referral Name");
		row.createCell(4).setCellValue("Referral Status");
		row.createCell(5).setCellValue("Referral TGI/SGI");
		row.createCell(6).setCellValue("Position Offered");
		row.createCell(7).setCellValue("Diversity");
		row.createCell(8).setCellValue("Date of Joining");
		row.createCell(9).setCellValue("Probation Completion Date");
		/*
		 * row.createCell(9).setCellValue("Offer Accepted");
		 * row.createCell(10).setCellValue("Onboarded");
		 * row.createCell(11).setCellValue("Probation Completed");
		 */
	}

	private void writeDataRows() {
		int rowCount = 1;
		for (Referral referral : listReferrals) {
			Row row = sheet.createRow(rowCount++);
			row.createCell(0).setCellValue(referral.getEmployee().getEmployeeTGI());
			sheet.autoSizeColumn(0);

			row.createCell(1).setCellValue(referral.getEmployee().getEmployeeName());
			sheet.autoSizeColumn(1);

			// Date of Referral
			CellStyle cellStyle = workbook.createCellStyle();
			CreationHelper createHelper = workbook.getCreationHelper();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("MM/dd/yyyy"));

			Cell cell = row.createCell(2);
			cell.setCellValue(referral.getDateOfReferral());
			cell.setCellStyle(cellStyle);
			sheet.autoSizeColumn(2);

			row.createCell(3).setCellValue(referral.getReferralName());
			sheet.autoSizeColumn(3);
			
			row.createCell(4).setCellValue(referral.getReferralStatus());
			sheet.autoSizeColumn(4);

			row.createCell(5).setCellValue(referral.getReferralTGI());
			sheet.autoSizeColumn(5);

			row.createCell(6).setCellValue(referral.getPositionOffered());
			sheet.autoSizeColumn(6);

			row.createCell(7).setCellValue(referral.getDiversity());
			sheet.autoSizeColumn(7);

			// Date of Joining
			Cell cell8 = row.createCell(8);
			cell8.setCellValue(referral.getDateOfJoining());
			cell8.setCellStyle(cellStyle);
			sheet.autoSizeColumn(8);

			// Date of Probation
			Cell cell9 = row.createCell(9);
			cell9.setCellValue(referral.getDateOfProbation());
			cell9.setCellStyle(cellStyle);
			sheet.autoSizeColumn(9);

			/*
			 * row.createCell(9).setCellValue(referral.getOffer()); sheet.autoSizeColumn(9);
			 * row.createCell(10).setCellValue(referral.getOnboarded());
			 * sheet.autoSizeColumn(10);
			 * row.createCell(11).setCellValue(referral.getProbation());
			 * sheet.autoSizeColumn(11);
			 */
		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderRow();
		writeDataRows();
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

}
