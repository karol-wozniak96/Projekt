package com.example.projekt;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button add;

    RecyclerView recyclerView;
    ArrayList<Book> bookArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    EditText author,title, numberOfPages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add=findViewById(R.id.add);
        author=findViewById(R.id.editTextAuthor);
        title=findViewById(R.id.editTextTitle);
        numberOfPages=findViewById(R.id.editTextPages);

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        recyclerView=findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        bookArrayList= new ArrayList<>();
        myAdapter=new MyAdapter(MainActivity.this, bookArrayList);

        recyclerView.setAdapter(myAdapter);

        EvenChangeListener();

        AddBook();


    }

    private void AddBook() {
        add.setOnClickListener(view -> {
            String txt_title=title.getText().toString();
            String txt_author=author.getText().toString();
            String txt_numberOfPages= numberOfPages.getText().toString();
            Book book=new Book(txt_title,txt_author,txt_numberOfPages);
            if(txt_title.isEmpty()||txt_author.isEmpty()){
                Toast.makeText(MainActivity.this,"Please enter text!",Toast.LENGTH_SHORT).show();

            }
            else{

                db.collection("books")
                        .add(book)
                        .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))

                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
            }

        });
    }

    private void EvenChangeListener() {
        System.out.println("1");
        db.collection("books")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null)
                        {
                            System.out.println("2");
                            if(progressDialog.isShowing())
                                System.out.println("3");
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage());
                            System.out.println("4");
                            return;
                        }
                        for(DocumentChange documentChange:value.getDocumentChanges()){
                            System.out.println("4");
                            if(documentChange.getType()== DocumentChange.Type.ADDED){
                                bookArrayList.add(documentChange.getDocument().toObject(Book.class));
                                System.out.println("aray yes");
                            }
                            myAdapter.notifyDataSetChanged();
                            System.out.println("5");
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            System.out.println("aray not");
                        }
                    }
                });
    }
}