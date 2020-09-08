package com.example.personaltennistracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personaltennistracker.Database.AppDatabase;
import com.example.personaltennistracker.Database.UserEntity;

public class LoginActivity extends AppCompatActivity {

    AppDatabase db;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO Auto-redirect if already logged in


        email = (EditText) findViewById(R.id.editTextEmailAddressLogin);
        password = (EditText) findViewById(R.id.editTextPasswordLogin);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String emailStr = email.getText().toString().trim();
                    final String passwordStr = password.getText().toString();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tennisTracker")
                                    .build();

                            UserEntity user = db.userDao().validateLogin(emailStr, passwordStr);
                            //check for correct format
                            if(user != null){
                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                homeIntent.putExtra("User",user);
                                homeIntent.putExtra("SignedIn", true);
                                startActivity(homeIntent);
                            }else{ //throw error
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });

        TextView register = (TextView) findViewById(R.id.textViewRegisterLink);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

}