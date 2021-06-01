package com.example.clinicalaluzpaciente.ui;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.clinicalaluzpaciente.R;
import com.example.clinicalaluzpaciente.activity.EditarCitaActivity;
import com.example.clinicalaluzpaciente.adapter.adapterCitas;
import com.example.clinicalaluzpaciente.models.Citas;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CitasFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference referenceCitas;
    private DatabaseReference referenceCitas2;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView friendList;
    private FirebaseAuth mAuth;
    String user_id;
    adapterCitas myAdapter;
    ArrayList<Citas> listaCitas=new ArrayList<>();
    private ProgressDialog progressDialog;
    android.app.AlertDialog.Builder builder;
    AlertDialog alert;

    public CitasFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static CitasFragment newInstance(String param1, String param2) {
        CitasFragment fragment = new CitasFragment();
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

        View vista = inflater.inflate(R.layout.fragment_citas, container, false);
        listaCitas = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        referenceCitas= FirebaseDatabase.getInstance().getReference("CitasReservadas").child(user_id);
        //linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
       //friendList.setLayoutManager(linearLayoutManager);
        friendList=(RecyclerView)vista.findViewById(R.id.recyclercitas);
        friendList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query q=referenceCitas;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaCitas.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Citas artist = postSnapshot.getValue(Citas.class);
                    listaCitas.add(artist);
                }
                //ArrayList<Citas> listaCitas=new ArrayList<>();
                myAdapter=new adapterCitas(listaCitas);
                friendList.setAdapter(myAdapter);

                myAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                     //   Toast.makeText(getContext(), listaCitas.get(friendList.getChildAdapterPosition(v)).getHora(), Toast.LENGTH_SHORT).show();
                        Dialogo(listaCitas.get(friendList.getChildAdapterPosition(v)).getKey());
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      /*  FirebaseRecyclerOptions<Citas> recyclerOptions = new FirebaseRecyclerOptions.Builder<Citas>()
                .setQuery(referenceCitas, Citas.class).build();
        FirebaseRecyclerAdapter<Citas, Items> adapter =new FirebaseRecyclerAdapter<Casos, Items>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final Items items, final int i, @NonNull Citas claseesprofe) {
                final String key = getRef(i).getKey();
                referenceCitas.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Context context = getContext();
                        if (dataSnapshot.exists()){
                          //  final String tipoCAso=dataSnapshot.child("tipoCAso").getValue().toString();



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public Items onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_casos, parent, false);
                return new  Items(vista);

            }
        };
        friendList.setAdapter(adapter);
        adapter.startListening();*/
    }

    //Dialgo de editar
        private  void Dialogo(String key){
            builder = new AlertDialog.Builder(getActivity());
            TextView tvfalta;
            final CharSequence[] items = new CharSequence[3];
            items[0] = "Anular";
            items[1] = "Cambiar Fecha";
            items[2] = "Otros";
           // View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_datetime, null);
           // builder.setView(v);
            builder.setTitle("Acciones").setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int posicion) {
                    if (posicion==0){
                       Anular(key);
                    }
                    if (posicion==1){
                        startActivity(new Intent(getActivity(), EditarCitaActivity.class));

                       // Toast.makeText(getActivity(), "tdoavai no implementado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alert  = builder.create();
            alert.show();
        }

        private  void Anular(String key){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Cargando..");
            progressDialog.setTitle("Eliinando");
            progressDialog.show();
            progressDialog.setCancelable(false);
            referenceCitas2= FirebaseDatabase.getInstance().getReference("CitasReservadas").child(user_id).child(key);
            referenceCitas2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( task.isSuccessful()){
                        Toast.makeText(getActivity(), "Eliminado ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    public   class Items extends RecyclerView.ViewHolder{
        TextView tvtipo,tvcliente,tvfecha,tvsteado;
        ImageView imgcam;
        String id_usario,nombreclase;
        public Items(final View itemView) {
            super(itemView);

        }
    }
}