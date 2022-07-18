package com.example.projekt.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.Book;
import com.example.projekt.AddActivity;
import com.example.projekt.EditorActivity;
import com.example.projekt.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseUser user;
    ProgressDialog progressDialog;

    MainAdapter.ItemClickListener itemClickListener;
    FloatingActionButton fab;

    String id,byUser;




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
        user = FirebaseAuth.getInstance().getCurrentUser();

        bookArrayList= new ArrayList<>();
        mainAdapter =new MainAdapter(MainActivity.this, bookArrayList, itemClickListener);

        recyclerView.setAdapter(mainAdapter);

        EvenChangeListener();



        fab=findViewById(R.id.fabAdd);
        fab.setOnClickListener(view -> startActivity(new Intent(this, AddActivity.class)));




    }
    

    private void clickListener() {
        itemClickListener=((view, position) -> {
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


                            if (!value.isEmpty()) {

                                List<DocumentSnapshot> list = value.getDocuments();

                                bookArrayList.clear();

                                for (DocumentSnapshot d : list) {
                                    Book b = d.toObject(Book.class);
                                    b.setId(d.getId());

                                    id=user.getUid();
                                    byUser=b.getByUser();

                                    if(id.equals(byUser)){
                                        bookArrayList.add(b);
                                    }
                                    else
                                    {
                                        System.out.println("nie");
                                    }
                                }
                            }

                    }
                });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }


}