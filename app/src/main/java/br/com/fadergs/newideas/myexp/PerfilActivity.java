package br.com.fadergs.newideas.myexp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class PerfilActivity extends AppCompatActivity {

    private static final String TAG = "UpdateUser:";
    private static final int RC_SIGN_IN = 1 ;
    FirebaseUser user;
    FirebaseAuth auth;
    EditText edtNome;
    TextView txtEmail;
    EditText edtSenha;
    Button btnSalvar;
    Button btnSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // instancia do user no Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Preenche os campos com os valores do FirebaseDB
        edtNome = findViewById(R.id.edtNome);
        edtNome.setText(user.getDisplayName().toString());
        txtEmail = findViewById(R.id.edtEmail);
        txtEmail.setHint(user.getEmail().toString());

        btnSenha = findViewById(R.id.btnSenha);
        btnSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviaEmailTrocaSenha(user.getEmail().toString());
                Toast.makeText(PerfilActivity.this, "Email enviado para a troca de senha!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(edtNome.getText().toString())
                        .build();

                // Atualiza o nome
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                }
                            }
                        });
                // Atualiza o email
                user.updateEmail(txtEmail.getHint().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");
                                }
                            }
                        });

            }
        });
    }


    private void enviaEmailTrocaSenha(String emailAddress){

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

}
