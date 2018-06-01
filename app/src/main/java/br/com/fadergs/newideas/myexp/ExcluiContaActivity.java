package br.com.fadergs.newideas.myexp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ExcluiContaActivity extends AppCompatActivity {

    Button btnExcluiConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclui_conta);

        FirebaseUser firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();

        //Exclui conta e os dados
        //btnExcluiContaAction(firebaseAuth);

        //Desloga
        //new MainActivity().firebaseSigin();


    }

    // Implementa o botão excluir
    private void btnExcluiContaAction(final FirebaseUser user) {
        btnExcluiConta = findViewById(R.id.btnExcluiConta);
        btnExcluiConta.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {

                if (user.getUid() != null) {

                    // Exclui a conta no Firebase Auth
                    FirebaseAuth.getInstance().getCurrentUser().delete();

                    /* Remove todos os no Firebase DB */
                }
            }
        });
    }


}
