package com.example.personaltennistracker;

import android.content.Intent;
import android.os.Bundle;

import com.example.personaltennistracker.Database.AppDatabase;
import com.example.personaltennistracker.Database.UserDao;
import com.example.personaltennistracker.Database.UserEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    AppDatabase db;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText rePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tennisTracker")
                .allowMainThreadQueries()
                .build();
        firstName = (EditText) findViewById(R.id.editTextFirstNameRegister);
        lastName = (EditText) findViewById(R.id.editTextLastNameRegister);
        email = (EditText) findViewById(R.id.editTextEmailAddressRegister);
        password = (EditText) findViewById(R.id.editTextPasswordSignUp);
        rePassword = (EditText) findViewById(R.id.editTextRetypePasswordSignUp);

        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() { //on registering... query to db
            @Override
            public void onClick(View view) {
                String firstNameStr = firstName.getText().toString().trim();
                String lastNameStr = lastName.getText().toString().trim();
                String emailStr = email.getText().toString().trim();
                String passwordStr = password.getText().toString();
                String rePasswordStr = rePassword.getText().toString();

                UserDao.UserErrors res = registerUser(firstNameStr, lastNameStr, emailStr, passwordStr, rePasswordStr, db);

                switch(res){
                    case EMAIL_EXISTS:
                        Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        break;
                    case BLANK_FIELDS:
                        Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        break;
                    case PASSWORDS_NO_MATCH:
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        break;
                    case NO_ERROR:
                        UserEntity newUser = new UserEntity(firstNameStr, lastNameStr, emailStr, passwordStr);
                        db.userDao().insertAll(newUser);
                        Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                        homeIntent.putExtra("User", newUser);
                        homeIntent.putExtra("SignedIn", true);
                        startActivity(homeIntent);   //redirect

                        break;
                }
            }
        });

        TextView login = (TextView) findViewById(R.id.textViewLoginLink);
        login.setOnClickListener(new View.OnClickListener() { //send back to login page
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    private static UserDao.UserErrors registerUser(String firstName, String lastName, String email, String password, String rePassword, AppDatabase db){
        try{
            if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()){
                return UserDao.UserErrors.BLANK_FIELDS;
            } else if(!password.equals(rePassword)){
                return UserDao.UserErrors.PASSWORDS_NO_MATCH;
            } else{
                //check if user already exists
                if(db.userDao().findByEmail(email) != null){
                    return UserDao.UserErrors.EMAIL_EXISTS;
                }
            }
        } catch (Exception e){
            return UserDao.UserErrors.UNKNOWN_ERROR;
        }

        return UserDao.UserErrors.NO_ERROR;
    }
}