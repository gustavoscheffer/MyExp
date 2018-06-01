package br.com.fadergs.newideas.myexp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instancia do user no Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // verifica se o user esta logado
        if (firebaseUser != null) {

            //menuListener
            mDrawerLayout = findViewById(R.id.drawer_layout);
            MenuListener(mDrawerLayout);

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
                        menuItem.setChecked(true);
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
                AuthUI.getInstance().signOut(this);
                firebaseSigin();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}
