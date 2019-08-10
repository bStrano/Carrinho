package br.com.stralom.compras.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import br.com.stralom.compras.R;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private static final String TAG = "RegisterActivity";
    private Button loginBtn;
    private Button registerBtn;
    private EditText nameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText confirmEmailText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nameText = findViewById(R.id.register_name);
        emailText = findViewById(R.id.register_email);
        confirmEmailText = findViewById(R.id.register_confirmEmail);
        passwordText = findViewById(R.id.register_password);
        loginBtn = findViewById(R.id.register_login);
        registerBtn = findViewById(R.id.register_submit);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToLoginIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(backToLoginIntent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(emailText.getText().toString(), passwordText.getText().toString());
            }

        });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nameText.getText().toString())
                                    .build();
                            Objects.requireNonNull(user).updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });

                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getBaseContext(), "Email de verificação enviado para " + user.getEmail(), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getBaseContext(), "Falha ao enviar email de verificação" , Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }





    private boolean validateForm() {
        boolean valid = true;


        String name = nameText.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nameText.setError("Campo obrigatório.");
            valid = false;
        } else {
            nameText.setError(null);
        }


        String email = emailText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailText.setError("Campo obrigatório.");
            valid = false;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailText.setError(null);

            } else {
                valid = false;
                emailText.setError("Email invalído.");
            }
        }

        String confirmEmail = confirmEmailText.getText().toString();
        if (TextUtils.isEmpty(confirmEmail)) {
            confirmEmailText.setError("Campo obrigatório.");
            valid = false;
        } else {
            if (email.equals(confirmEmail)) {
                confirmEmailText.setError(null);
            } else {
                valid = false;
                confirmEmailText.setError("Os e-mais informados são diferentes");
            }
        }

        String password = passwordText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordText.setError("Campo obrigatório.");
            valid = false;
        } else {
            if (password.length() < 6) {
                passwordText.setError("A senha deve possuir pelo menos 6 caracteres.");
                valid = false;
            } else {
                passwordText.setError(null);
            }
        }

        return valid;
    }

}


