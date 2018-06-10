package br.com.fadergs.newideas.myexp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class CadastraGastoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_gasto);




        // Coloca a data atual no campo data
        setDefaultDate();


        Button btnCadastraGasto = findViewById(R.id.btnCadastraGasto);
        btnCadastraGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Pega os valores dos campos
                EditText edtGastoNome = findViewById(R.id.edtNomeGasto);
                EditText edtGastoValor = findViewById(R.id.edtValorGasto);
                Spinner lstGastoCategoria = findViewById(R.id.listaCategorias);
                TextView txtGastoDate = findViewById(R.id.edtDate);
                String nome = edtGastoNome.getText().toString();
                double valor = Double.parseDouble(edtGastoValor.getText().toString());
                String categoria = lstGastoCategoria.getSelectedItem().toString();
                String data = txtGastoDate.getText().toString();

                // Novo Gasto
                Gasto gasto = new Gasto(nome, valor, data, categoria);

                // Cadastra os gastos no banco
                cadastraGastoNoBanco(gasto);





            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.listaCategorias);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoria_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);



    }


    // implmenta a view que mostra o calendario para escolher a data
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Implementa o metodo de colocar a data atual no campo data
    public void setDefaultDate() {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //pega a referencia do campo para colocar a data
        TextView txtDate = findViewById(R.id.edtDate);

        // Adicionar 1, pois o indice do mes começa do 0
        int mesAtual = month + 1;

        //Coloca a data selecionada no campo data
        txtDate.setText(day + "/" + mesAtual + "/" + year);

    }

    // Implementa o metodo que cadastra um gasto no banco
    public void cadastraGastoNoBanco(Gasto gasto) {
        // instancia do user no Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("gastos");
        String newKey = myRef.child(firebaseUser.getUid()).push().getKey();

        //Verifica se o dado foi inserido com sucesso!
        myRef.child(firebaseUser.getUid()).child(newKey).setValue(gasto.toMap())
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        startActivity(new Intent(CadastraGastoActivity.this, MainActivity.class));
                        Toast.makeText(CadastraGastoActivity.this, "Falha na inclusão do Gasto!!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(CadastraGastoActivity.this, MainActivity.class));
                Toast.makeText(CadastraGastoActivity.this, "Gasto inserido com sucesso!", Toast.LENGTH_LONG).show();
            }
        });

    }


}
