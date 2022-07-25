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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText author,title, numberOfPages;
    Button add;
    FirebaseFirestore db;

    FirebaseUser user;

    String id;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;


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



        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
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

    private void AddBook() {
        add.setOnClickListener(view -> {
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