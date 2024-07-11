package com.nguyenanhtuyen.admin.exporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nguyenanhtuyen.common.entity.Brand;

import jakarta.servlet.http.HttpServletResponse;

public class BrandPdfExporter extends AbstractExporter {

	public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "application/pdf", ".pdf", "brands_");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(14);
		font.setColor(Color.BLUE);

		Paragraph paragraph = new Paragraph("List of Brands", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		writeTableHeader(table);
		writeTableData(table, listBrands);
		document.add(table);

		document.close();
	}

	private void writeTableData(PdfPTable table, List<Brand> listBrands) {
		for (Brand brand : listBrands) {
			table.addCell(String.valueOf(brand.getId()));
			table.addCell(brand.getName());
			table.addCell(brand.getCategories().toString());
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		font.setSize(12);

		cell.setPhrase(new Phrase("Brand ID", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Brand Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Categories", font));
		table.addCell(cell);
	}
}
