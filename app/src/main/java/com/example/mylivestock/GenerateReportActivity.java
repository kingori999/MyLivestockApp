package com.example.mylivestock;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GenerateReportActivity extends AppCompatActivity {

    private Spinner spinnerReportType;
    private Button buttonGenerateReport, buttonExportPdf;
    private TextView textViewReportContent;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ReportViewModel reportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        spinnerReportType = findViewById(R.id.spinner_report_type);
        buttonGenerateReport = findViewById(R.id.button_generate_report);
        buttonExportPdf = findViewById(R.id.button_export_pdf);
        textViewReportContent = findViewById(R.id.text_view_report_content);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        setupReportTypeSpinner();

        buttonGenerateReport.setOnClickListener(v -> generateReport());

        buttonExportPdf.setOnClickListener(v -> exportReportAsPdf());
    }

    private void setupReportTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.report_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportType.setAdapter(adapter);
    }

    private void generateReport() {
        String reportType = spinnerReportType.getSelectedItem().toString();
        String userId = mAuth.getCurrentUser().getUid();

        List<String> reportData = fetchReportData(reportType, userId);
        if (reportData.isEmpty()) {
            Toast.makeText(this, "No data available for the selected report type", Toast.LENGTH_SHORT).show();
            return;
        }

        Report report = new Report(reportType, "Full Data", userId, reportData);
        reportViewModel.insert(report);

        displayReportContent(reportData);
    }

    private List<String> fetchReportData(String reportType, String userId) {
        List<String> data = new ArrayList<>();

        switch (reportType) {
            case "Livestock Inventory Report":
                db.collection("livestock")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                String name = document.getString("name");
                                String type = document.getString("type");
                                String breed = document.getString("breed");
                                String gender = document.getString("gender");
                                String birthOrPurchaseStatus = document.getString("birthOrPurchaseStatus");
                                String birthOrPurchaseDate = document.getString("birthOrPurchaseDate");
                                String healthStatus = document.getString("healthStatus");
                                data.add(name + " | " + type + " | " + breed + " | " + gender + " | " + birthOrPurchaseStatus + " | " + birthOrPurchaseDate + " | " + healthStatus);
                            }
                            displayReportContent(data);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to fetch livestock data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            case "Milk Production Report":
                db.collection("milkRecords")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                String livestockName = document.getString("livestockName");
                                String productionDate = document.getString("productionDate");
                                double quantity = document.getDouble("quantity");
                                data.add(livestockName + " | " + productionDate + " | " + quantity + " liters");
                            }
                            displayReportContent(data);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to fetch milk production data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            case "Breeding Report":
                db.collection("breedingRecords")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                String femaleLivestockName = document.getString("femaleLivestockName");
                                String maleLivestockName = document.getString("maleLivestockName");
                                String breedingDate = document.getString("breedingDate");
                                String expectedDueDate = document.getString("expectedDueDate");
                                data.add(femaleLivestockName + " | " + maleLivestockName + " | " + breedingDate + " | " + expectedDueDate);
                            }
                            displayReportContent(data);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to fetch breeding data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            case "Health Records Report":
                db.collection("health")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                String livestockName = document.getString("livestockName");
                                String checkupDate = document.getString("checkupDate");
                                String healthStatus = document.getString("healthStatus");
                                String treatment = document.getString("treatmentAdministered");
                                data.add(livestockName + " | " + checkupDate + " | " + healthStatus + " | " + treatment);
                            }
                            displayReportContent(data);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to fetch health records: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            case "Nutrition Records Report":
                db.collection("nutrition")
                        .whereEqualTo("userId", userId)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                String livestockName = document.getString("livestockName");
                                String feedType = document.getString("feedType");
                                double quantity = document.getDouble("quantity");
                                String date = document.getString("date");
                                data.add(livestockName + " | " + feedType + " | " + quantity + " | " + date);
                            }
                            displayReportContent(data);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to fetch nutrition records: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                break;

            default:
                Toast.makeText(this, "Invalid report type selected", Toast.LENGTH_SHORT).show();
                break;
        }

        return data;
    }

    private void displayReportContent(List<String> reportData) {
        StringBuilder content = new StringBuilder();
        for (String line : reportData) {
            content.append(line).append("\n");
        }
        textViewReportContent.setText(content.toString());
        textViewReportContent.setVisibility(View.VISIBLE);
    }

    private void exportReportAsPdf() {
        String reportType = spinnerReportType.getSelectedItem().toString();
        String reportContent = textViewReportContent.getText().toString();
        if (TextUtils.isEmpty(reportContent)) {
            Toast.makeText(this, "No report content to export", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Set up the PDF document with iText
            String path = getExternalFilesDir(null).getAbsolutePath() + "/report.pdf";
            PdfWriter writer = new PdfWriter(path);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            // Add a title to the PDF
            document.add(new Paragraph("Livestock Management Report")
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold());

            // Create a table with appropriate column widths
            Table table;
            switch (reportType) {
                case "Livestock Inventory Report":
                    table = new Table(new float[]{3, 3, 3, 3, 3, 3, 3});
                    table.addHeaderCell(new Cell().add(new Paragraph("Name")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Type")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Breed")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Gender")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Birth/Purchase Status")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Birth/Purchase Date")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Health Status")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    break;

                case "Milk Production Report":
                    table = new Table(new float[]{4, 4, 4});
                    table.addHeaderCell(new Cell().add(new Paragraph("Livestock Name")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Production Date")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Quantity (liters)")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    break;

                case "Breeding Report":
                    table = new Table(new float[]{3, 3, 3, 3});
                    table.addHeaderCell(new Cell().add(new Paragraph("Female Livestock Name")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Male Livestock Name")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Breeding Date")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Expected Due Date")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    break;

                case "Health Records Report":
                    table = new Table(new float[]{3, 3, 3, 3});
                    table.addHeaderCell(new Cell().add(new Paragraph("Livestock Name")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Checkup Date")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Health Status")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Treatment Administered")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    break;

                case "Nutrition Records Report":
                    table = new Table(new float[]{4, 3, 2, 3});
                    table.addHeaderCell(new Cell().add(new Paragraph("Animal Type")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Feed Type")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Quantity")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    table.addHeaderCell(new Cell().add(new Paragraph("Date and Time")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    break;

                default:
                    table = new Table(1);
                    table.addHeaderCell(new Cell().add(new Paragraph("Data")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                    break;
            }

            table.setWidth(UnitValue.createPercentValue(100));

            // Add table rows
            for (String line : reportContent.split("\n")) {
                String[] parts = line.split("\\|");
                for (String part : parts) {
                    table.addCell(new Cell().add(new Paragraph(part.trim())).setTextAlignment(TextAlignment.CENTER));
                }
            }

            // Add table to document
            document.add(table);

            // Close the document
            document.close();

            // Show dialog to prompt the user to open the PDF
            showOpenPdfDialog(path);

        } catch (Exception e) {
            Toast.makeText(this, "Error exporting PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showOpenPdfDialog(String filePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PDF Generated")
                .setMessage("The report has been saved as a PDF. Would you like to open it?")
                .setPositiveButton("Open", (dialog, which) -> openPdf(filePath))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openPdf(String filePath) {
        File file = new File(filePath);
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent chooser = Intent.createChooser(intent, "Open PDF");
        try {
            startActivity(chooser);
        } catch (Exception e) {
            Toast.makeText(this, "No PDF viewer available", Toast.LENGTH_SHORT).show();
        }
    }}