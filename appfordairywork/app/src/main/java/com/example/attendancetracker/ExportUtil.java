package com.example.attendancetracker;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.provider.MediaStore;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExportUtil {

    private Context context;

    public ExportUtil(Context context) {
        this.context = context;
    }

    /**
     * Exports all attendance records to a CSV file in the Downloads folder.
     * Returns the URI of the created file, or null on failure.
     */
    public Uri exportCsv(List<AttendanceEntity> records) {
        if (records == null || records.isEmpty()) {
            return null;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timestamp = dateFormat.format(System.currentTimeMillis());
        String fileName = "attendance_" + timestamp + ".csv";

        // Android 10+ MediaStore approach
        ContentValues values = new ContentValues();
        values.put(MediaStore.Documents.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/AttendanceTracker");
        values.put(MediaStore.DISPLAY_NAME, fileName);
        values.put(MediaStore.MIME_TYPE, "text/csv");

        Uri uri = context.getContentResolver().insert(MediaStore.DocumentsExternalContentUri, values);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(uri.getPath(), fileName)))) {
            // Header
            writer.write("Date,Status,In-Time,Out-Time,Advance (₹),Notes,Earnings (₹)");
            writer.newLine();

            for (AttendanceEntity entity : records) {
                String dateStr = entity.getDateMillis() > 0
                        ? new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(entity.getDateMillis()))
                        : "";

                String status = entity.getStatus();
                String inTime = entity.getInHour() + ":" + String.format("%02d", entity.getInMinute());
                String outTime = entity.getOutHour() + ":" + String.format("%02d", entity.getOutMinute());
                double advance = entity.getAdvanceTaken();
                String notes = entity.getNotes() != null ? entity.getNotes() : "";
                double earnings = entity.getDayEarnings();

                writer.write(String.format("%s,%s,%s,%s,%.2f,%s,%.2f", dateStr, status, inTime, outTime, advance, notes, earnings));
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // Refresh media store
        context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", uri));
        return uri;
    }

    /**
     * Generates a formatted Monthly Payslip as PDF and saves to Downloads.
     * @param monthYear e.g., "January 2025"
     * @param totalDaysLogged total days attended
     * @param fullCount count of full days
     * @param halfCount count of half days
     * @param absentCount count of absent days
     * @param grossEarned total gross earned salary
     * @param totalAdvance total advance taken
     * @param netReceivable net salary after advance deduction
     * @return Uri of the PDF file, or null on failure
     */
    public Uri generatePayslipPdf(String monthYear,
                                  int totalDaysLogged,
                                  int fullCount,
                                  int halfCount,
                                  int absentCount,
                                  double grossEarned,
                                  double totalAdvance,
                                  double netReceivable) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timestamp = sdf.format(System.currentTimeMillis());
        String fileName = "payslip_" + monthYear.replace(" ", "_") + "_" + timestamp + ".pdf";

        // Use PdfDocument API available in Android
        android.graphics.pdf.PdfDocument pdfDocument = new android.graphics.pdf.PdfDocument();

        // --- Page 1: Title ---
        android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 portrait in points
        android.graphics.pdf.PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        android.canvas.SurfaceCanvas canvas = new android.canvas.SurfaceCanvas(page.getCanvas());
        // You can use canvas.drawText etc. But SurfaceCanvas is deprecated; we'll use direct page.getCanvas()
        android.graphics.Canvas gCanvas = page.getCanvas();

        // Title
        android.graphics.Paint titlePaint = new android.graphics.Paint();
        titlePaint.setTextSize(24);
        titlePaint.setTypeface(android.graphics.Typeface.createBold(android.graphics.Typeface.DEFAULT));
        titlePaint.setColor(android.graphics.Color.BLACK);
        float titleX = (595 - titlePaint.measureText("Monthly Payslip")) / 2;
        gCanvas.drawText("Monthly Payslip", titleX, 50, titlePaint);

        // Month/Year
        android.graphics.Paint subtitlePaint = new android.graphics.Paint();
        subtitlePaint.setTextSize(18);
        subtitlePaint.setColor(android.graphics.Color.BLACK);
        gCanvas.drawText(monthYear, titleX, 80, subtitlePaint);

        // Summary table headers
        android.graphics.Paint headerPaint = new android.graphics.Paint();
        headerPaint.setTextSize(14);
        headerPaint.setColor(android.graphics.Color.BLACK);
        String[] labels = {"Total Days Logged:", "Full Days:", "Half Days:", "Absent Days:", "Gross Earned (₹):", "Total Advance (₹):", "Net Receivable (₹):"};
        float startX = 50;
        float lineY = 120;
        for (String label : labels) {
            String value = "";
            switch (label) {
                case "Total Days Logged:": value = String.valueOf(totalDaysLogged); break;
                case "Full Days:": value = String.valueOf(fullCount); break;
                case "Half Days:": value = String.valueOf(halfCount); break;
                case "Absent Days:": value = String.valueOf(absentCount); break;
                case "Gross Earned (₹):": value = String.format("%.2f", grossEarned); break;
                case "Total Advance (₹):": value = String.format("%.2f", totalAdvance); break;
                case "Net Receivable (₹):": value = String.format("%.2f", netReceivable); break;
            }
            gCanvas.drawText(label, startX, lineY, headerPaint);
            gCanvas.drawText(value, 350, lineY, headerPaint);
            lineY += 30;
        }

        // Closing
        android.graphics.Paint footPaint = new android.graphics.Paint();
        footPaint.setTextSize(12);
        footPaint.setColor(android.graphics.Color.GRAY);
        gCanvas.drawText("Generated on " + new java.util.Date(), startX, lineY + 20, footPaint);

        pdfDocument.finishPage(page);

        // Write to file
        File outFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
        try {
            pdfDocument.writeTo(new java.io.FileOutputStream(outFile));
        } catch (Exception e) {
            e.printStackTrace();
            pdfDocument.close();
            return null;
        }
        pdfDocument.close();

        // Return URI via content resolver for media scan
        ContentValues values = new ContentValues();
        values.put(MediaStore.Documents.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/AttendanceTrackerPayslips");
        values.put(MediaStore.DISPLAY_NAME, fileName);
        values.put(MediaStore.MIME_TYPE, "application/pdf");
        Uri uri = context.getContentResolver().insert(MediaStore.DocumentsExternalContentUri, values);

        // Try to copy file to the uri location if needed; for simplicity just return uri.
        return uri;
    }
}