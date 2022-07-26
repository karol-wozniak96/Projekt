package com.example.projekt.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projekt.Book;
import com.example.projekt.AddActivity;
import com.example.projekt.EditorActivity;
import com.example.projekt.Login;
import com.example.projekt.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    RecyclerView recyclerView;
    ArrayList<Book> bookArrayList;
    MainAdapter mainAdapter;
    FirebaseFirestore db;
    FirebaseUser user;
    ProgressDialog progressDialog;

    MainAdapter.ItemClickListener itemClickListener;
    FloatingActionButton fab;

    EditText editTextSearch;

    String id,byUser;
    private ArrayList<Book> mExampleList;



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
        editTextSearch=findViewById(R.id.search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();


        bookArrayList= new ArrayList<>();
        mainAdapter =new MainAdapter(MainActivity.this, bookArrayList, itemClickListener);

        recyclerView.setAdapter(mainAdapter);

        EvenChangeListener();


        fab=findViewById(R.id.fabAdd);
        fab.setOnClickListener(view -> startActivity(new Intent(this, AddActivity.class)));

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


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


                                try
                                {
                                    id=user.getUid();
                                    byUser=b.getByUser();

                                    if(id.equals(byUser)){
                                        bookArrayList.add(b);
                                    }
                                }
                                catch(NullPointerException e)
                                {
                                    byUser=b.getByUser();

                                    if(id.equals(byUser)){
                                        bookArrayList.add(b);
                                    }
                                }
                            }
                        }
                    }
                });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println(editable);
                filter(editable.toString());
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );

            }
        });

    }

    private void filter(String text) {
        ArrayList<Book> filteredList = new ArrayList<>();
        for (Book b : bookArrayList) {
            if(b.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(b);
            }
        }
        mainAdapter.filerList(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemLogout:
                FirebaseAuth.getInstance().signOut();
                Context context=this;

                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()){
                                    Intent intent=new Intent(context, Login.class);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Session not close", Toast.LENGTH_LONG).show();
                                }
                            }
                        });



                Intent intent=new Intent(context, Login.class);
                context.startActivity(intent);
                finish();



                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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