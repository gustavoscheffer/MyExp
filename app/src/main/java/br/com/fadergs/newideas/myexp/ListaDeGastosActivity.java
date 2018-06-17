package br.com.fadergs.newideas.myexp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaDeGastosActivity extends AppCompatActivity {

    ListView lvContatos;
    List<String> gastos = new ArrayList<String>();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myNewRef = FirebaseDatabase.getInstance().getReference("gastos").child(firebaseUser.getUid());







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_gastos);

        myNewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(ListaDeGastosActivity.this, "Deu certo", Toast.LENGTH_SHORT).show();

                for (DataSnapshot dataGasto: dataSnapshot.getChildren()){
                    dataGasto.getValue().toString();
                    gastos.add(dataGasto.child("nome").getValue(String.class));
//                    Toast.makeText(ListaDeGastosActivity.this, dataGasto.child("nome").getValue(String.class), Toast.LENGTH_SHORT).show();
                }

                //Recuperando referência da ListView pelo seu id
                lvContatos = findViewById(R.id.lstGastos);

//                Precisamos de um ArrayAdapter... Vamos criar e popular com o array de nomes!
//                ArrayAdapter é quem faz o "link" entre o array do Java com o ListView da interface em xml
//                 android.R.layout.simple_list_item_1 é um layout pronto do Android
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListaDeGastosActivity.this, android.R.layout.simple_list_item_1, gastos);
//
//                Dizemos para o listView quem é o seu adapter
                lvContatos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
