package com.example.clinicalaluzpaciente.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.clinicalaluzpaciente.R;
import com.example.clinicalaluzpaciente.models.Citas;
import com.example.clinicalaluzpaciente.models.Medico;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservarMedicoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservarMedicoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView friendList;
    RecyclerView recyclerView;
    NavController navController;

    android.app.AlertDialog.Builder builder;
    AlertDialog alert;
    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    private DatabaseReference referenceCitas;
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    private static final String DOS_PUNTOS = ":";
    private ProgressDialog progressDialog;
    String fecha;
    private FirebaseAuth mAuth;
    String user_id;
    String especialidad_medico;
    public ReservarMedicoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservarMedicoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservarMedicoFragment newInstance(String param1, String param2) {
        ReservarMedicoFragment fragment = new ReservarMedicoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista =inflater.inflate(R.layout.fragment_reservar_medico, container, false);
        friendList =vista.findViewById(R.id.recyclermedidos);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String myInt = bundle.getString("key", "0");
            Toast.makeText(getContext() ,myInt, Toast.LENGTH_SHORT).show();
        }
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        referenceCitas= FirebaseDatabase.getInstance().getReference("CitasReservadas").child(user_id);
        init();
        getFriendList();
        return vista;

    }

    private void init(){

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        friendList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }
    private  void ReservarCita(String fecha,String hora,String idmedico,String nombremedico)
    {

        ///reservar cita que manda aal bd Firebae
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Agregajdo Cita");
        progressDialog.setMessage("cargando...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String key = referenceCitas.push().getKey();

        // para gaurar el nodo Citas
        // sadfsdfsdfsd1212sdss
        Citas objecita =new Citas(key,user_id,"user",fecha,hora,idmedico,nombremedico,"especilidad" ,"Nuevo"  );

      //  referenceCitas= FirebaseDatabase.getInstance().getReference("CitasReservadas").child(user_id);
        referenceCitas.child(key).setValue(objecita).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Agregado", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error :" +e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    //Dialogo
    private void Reservar(String idmedico,String nombremedico){
        builder = new AlertDialog.Builder(getActivity());
        Button btcerrrar,btnreservar;
        ImageButton btnfecha,btnhora;
        EditText fechaCita,horacita;
        TextView tvfalta;
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_datetime, null);
        builder.setView(v);
        btcerrrar=(Button)v.findViewById(R.id.btncerrar);
        btnreservar=(Button)v.findViewById(R.id.iniciar_button);
        btnfecha=(ImageButton)v.findViewById(R.id.ib_obtener_fecha);
        fechaCita=(EditText)v.findViewById(R.id.etFecha);
        btnhora=(ImageButton)v.findViewById(R.id.ib_obtener_hora);
        horacita=(EditText)v.findViewById(R.id.etHora);
        btnfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calendariopo

                DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final int mesActual = month + 1;
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        fecha  =diaFormateado + BARRA + mesFormateado + BARRA + year;
                        //btn aceptar
                        fechaCita.setText(fecha);
                    }
                },anio, mes, dia);
                recogerFecha.show();
            }
        });
        btnhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        horacita.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        // etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                    }
                }, hora, minuto, false);
                recogerHora.show();
            }
        });
        btcerrrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        btnreservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fe =fechaCita.getText().toString();
                String hora =horacita.getText().toString();
                if (TextUtils.isEmpty(fe)){
                    Toast.makeText(getContext(), "Poner Fecha", Toast.LENGTH_SHORT).show();
                    return;
                }

                ReservarCita(fe,hora,idmedico,nombremedico);
            }
        });

        alert  = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }


    private void getFriendList(){
        Query query = db.collection("medicos");
        FirestoreRecyclerOptions<Medico> response = new FirestoreRecyclerOptions.Builder<Medico>()
                .setQuery(query, Medico.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Medico, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(FriendsHolder holder, int position, Medico model) {
                holder.tvnombre.setText(model.getNombres());
                holder.btnserservar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String docId = getSnapshots().getSnapshot(position).getId();
                        String nombremedico =model.getNombres();
                        Reservar(docId,nombremedico);

                    }
                });
            }
            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_medicos, group, false);
                return new FriendsHolder(view);
            }
            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };
        friendList.setAdapter(adapter);
        adapter.startListening();
    }
    public class FriendsHolder extends RecyclerView.ViewHolder {
        TextView tvnombre,tvfechaclase,tvnombresalon,tvnota;
        Button btnserservar;
        public FriendsHolder(View itemView) {
            super(itemView);
            tvnombre=(TextView)itemView.findViewById(R.id.tvnombredoctor);
            btnserservar=(Button)itemView.findViewById(R.id.btnrsservar);

        }
    }
}