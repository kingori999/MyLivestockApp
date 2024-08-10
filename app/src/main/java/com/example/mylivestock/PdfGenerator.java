package com.example.mylivestock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfGenerator {

    public static void generatePdf(Context context, String title, List<Livestock> livestockList) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int x = 10, y = 25;
        canvas.drawText(title, x, y, paint);

        y += 30;
        for (Livestock livestock : livestockList) {
            canvas.drawText(livestock.getName() + " - " + livestock.getType() + " - " + livestock.getHealthStatus(), x, y, paint);
            y += 20;
        }

        pdfDocument.finishPage(page);

        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "LivestockReport.pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF generated", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }
}
