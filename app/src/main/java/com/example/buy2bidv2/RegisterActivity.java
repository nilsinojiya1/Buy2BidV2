package com.example.buy2bidv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText fullname,email, password;
    Button register;
    TextView txt_login;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname=findViewById(R.id.fullname);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        txt_login=findViewById(R.id.txt_login);
        register=findViewById(R.id.register);


        auth=FirebaseAuth.getInstance();
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_fullname = fullname.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();


                if(TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password))
                {
                    Toast.makeText(RegisterActivity.this, "All fileds are required!!", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
                else if(str_password.length() < 6)
                {
                    Toast.makeText(RegisterActivity.this, "Password must have 6 characters!!", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
                else
                {
                    register(str_fullname,str_email,str_password);
                }
            }
        });
    }
    private void register(final String fullname, final String email, String password)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            Log.i("uid",userid);

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String,Object> hashMap = new HashMap<String,Object>();
                            hashMap.put("fullname",fullname);
                            hashMap.put("id",userid);
                            hashMap.put("email",email);
                            hashMap.put("activeFlag","1");
                            hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/buy2bid-55ac6.appspot.com/o/homer.png?alt=media&token=61862f6b-7f29-47ab-8d72-8f8d95874540");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("task","success");
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else
                        {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "you can't register with this email and password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
