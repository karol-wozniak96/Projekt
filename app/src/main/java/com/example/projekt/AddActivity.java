package com.example.projekt;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddActivity extends AppCompatActivity {

    EditText author,title, numberOfPages;
    Button add;
    FirebaseFirestore db;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        author=findViewById(R.id.editTextAuthor);
        title=findViewById(R.id.editTextTitle);
        numberOfPages=findViewById(R.id.editTextPages);
        user = FirebaseAuth.getInstance().getCurrentUser();

        add=findViewById(R.id.add);
        db = FirebaseFirestore.getInstance();

        AddBook();
    }

    private void AddBook() {
        add.setOnClickListener(view -> {
            String txt_title=title.getText().toString();
            String txt_author=author.getText().toString();
            String txt_numberOfPages= numberOfPages.getText().toString();
            String by_user=user.getUid();
            Book book=new Book(txt_title,txt_author,txt_numberOfPages,by_user);
            if(txt_title.isEmpty()||txt_author.isEmpty()){
                Toast.makeText(this,"Please enter text!",Toast.LENGTH_SHORT).show();

            }
            else{

                db.collection("books")
                        .add(book)
                        .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))

                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
            }

            finish();
        });
    }
}