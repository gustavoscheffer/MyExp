package br.com.fadergs.newideas.myexp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ExcluiContaActivity extends AppCompatActivity {

    Button btnExcluiConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclui_conta);

        // Implementa o botão excluir
        btnExcluiConta = findViewById(R.id.btnExcluiConta);
        btnExcluiConta.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {

                // Aqui vai o metodo que apaga todos os dados no Firebase DB
                excluiDadosFirebaseDB();

                // Exclui a Conta no Firebase Auth
                excluiContaFireBase();
            }
        });


    }

    // metodo para excluir todos os dados do usuario no Firebase DB
    private  void excluiDadosFirebaseDB(){
        Toast.makeText(this, "Ainda não foi implementado!", Toast.LENGTH_LONG).show();
    }

    //metodo para excluir a conta no Firebase Atuh
    private void excluiContaFireBase(){
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Deletion succeeded
                            Toast.makeText(ExcluiContaActivity.this, "Conta Excluída com sucesso!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Deletion failed
                            Toast.makeText(ExcluiContaActivity.this, "Não estamos excluir sua Conta, Entre em contato através\n" +
                                    "do Suporte suporte@myexp.com", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
