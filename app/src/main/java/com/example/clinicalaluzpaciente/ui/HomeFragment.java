package com.example.clinicalaluzpaciente.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.clinicalaluzpaciente.R;
import com.example.clinicalaluzpaciente.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private LinearLayout bReservar, bCitas, bHistorial, bUbicanos;
    private TextView tvPaciente;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =   new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("usuario",
                Context.MODE_PRIVATE);

        String nombre = sharedPref.getString("nombres", "<nombres>");

        tvPaciente = requireView().findViewById(R.id.home_tv_paciente);
        tvPaciente.setText(String.format("Bienvenido %s!", nombre));

        bReservar = requireView().findViewById(R.id.fph_ll_reservar);
        bReservar.setOnClickListener(view1 ->
                navController.navigate(R.id.nav_reservar_esp));

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}