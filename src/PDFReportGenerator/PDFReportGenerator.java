package PDFReportGenerator;

import Domain.statistica_film;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Alex on 05.01.2017.
 */
public class PDFReportGenerator
{

    public PDFReportGenerator()
    {}

    public static void createPdf(String dest, List<statistica_film> stat) throws IOException {

        File file = new File(dest);
        file.getParentFile().mkdirs();

        //Initialize PDF writer
        PdfWriter writer = new PdfWriter(dest);

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Initialize document
        Document document = new Document(pdf);

        //Add paragraph to the document
        /*Paragraph paragraph=new Paragraph("Hello World!");
        document.add(paragraph);*/

        float[] columnWidths = {75,200,200};
        Table table = new Table(columnWidths);
        table.addHeaderCell("Nr. crt.");
        table.addHeaderCell("Nr. inchirieri");
        table.addHeaderCell("Titlu");

        int nrcrt=1;
        for(statistica_film s:stat)
        {
            table.addCell(Integer.toString(nrcrt));
            table.addCell(Integer.toString(s.getInch()));
            table.addCell(s.getTitlu());
            nrcrt++;
        }

        document.add(table);

        //Close document
        document.close();
    }
}
