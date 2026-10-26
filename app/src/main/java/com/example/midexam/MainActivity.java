package com.example.midexam;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etId;
    CheckBox cbEnrolled;
    Button btnAdd, btnEdit, btnDelete, btnView;
    TextView tvResult;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etId);
        cbEnrolled = findViewById(R.id.cbEnrolled);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        tvResult = findViewById(R.id.tvResult);

        dbHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String id = etId.getText().toString().trim();
            if (name.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Please fill name and ID", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dbHelper.addStudent(name, id, cbEnrolled.isChecked())) {
                Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show();
                clearInput();
                showData();
            } else {
                Toast.makeText(this, "Failed (ID may already exist)", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String id = etId.getText().toString().trim();
            if (dbHelper.updateStudent(name, id, cbEnrolled.isChecked())) {
                Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show();
                clearInput();
                showData();
            } else {
                Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            String id = etId.getText().toString().trim();
            if (dbHelper.deleteStudent(id)) {
                Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show();
                clearInput();
                showData();
            } else {
                Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
            }
        });

        btnView.setOnClickListener(v -> showData());
    }

    private void showData() {
        Cursor c = dbHelper.getAllStudents();
        if (c.getCount() == 0) {
            tvResult.setText("No data");
            c.close();
            return;
        }
        StringBuilder sb = new StringBuilder();
        while (c.moveToNext()) {
            sb.append("ID: ").append(c.getString(0)).append("\n");
            sb.append("Name: ").append(c.getString(1)).append("\n");
            sb.append("Enrolled: ").append(c.getInt(2) == 1 ? "Yes" : "No").append("\n\n");
        }
        c.close();
        tvResult.setText(sb.toString());
    }

    private void clearInput() {
        etName.setText("");
        etId.setText("");
        cbEnrolled.setChecked(false);
    }
}