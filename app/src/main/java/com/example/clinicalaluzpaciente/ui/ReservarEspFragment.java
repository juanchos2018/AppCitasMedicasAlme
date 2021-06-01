package com.example.clinicalaluzpaciente.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clinicalaluzpaciente.R;
import com.example.clinicalaluzpaciente.models.Especialidad;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservarEspFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservarEspFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CollectionReference espRef;




    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView friendList;
    RecyclerView recyclerView;
    NavController navController;

    public ReservarEspFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ReservarEspFragment newInstance(String param1, String param2) {
        ReservarEspFragment fragment = new ReservarEspFragment();
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

        View vista = inflater.inflate(R.layout.fragment_reservar_esp, container, false);
        friendList =vista.findViewById(R.id.recyclerespecialidad);
        init();
        getFriendList();



        return  vista;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

    }
    private void init(){
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        friendList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
    }
    private void getFriendList(){
        Query query = db.collection("especialidades");
        FirestoreRecyclerOptions<Especialidad> response = new FirestoreRecyclerOptions.Builder<Especialidad>()
                .setQuery(query, Especialidad.class)
                .build();
      //  private FirestoreRecyclerAdapter adapter;
        adapter = new FirestoreRecyclerAdapter<Especialidad, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(FriendsHolder holder, int position, Especialidad model) {
                holder.tvnombre.setText(model.getNombre());
                holder.tvdescripcion.setText(model.getDescripcion());
                Picasso.get()
                        .load(model.getUrlImagen())
                        .fit().centerCrop()
                        .into(holder.img);

                holder.itemView.setOnClickListener(v -> {
                  // ReservarMedicoFragment fragment = new ReservarMedicoFragment();
                  // Bundle bundle = new Bundle();
                  // bundle.putString("key", model.getNombre());
                  // fragment.setArguments(bundle);


                    navController.navigate(R.id.nav_reservar_medico);
                });
            }

            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_especialidad, group, false);
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
        TextView tvnombre,tvdescripcion;
        ImageView img;
        public FriendsHolder(View itemView) {
            super(itemView);
            tvnombre=(TextView)itemView.findViewById(R.id.idtvnombreespecialidad);
            tvdescripcion=(TextView)itemView.findViewById(R.id.idtvdescripcion);
            img=(ImageView)itemView.findViewById(R.id.imgespe);
        }
    }
}