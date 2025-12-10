package crs.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import crs.model.*;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AcademicReportGenerator {

    private static final Font TITLE_FONT =
            new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT =
            new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
    private static final Font TEXT_FONT =
            new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

    // =====================================================
    // Generate PDF (Student Info + Course Results ONLY)
    // =====================================================
    public void generate(Student student, String outputPath) throws Exception {

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        // -------------------------------------------------
        // TITLE
        // -------------------------------------------------
        Paragraph title = new Paragraph("Student Academic Report", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph date = new Paragraph(
                "Generated: " + new SimpleDateFormat("dd MMM yyyy, HH:mm").format(new Date()),
                TEXT_FONT
        );
        date.setAlignment(Element.ALIGN_CENTER);
        document.add(date);
        document.add(Chunk.NEWLINE);

        // -------------------------------------------------
        // STUDENT INFO
        // -------------------------------------------------
        document.add(new Paragraph("Student Information", HEADER_FONT));
        document.add(Chunk.NEWLINE);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);

        addKeyValue(infoTable, "Student ID:", student.getStudentId());
        addKeyValue(infoTable, "Name:", student.getFullName());
        addKeyValue(infoTable, "Major:", student.getMajor());
        addKeyValue(infoTable, "Year:", student.getStudyYear());
        addKeyValue(infoTable, "Email:", student.getEmail());

        document.add(infoTable);
        document.add(Chunk.NEWLINE);

        // -------------------------------------------------
        // COURSE RESULTS
        // -------------------------------------------------
        document.add(new Paragraph("Course Results", HEADER_FONT));
        document.add(Chunk.NEWLINE);

        PdfPTable resultTable = new PdfPTable(5);
        resultTable.setWidthPercentage(100);

        addHeaderCell(resultTable, "Course ID");
        addHeaderCell(resultTable, "Course Name");
        addHeaderCell(resultTable, "Final Mark");
        addHeaderCell(resultTable, "Grade");
        addHeaderCell(resultTable, "Passed");

        for (CourseResult r : student.getCourseResults()) {
            Course c = CourseRegistry.getCourse(r.getCourseId());

            resultTable.addCell(new Phrase(r.getCourseId(), TEXT_FONT));
            resultTable.addCell(new Phrase(
                    c != null ? c.getCourseName() : "Unknown",
                    TEXT_FONT
            ));
            resultTable.addCell(new Phrase(String.format("%.2f", r.getFinalMark()), TEXT_FONT));
            resultTable.addCell(new Phrase(r.getGrade(), TEXT_FONT));
            resultTable.addCell(new Phrase(r.isPassed() ? "Yes" : "No", TEXT_FONT));
        }

        document.add(resultTable);

        document.close();
    }

    // =====================================================
    // HELPERS
    // =====================================================
    private void addKeyValue(PdfPTable table, String key, String value) {
        table.addCell(new PdfPCell(new Phrase(key, HEADER_FONT)));
        table.addCell(new PdfPCell(new Phrase(value, TEXT_FONT)));
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
}
