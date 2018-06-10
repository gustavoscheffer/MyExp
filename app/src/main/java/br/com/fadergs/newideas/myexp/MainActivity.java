package br.com.fadergs.newideas.myexp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "DBGoogle";
    private DrawerLayout mDrawerLayout;
    private Button btnCadastraGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instancia do user no Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //menuListener
        mDrawerLayout = findViewById(R.id.drawer_layout);
        MenuListener(mDrawerLayout);

        // verifica se o user esta logado
        if (firebaseUser != null) {

            btnCadastraGasto  = findViewById(R.id.btnCadastraGasto);
            btnCadastraGasto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, CadastraGastoActivity.class));
                }
            });


//            //Teste de acesso a base de dados
//            // Write a message to the database
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("gastos");
////            myRef.child(firebaseUser.getUid()).setValue("Hello World Bitch!");
//            myRef.child(firebaseUser.getUid()).child("gasto1").setValue(2.50);
//            myRef.child(firebaseUser.getUid()).child("gasto2").setValue(3.50);
//            myRef.child(firebaseUser.getUid()).child("gasto3").setValue(4.40);
//            myRef.child(firebaseUser.getUid()).child("gasto4").setValue(5.30);



//            // Read from the database
//            myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    String value = dataSnapshot.getValue(String.class);
//                    Log.d(TAG, "Value is: " + value);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.w(TAG, "Failed to read value.", error.toException());
//                }
//            });


        } else {
            firebaseSigin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In!!!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed in Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    // implementa um listener para o menu
    private void MenuListener(DrawerLayout drawerLayout) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(false);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        onOptionsItemSelected(menuItem);

                        return true;
                    }
                });
    }

    // implementa a tela de login do Firebase
    protected void firebaseSigin() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    // implementa as açoes do menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_perfil:
                startActivity(new Intent(this, PerfilActivity.class));
                return true;
            case R.id.nav_excluir_conta:
                startActivity(new Intent(this, ExcluiContaActivity.class));
                return true;
            case R.id.nav_sair:
                signOutFireBase();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // implementa o signout
    public void signOutFireBase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        firebaseSigin();
                    }
                });

    }
}
