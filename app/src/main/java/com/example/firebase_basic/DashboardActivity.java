package com.example.firebase_basic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    RecyclerView rcvItem;
    ArrayList<User> userArrayList;
    UserAdapter adapter;
    FirebaseFirestore db ;
    ProgressDialog progressDialog;
    Button btnAdd,btnDelete,btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

      btnAdd = findViewById(R.id.btnAdd);
      btnDelete = findViewById(R.id.btnDelete);
      btnUpdate = findViewById(R.id.btnUpdate);

      rcvItem = findViewById(R.id.rcvItem);
      rcvItem.setHasFixedSize(true);
      rcvItem.setLayoutManager(new LinearLayoutManager(this));

      db = FirebaseFirestore.getInstance();

      userArrayList = new ArrayList<User>();
      adapter = new UserAdapter(DashboardActivity.this,userArrayList);
        rcvItem.setAdapter(adapter);
      EventChangeListener();
      btnAdd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(DashboardActivity.this,MainActivity.class);
              startActivity(intent);
          }
      });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,DeleteActivity.class);
                startActivity(intent);
            }
        });

    }

    private void EventChangeListener() {
            db.collection("users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore Error",error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                userArrayList.add(dc.getDocument().toObject(User.class));
                            }
                            adapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                }
            });
    }
}