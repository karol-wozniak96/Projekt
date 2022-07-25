package com.example.projekt;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditorActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    EditText author,title, numberOfPages;
    Button update,delete;
    FirebaseFirestore db;
    FirebaseUser user;

    private Book book;

    String id;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;


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
        user = FirebaseAuth.getInstance().getCurrentUser();

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


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
            String by_user;

            try
            {
                by_user=user.getUid();
            }
            catch(NullPointerException e)
            {

                by_user=id;

            }


            if(txt_title.isEmpty()||txt_author.isEmpty()){
                Toast.makeText(this,"Please enter text!",Toast.LENGTH_SHORT).show();

            }
            else{

                Book b = new Book(txt_title,txt_author,txt_numberOfPages,by_user);

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
    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();

            id=account.getId();



        }else{

        }
    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}