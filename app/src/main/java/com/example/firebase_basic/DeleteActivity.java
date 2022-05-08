package com.example.firebase_basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DeleteActivity extends AppCompatActivity {
    Button btnDelete;
    EditText edtFirstNameDelete;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        db = FirebaseFirestore.getInstance();
        btnDelete = findViewById(R.id.btnDeleteData);
        edtFirstNameDelete = findViewById(R.id.edtFirstNameDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = edtFirstNameDelete.getText().toString();
                edtFirstNameDelete.setText("");
                delete(firstname);
            }
        });
    }

    private void delete(String firstname) {
            db.collection("users").whereEqualTo("firstName",firstname).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && !task.getResult().isEmpty()){
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentID = documentSnapshot.getId();
                                db.collection("users").document(documentID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(DeleteActivity.this,"Successful!!",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DeleteActivity.this,"Failureee!!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(DeleteActivity.this,"failedd!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }
}