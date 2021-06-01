package com.example.clinicalaluzpaciente.models;

import java.util.List;

public class Medico {

    private DocIdentidad docIdentidad;
    private String nombres;
    private String apellidos;
    private String celular;



    public Medico(DocIdentidad docIdentidad, String nombres, String apellidos, String celular) {
        this.docIdentidad = docIdentidad;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
    }

    public Medico() {
    }
    public DocIdentidad getDocIdentidad() {
        return docIdentidad;
    }

    public void setDocIdentidad(DocIdentidad docIdentidad) {
        this.docIdentidad = docIdentidad;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
