package br.com.stralom.compras.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.stralom.compras.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private ConstraintLayout mainLayout;
    private Button signinBtn;
    private Button registerBtn;
    private Button forgotPassBtn;
    private EditText emailText;
    private EditText passText;
    private FrameLayout progressBarPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.mAuth = FirebaseAuth.getInstance();
        user  = this.mAuth.getCurrentUser();

        if(user != null) {
            if(user.isEmailVerified()){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        this.signinBtn = findViewById(R.id.login_signin);
        this.forgotPassBtn = findViewById(R.id.login_forgotPassword);
        this.emailText = findViewById(R.id.login_email);
        this.passText = findViewById(R.id.login_pass);
        this.registerBtn = findViewById(R.id.login_register);
        this.mainLayout = findViewById(R.id.login_mainLayout);
        this.progressBarPanel = findViewById(R.id.progressBar_panel);

        this.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        this.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    progressBarPanel.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailText.getText().toString(), passText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBarPanel.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(user.isEmailVerified()){
                                            Intent intent = new Intent(getBaseContext(),MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            showEmailNotVerifiedSnackBar(user);
                                            Log.d(TAG, "Email não verificado");
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Credencial Invalídas",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    // ...
                                }
                            });
                }
            }
        });

        this.forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    emailText.setError("Informe o seu email");
                } else {
                    mAuth.sendPasswordResetEmail(emailText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getBaseContext(), "Instruções para alteração de senha enviadas para " + emailText.getText().toString(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getBaseContext(), emailText.getText().toString() + " não cadastrado.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });
    }


    public void showEmailNotVerifiedSnackBar(final FirebaseUser user){
        final Snackbar snackbar = Snackbar
                .make(this.mainLayout, "Email não validado, deseja receber um novo email de validação?", Snackbar.LENGTH_LONG);
        snackbar.setAction("Reenviar", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Email de verificação enviado para " + user.getEmail(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Falha ao enviar email de verificação" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
                snackbar.dismiss();
            }
        });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color


        snackbar.show();
    }



    private boolean validateForm() {
        boolean valid = true;

        String email = emailText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailText.setError("Campo obrigatório.");
            valid = false;
        }

        String password = passText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passText.setError("Campo obrigatório.");
            valid = false;
        }

        return valid;
    }

}
