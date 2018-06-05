/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdbinv;

/**
 *
 * @author Biot
 */
public class Estudiante {

    String nombre, apellidoP, apellidoM;

    int semestre, creditos, Noctrl;
    String[] adeudos;
    String[][] horario;
    boolean encuesta;
    boolean reinscrip;

    public Estudiante() {
        this.Noctrl = 0;
        this.nombre = null;
        this.apellidoP = null;
        this.apellidoM = null;
        this.semestre = 0;
        this.creditos = 0;
        this.adeudos = null;
        this.horario = null;
        this.encuesta = false;
        this.reinscrip = false;
    }

    public Estudiante(int NoCtrl, String nombre, String apellidoP, String apellidoM, int semestre, int creditos, String[] adeudos, String[][] horario, boolean encuesta, boolean reinscrip) {
        this.Noctrl=NoCtrl;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.semestre = semestre;
        this.creditos = creditos;
        this.adeudos = adeudos;
        this.horario = horario;
        this.encuesta = encuesta;
        this.reinscrip = reinscrip;
    }

    public int getNoctrl() {
        return Noctrl;
    }

    public void setNoctrl(int Noctrl) {
        this.Noctrl = Noctrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public String[] getAdeudos() {
        return adeudos;
    }

    public void setAdeudos(String[] adeudos) {
        this.adeudos = adeudos;
    }

    public String[][] getHorario() {
        return horario;
    }

    public void setHorario(String[][] horario) {
        this.horario = horario;
    }

    public boolean isEncuesta() {
        return encuesta;
    }

    public void setEncuesta(boolean encuesta) {
        this.encuesta = encuesta;
    }

    public boolean isReinscrip() {
        return reinscrip;
    }

    public void setReinscrip(boolean reinscrip) {
        this.reinscrip = reinscrip;
    }

}
