package com.example.clinicalaluzpaciente.models;

import java.io.Serializable;

public class DocIdentidad   {

    private String tipo;
    private String codigo;

    public  DocIdentidad(){

    }
    public DocIdentidad( String tipo, String codigo) {
       // this.id = id;
        this.tipo = tipo;
        this.codigo = codigo;
    }



    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
