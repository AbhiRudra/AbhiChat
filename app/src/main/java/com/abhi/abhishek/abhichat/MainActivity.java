package com.abhi.abhishek.abhichat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    EditText e1;
    Button b1;
    ListView l1;

    DatabaseReference mdatabase;
    private FirebaseAuth mauth;
    private static int SIGN_IN_REQUEST_CODE=1;
    private FirebaseListAdapter<ChatMessage> adapter;
    private FirebaseAuth.AuthStateListener mauthlistner;
    private FirebaseUser mcurrentuser;
    private DatabaseReference mdatabaseuser;
    RelativeLayout Activity_main;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Snackbar.make(Activity_main,"You have Signed Out!",Snackbar.LENGTH_LONG).show();
finish();

                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SIGN_IN_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                Snackbar.make(Activity_main,"Successfully Signed In!, Welcome!",Snackbar.LENGTH_LONG).show();
                dispaychatmessages();
            }

            else
            {
                Snackbar.make(Activity_main,"Authentication Problem!\n Please try again later!",Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = findViewById(R.id.editmessage);
        b1 = findViewById(R.id.send);
        l1 = findViewById(R.id.listview);
        Activity_main=findViewById(R.id.activity_main);


        mdatabase = FirebaseDatabase.getInstance().getReference().child("Messages");

        mauth = FirebaseAuth.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String input=e1.getText().toString().trim();
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input,
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                e1.setText("");
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }

        else
        {
            Snackbar.make(Activity_main,"Welcome! "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_LONG).show();
            dispaychatmessages();

        }




    }

    private void dispaychatmessages() {

        ListView listofmessage=(ListView)findViewById(R.id.listview);
        adapter= new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView messagetext,messageuser,messagetime;
                messagetext=(TextView)v.findViewById(R.id.message);
                messageuser=(TextView)v.findViewById(R.id.messageuser);
                messagetime=(TextView)v.findViewById(R.id.messagetime);

                messagetext.setText(model.getMessagetext());
                messageuser.setText(model.getMessageuser());
                messagetime.setText(DateFormat.format("dd-mm-yyyy (HH:MM:SS)",model.getMessagetime()));



            }
        };

        listofmessage.setAdapter(adapter);
    }



}