package com.example.projekt;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditorActivity extends AppCompatActivity {

    EditText author,title, numberOfPages;
    Button update,delete;
    FirebaseFirestore db;

    private Book book;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        book=(Book) getIntent().getSerializableExtra("book");

        author=findViewById(R.id.editAuthor);
        title=findViewById(R.id.editTitle);
        numberOfPages=findViewById(R.id.editPages);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        numberOfPages.setText(book.getNumberOfPages());


        update=findViewById(R.id.update);
        delete=findViewById(R.id.delete);
        db = FirebaseFirestore.getInstance();

        UpdateBook();
        DeleteBook();
    }

    private void DeleteBook() {
        delete.setOnClickListener(view -> {
            db.collection("books").document(book.getId()).delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditorActivity.this, "Book deleted", Toast.LENGTH_LONG).show();
                                finish();

                            }
                        }
                    });
        });
    }

    private void UpdateBook() {
        update.setOnClickListener(view -> {
            String txt_title=title.getText().toString();
            String txt_author=author.getText().toString();
            String txt_numberOfPages= numberOfPages.getText().toString();

            if(txt_title.isEmpty()||txt_author.isEmpty()){
                Toast.makeText(this,"Please enter text!",Toast.LENGTH_SHORT).show();

            }
            else{

                Book b = new Book(txt_title,txt_author,txt_numberOfPages);

                db.collection("books").document(book.getId())
                        .set(b)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditorActivity.this, "Book Updated", Toast.LENGTH_LONG).show();
                            }
                        });


            }

            finish();
        });
    }
}