package com.example.roshan.restrominku3;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roshan.restrominku3.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.example.roshan.restrominku3.R.id.edtPassword;

public class SignUp extends AppCompatActivity {
    MaterialEditText edtName,edtPassword,edtNumber;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName=(MaterialEditText)findViewById(R.id.edtName);
        edtPassword=(MaterialEditText)findViewById(R.id.edtPassword);
        edtNumber=(MaterialEditText)findViewById(R.id.edtnumber);

        btnSignUp=(Button)findViewById(R.id.btnSignUp);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("user");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog=new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please Waiting....");
                mDialog.show();

                table_user .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if already user phone
                        if (dataSnapshot.child(edtNumber.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Phone number Already Used", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mDialog.dismiss();
                            User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                            table_user.child(edtNumber.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this,"Sign Up Success",Toast.LENGTH_SHORT).show();
                            finish();
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
