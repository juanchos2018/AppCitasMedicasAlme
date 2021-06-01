package com.example.clinicalaluzpaciente;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText inputEmail, inputPassword;
    private MaterialAutoCompleteTextView mactvTipo;
    private DocumentSnapshot paciente;
    ///private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.fl_et_email);
        inputPassword = findViewById(R.id.fl_et_contrasena);
        //mactvTipo = findViewById(R.id.fl_mactv_como);
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        Button signupButton =findViewById(R.id.fl_mb_registrarse);
        Button startButton = findViewById(R.id.fl_b_login);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String tipoUsuario ="Paciente";
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Ingrese su email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(tipoUsuario)) {
                    Toast.makeText(LoginActivity.this, "Seleccione su tipo de usuario", Toast.LENGTH_SHORT).show();
                    return;
                }
                Login(email,password,tipoUsuario);
            }
        });

    }

    private void Login(String email, String password, String tipoUsuario) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Un momento");
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
///private FirebaseAuth mAuth;
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Authentication fallida ." + task.getException(), Toast.LENGTH_SHORT).show();
            } else {
                int collectionName;
                //tipoUsuario = paciente
                switch (tipoUsuario) {
                    case "Paciente": collectionName = R.string.db_collection_pacientes; break;
                    case "Médico": collectionName = R.string.db_collection_medicos; break;
                    case "Administrador": collectionName = R.string.db_collection_administradores; break;
                    default: collectionName = 0; break;
                }
                db.collection(getString(collectionName)).whereEqualTo("email", email).get().addOnCompleteListener(task1 -> {
                    if (task.isSuccessful()) {

                        QuerySnapshot document = task1.getResult();
                        assert document != null;
                        if (!document.isEmpty()) {

                            paciente = document.getDocuments().get(0);

                            SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences("usuario", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString("nombres", paciente.getString("nombres"));
                            editor.putString("apellidos", paciente.getString("apellidos"));
                            editor.putString("email", paciente.getString("email"));
                            editor.putString("tipoUsuario", tipoUsuario);

                            editor.apply();
                            //  private FirebaseUser user;
                            user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                            startActivity(intent);

                        } else {
                            Log.d(TAG, "No hay datos coincidentes");
                            Toast.makeText(this, "No hay datos coincidentes", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    } else {
                        Log.d(TAG, "Error obteniendo documentos: ", task.getException());
                        Toast.makeText(this, "Error obteniendo documento", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }
}