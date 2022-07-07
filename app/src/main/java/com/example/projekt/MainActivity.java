package com.example.projekt;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button add;
    private EditText title, author, numberOfPages;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add=findViewById(R.id.add);
        title=findViewById(R.id.editTextTitle);
        author=findViewById(R.id.editTextTitle);
        numberOfPages=findViewById(R.id.editTextTitle);
        listView=findViewById(R.id.listView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> book = new HashMap<>();
        final ArrayList<String> list =new ArrayList<>();
        final ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.list_item,list);
        listView.setAdapter(adapter);

        db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                list.add(document.getData().toString());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_title=title.getText().toString();
                String txt_author=title.getText().toString();
                String txt_numberOfPages=title.getText().toString();
                if(txt_title.isEmpty()||txt_author.isEmpty()||txt_numberOfPages.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter text!",Toast.LENGTH_SHORT).show();

                }
                else{
                    book.put("Title", txt_title);
                    book.put("Author", txt_author);
                    book.put("Number of pages", txt_numberOfPages);
                    db.collection("books")
                            .add(book)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
                adapter.notifyDataSetChanged();
                adapter.clear();
                db.collection("books")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        list.add(document.getString("Title"));

                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });


            }

        });





    }
}