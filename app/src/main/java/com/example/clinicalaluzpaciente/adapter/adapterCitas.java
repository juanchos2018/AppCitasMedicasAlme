package com.example.clinicalaluzpaciente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clinicalaluzpaciente.R;
import com.example.clinicalaluzpaciente.models.Citas;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class adapterCitas extends RecyclerView.Adapter<adapterCitas.ViewHolderDatos>  implements View.OnClickListener {

    ArrayList<Citas> listaPersonaje;

    public adapterCitas(ArrayList<Citas> listaPersonaje) {
        this.listaPersonaje = listaPersonaje;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_citas_reservadas,parent,false);
        vista.setOnClickListener(this);
        return new ViewHolderDatos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderDatos holder, int position) {
        if (holder instanceof ViewHolderDatos){
            //  final Dato dato=
            final ViewHolderDatos datgolder =(ViewHolderDatos)holder;
            datgolder.fecha.setText(listaPersonaje.get(position).getFecha());
            datgolder.hora.setText(listaPersonaje.get(position).getHora());
            datgolder.nombremedico.setText(listaPersonaje.get(position).getNombreMedico());

        }
    }

    @Override
    public int getItemCount() {
        return listaPersonaje.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }
    private View.OnClickListener listener;

    public  void setOnClickListener(View.OnClickListener listener)
    {
        this.listener=listener;
    }
    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView fecha,hora,nombremedico,especialidad;
        public ViewHolderDatos(@NonNull @NotNull View itemView) {
            super(itemView);
            fecha=(TextView)itemView.findViewById(R.id.idtvFechaCita);
            hora=(TextView)itemView.findViewById(R.id.idtvHoraCita);
            nombremedico=(TextView)itemView.findViewById(R.id.idtvNombreMedico);

        }
    }
}
