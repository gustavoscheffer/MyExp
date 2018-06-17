package br.com.fadergs.newideas.myexp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CadastraGastoActivity extends AppCompatActivity {


    private static final String TAG = "TesteDB" ;

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

                Gasto gasto = new Gasto(nome, valor, convertDataToTimeStamp(data),categoria);

                // Cadastra os gastos no banco
                cadastraGastoNoBanco(gasto);

                // Read from the database
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference myNewRef = FirebaseDatabase.getInstance().getReference("gastos").child(firebaseUser.getUid());
                myNewRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Gasto gasto = dataSnapshot.getValue(Gasto.class);

                        ArrayList<Gasto> listaGasto = new ArrayList<Gasto>();
                        listaGasto.add(gasto);

                        Toast.makeText(CadastraGastoActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Gasto gasto = dataSnapshot.getValue(Gasto.class);

                        Toast.makeText(CadastraGastoActivity.this, gasto.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





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

        /* Metodo que converte a data de string para timestamp
       Sera usado para inserir a data no banco.*/
    private long convertDataToTimeStamp(String strData) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(strData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*Metodo que converte data (Timestamp) para data String(dd/MM/yyyy)
      Este sera usado para converter o dado quando vem do banco.*/

    private String convertTimeStampToDataBr(long dataTimeStamp){
        DateFormat formatter =  new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(dataTimeStamp);
    }


}
