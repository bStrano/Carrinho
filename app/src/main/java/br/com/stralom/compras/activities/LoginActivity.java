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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import br.com.stralom.compras.R;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager mCallbackManager;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private ConstraintLayout mainLayout;
    private Button signinBtn;
    private Button signinGoogleBtn;
    private LoginButton signinFacebookBtn;
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
            if(user.isEmailVerified() || user.getProviderData().get(1).equals("facebook.com")){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        this.signinBtn = findViewById(R.id.login_signin);
        this.signinGoogleBtn = findViewById(R.id.login_signin_google);
        this.signinFacebookBtn = findViewById(R.id.login_signin_facebook);
        this.forgotPassBtn = findViewById(R.id.login_forgotPassword);
        this.emailText = findViewById(R.id.login_email);
        this.passText = findViewById(R.id.login_pass);
        this.registerBtn = findViewById(R.id.login_register);
        this.mainLayout = findViewById(R.id.login_mainLayout);
        this.progressBarPanel = findViewById(R.id.progressBar_panel);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();
        signinFacebookBtn.setReadPermissions("email", "public_profile");
        signinFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
            }
        });
        signinFacebookBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                showProgressBar(false);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                showProgressBar(false);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]

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
                                            login();
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

        this.signinGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
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

    private void login() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);

        finish();
        showProgressBar(false);
    }

    @Override
    protected void onStart() {
        super.onStart();



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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            login();
                        } else {
                            showProgressBar(false);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_frame), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
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

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            login();
                        } else {
                            // If sign in fails, display a message to the user.
                            showProgressBar(false);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    public void showProgressBar(boolean show){
        if(show){
            progressBarPanel.setVisibility(View.VISIBLE);
        } else {
            progressBarPanel.setVisibility(View.GONE);
        }

    }
}
