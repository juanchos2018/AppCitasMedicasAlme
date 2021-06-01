package com.example.clinicalaluzpaciente.models;

public class CodigoMedico {

    private int id;
    private TipoCodMedEnum tipo;
    private int numero;

    public CodigoMedico(int id, TipoCodMedEnum tipo, int numero) {
        this.id = id;
        this.tipo = tipo;
        this.numero = numero;
    }

}
