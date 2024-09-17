package com.example.commapp1;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText issueNameEditText, issueDescriptionEditText, issueLocationEditText;
    private Button submitButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        issueNameEditText = findViewById(R.id.issueName);
        issueDescriptionEditText = findViewById(R.id.issueDescription);
        issueLocationEditText = findViewById(R.id.issueLocation);
        submitButton = findViewById(R.id.submitButton);

        // Set onClick listener for the submit button
        submitButton.setOnClickListener(v -> submitReport());
    }

    private void submitReport() {
        String issueName = issueNameEditText.getText().toString();
        String issueDescription = issueDescriptionEditText.getText().toString();
        String issueLocation = issueLocationEditText.getText().toString();
        String currentTime = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        // Validate inputs
        if (issueName.isEmpty() || issueDescription.isEmpty() || issueLocation.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new report
        Map<String, Object> report = new HashMap<>();
        report.put("issue_name", issueName);
        report.put("issue_description", issueDescription);
        report.put("location", issueLocation);
        report.put("timestamp", currentTime);

        // Save the report to Firestore
        db.collection("reports")
                .add(report)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(MainActivity.this, "Report submitted successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, "Error submitting report", Toast.LENGTH_SHORT).show());
    }
}
