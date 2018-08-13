package com.example.roshan.restrominku3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roshan.restrominku3.Common.Common;
import com.example.roshan.restrominku3.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText editpassword,editphone;
    Button btnSignin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editpassword=(MaterialEditText)findViewById(R.id.edtPassword);
        editphone=(MaterialEditText)findViewById(R.id.edtnumber);

        btnSignin=(Button)findViewById(R.id.btnSignIn);


        //init firebase
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("user");


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ProgressDialog mDialog=new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Waiting....");
                mDialog.show();


                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //check if user not exit in database
                        if (dataSnapshot.child(editphone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            //get user information


                            User user = dataSnapshot.child(editphone.getText().toString()).getValue(User.class);
                            user.setPhone(editphone.getText().toString());//Set phone no
                            if (user.getPassword().equals(editpassword.getText().toString())) {
                                Intent homeIntent = new Intent(SignIn.this,Home.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();

                            } else {
                                Toast.makeText(SignIn.this, "Sign In Failed!!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this,"User Not Exit", Toast.LENGTH_SHORT).show();
                        }
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
