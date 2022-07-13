package com.example.projekt.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.Book;
import com.example.projekt.AddActivity;
import com.example.projekt.EditorActivity;
import com.example.projekt.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int INTENT_EDIT = 200;
    private static final int INTENT_ADD = 100;

    RecyclerView recyclerView;
    ArrayList<Book> bookArrayList;
    MainAdapter mainAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    MainAdapter.ItemClickListener itemClickListener;
    FloatingActionButton fab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        clickListener();

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        if(bookArrayList!=null)progressDialog.show();

        recyclerView=findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        bookArrayList= new ArrayList<>();
        mainAdapter =new MainAdapter(MainActivity.this, bookArrayList, itemClickListener);

        recyclerView.setAdapter(mainAdapter);

        EvenChangeListener();



        fab=findViewById(R.id.fabAdd);
        fab.setOnClickListener(view -> startActivity(new Intent(this, AddActivity.class)));




    }
    

    private void clickListener() {
        itemClickListener=((view, position) -> {
/*            Book book=bookArrayList.get(position);
            Intent intent =new Intent(this,EditorActivity.class);
            intent.putExtra("book", book);
            Log.e("Book id",book.getId());
            this.startActivity(intent);*/
        });
    }



    private void EvenChangeListener() {
        db.collection("books")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null)
                        {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }
                        for(DocumentChange documentChange:value.getDocumentChanges()){
                            mainAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                        if(!value.isEmpty()) {
                            if (!value.isEmpty()) {

                                List<DocumentSnapshot> list = value.getDocuments();

                                bookArrayList.clear();

                                for (DocumentSnapshot d : list) {
                                    Book b = d.toObject(Book.class);
                                    b.setId(d.getId());
                                    bookArrayList.add(b);

                                }
                            }
                        }
                    }
                });


    }


}