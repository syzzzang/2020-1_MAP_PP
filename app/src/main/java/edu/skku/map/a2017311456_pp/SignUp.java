package edu.skku.map.a2017311456_pp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity {
    private DatabaseReference databaseReference;
    ArrayList<FirebasePost> firebasePosts;
    EditText id, pw,pw2;
    String name, spw, spw2;
    Boolean dupcheck;
    String cname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name="";
        spw="";
        spw2="";
        cname="";
        firebasePosts=new ArrayList<>();
        Button dup=(Button)findViewById(R.id.dup);
        dupcheck=false;
        dup.setOnClickListener(new View.OnClickListener() {
            EditText username = (EditText)findViewById(R.id.signupUsername);
            @Override
            public void onClick(View v) {
                name=username.getText().toString();

                checkduplicate(name);
            }
        });
        Button login = (Button)findViewById(R.id.signupButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText)findViewById(R.id.signupUsername);
                pw=(EditText)findViewById(R.id.signupPassword);
                pw2=(EditText)findViewById(R.id.signupPassword2);
                name=username.getText().toString();
                spw=pw.getText().toString();
                spw2=pw2.getText().toString();
                if(spw.equals(spw2)){
                    if(dupcheck && cname.equals(name)){
                        postFirebaseDatabase(true);
                        Toast.makeText(getApplicationContext(),"축가입",Toast.LENGTH_SHORT).show();
                        Intent signupIntent = new Intent(SignUp.this, MainActivity.class);
                        signupIntent.putExtra("id", name);
                        startActivity(signupIntent);
                    }else{
                        Toast.makeText(getApplicationContext(),"중복체크좀",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"비번이 달라여",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void checkduplicate(final String name) {

        DatabaseReference pref= FirebaseDatabase.getInstance().getReference("user_list").child(name);

        ValueEventListener valueEventListener=new ValueEventListener() {
            //boolean a=true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() || name.equals("kakao")){
                    //a=false;
                    Toast.makeText(getApplicationContext(), "이미 있는 아이디임", Toast.LENGTH_LONG).show();
                }else{
                    cname=name;
                    dupcheck=true;
                    Toast.makeText(getApplicationContext(), "써도 되는 아이디임", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        pref.addListenerForSingleValueEvent(valueEventListener);
    }

    public void postFirebaseDatabase(boolean add){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        Map<String,Object> chileUpdates=new HashMap<>();
        Map<String,Object> postValues=null;

        if(add){
            FirebasePost post=new FirebasePost(name, spw);
            postValues=post.toMap();
        }
        chileUpdates.put("/user_list/"+name,postValues);
        databaseReference.updateChildren(chileUpdates);
    }
    public void clearET(){
        id.setText("");
        pw.setText("");
        pw2.setText("");
    }


}
