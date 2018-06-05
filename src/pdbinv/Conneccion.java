/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdbinv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 */
public class Conneccion {

    public static void main(String[] args) {
        /*^^^^^^^^^^^^^^^^^^Main basico para pruebas^^^^^^^^^^^^^^^^^^^^^^^^*/
        System.out.println("Inicio:");
        Estudiante e1 = LoadData(18280007);
    }

    public static Connection GetConneccion() {
        try {
//Se conecta a la base de datos
//1 Carga el driver jdbc
            Class.forName("com.mysql.jdbc.Driver");
//Se almacena en una string la db a ejecutar
            String myUrl = "jdbc:mysql://localhost/pseudosii";
//Se intenta conexion
            Connection c = DriverManager.getConnection(myUrl, "root", "25041290");
            System.out.println("Conexión establecida.");
            return c;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error en conexion " + e);
            return null;
        }
    }

    public static ResultSet Queri(Connection u, String consulta) {
//conmprueba si la conexion es valida tras asignarla a una var auxiliar
        Connection c = u;
        if (c != null) {
            try {
                //Generamos los preparativos para realizar la consulta
                Statement stmt = c.createStatement();
                ResultSet rs;
                //Ejecutamos la consulta
                rs = stmt.executeQuery(consulta);
                return rs;
            } catch (SQLException e) {
                //En caso de error especifica donde y cuando, notese que es diferente a el error en otros metodos
                System.out.println("Algo ha ocurrido al realizar la query | " + e);
                return null;
            }
        } else {
            return null;
        }
    }

    public static Estudiante GetStData(Estudiante UL) {
        //Se crea un nuevo objeto estudiante
        Estudiante temp = UL;
        //Se establece conexion a la db
        Connection Conn = GetConneccion();
        //Se realizan las consultas de los datos del estudiante especificado
        ResultSet RS = Queri(Conn, "SELECT * FROM datp where noctrl = " + temp.getNoctrl() + ";");
        ResultSet RiS = Queri(Conn, "SELECT * FROM datm where idAlumn = " + temp.getNoctrl() + ";");

        try {
            if (RS.next()) {
                //Almacenamos los datos *Esto no es necesario en la estructura actual y requiere actualizarze
                String[] nombres = {RS.getString("nombre"), RS.getString("apellidop"), RS.getString("apellidom")};
                //seteo de los datos dentro del objeto
                temp.setNombre(nombres[0]);
                temp.setApellidoP(nombres[1]);
                temp.setApellidoM(nombres[2]);
                if (RiS.next()) {
                    temp.setSemestre(RiS.getInt("semestre"));
                    //SOUT de diagnostico
                    //System.out.println(RiS.getInt("semestre"));
                    temp.setCreditos(RiS.getInt("creditosa"));
                }
            }
            Conn.close();
        } catch (SQLException e) {
            //En caso de error se genera una respuesta diferente a los otros metodos
            System.out.println("Ocurrio un error al intentar obtener los datos personales \n @error" + e);
        }
        return temp;
    }

    public static Estudiante GetAdeudos(Estudiante UL) {
        //Se genera estudiante temporal para modificaciones
        Estudiante temp = UL;
        //Se establece conexion
        Connection Conn = GetConneccion();
        //Se generan las consultas de la base de datos
        ResultSet RS = Queri(Conn, "SELECT * FROM adeudos WHERE idEstudiante = " + temp.getNoctrl() + ";");
        ResultSet RiS = Queri(Conn, "SELECT count(idAdeudo) FROM adeudos where idEstudiante = " + temp.getNoctrl() + ";");
        //Se genera la string para el estudiante temporal
        //esto evita que en caso de error se quede null el dato
        String[] tmpAd = {"No Adeudo", "No Adeudo", "No Adeudo", "No Adeudo", "No Adeudo"};
        try {
            int cnt = 0;
            if (RiS.next()) {
                cnt = RiS.getInt(1);
            }
            if (cnt > 0) {
                //En caso de que se encuentre en la db algun adeudo se modifica por la cantidad adeudada
                while (RS.next()) {
                    if (RS.getString("depto").equalsIgnoreCase("LIB")) {
                        tmpAd[2] = "Debe:" + RS.getString("cantidad");
                    }
                    if (RS.getString("depto").equalsIgnoreCase("ECO")) {
                        tmpAd[3] = "Debe:" + RS.getString("cantidad");
                    }
                    if (RS.getString("depto").equalsIgnoreCase("ECU")) {
                        tmpAd[1] = "Debe:" + RS.getString("cantidad");
                    }
                    if (RS.getString("depto").equalsIgnoreCase("FIN")) {
                        tmpAd[4] = "Debe:" + RS.getString("cantidad");
                    }
                    if (RS.getString("depto").equalsIgnoreCase("PRE")) {
                        tmpAd[0] = "Debe:" + RS.getString("cantidad");
                    }

                }

            }
            Conn.close();
        } catch (SQLException e) {
            //En caso de error muestra un codigo de acuerdo al metodo
            System.out.println("Error al obtener adeudos: " + e);

        }
        temp.setAdeudos(tmpAd);

        return temp;
    }

    public static Estudiante GetBanderas(Estudiante UL) {
        //Genera un estudiante temporal
        Estudiante temp = UL;
        //Establece coneccion con la base de datos
        Connection Conn = GetConneccion();
        //Realiza la consulta con la base de datos
        ResultSet RS = Queri(Conn, "SELECT flags FROM pseudosii.datp where noctrl = " + temp.getNoctrl() + ";");
        try {
            if (RS.next()) {
                //carga las banderas y lo almacena al usuario
                int vef = RS.getInt("flags");
                int a, b;
                a = vef % 10;
                b = vef % 100 / 10;
                temp.setEncuesta(a == 1);
                temp.setReinscrip(b == 1);
            }

        } catch (SQLException e) {
            //Muestra error personalizado con respecto al metodo
            System.out.println("ERROR AL CARGAR BANDERAS: " + e);
        }

        return temp;
    }

    public static Estudiante GetHorario(Estudiante UL) {
        //Genera un estudiante temporal
        Estudiante temp = UL;
        //Obtiene una conecion con la base de datos
        Connection Conn = GetConneccion();
        //Realisa consultas de la base de datos
        ResultSet RS = Queri(Conn, "SELECT C.Nombre as Materia, C.HorasT as Horas_Teoria, C.HorasP as Horas_Practica, concat(A.HoraI, ' - ', A.HoraF) as horas FROM horario A inner join clase B inner join materia C where A.idalumno = " + temp.getNoctrl() + " and A.idClase = B.idClase and B.idmat = IdMateria;");
        ResultSet Ris = Queri(Conn, "SELECT count(A.idClase) FROM horario A inner join clase B inner join materia C where A.idalumno = " + temp.getNoctrl() + " and A.idClase = B.idClase and B.idmat = IdMateria;");
        String[][] hor = null;
        try {
            if (RS != null) {
                int cnt = 0;
                if (Ris.next()) {
                    cnt = Ris.getInt(1);
                }

                if (cnt > 0) {
                    //Genera la matriz en la base de datos
                    hor = new String[cnt][4];
                    while (RS.next()) {
                        //Almacena en el reglon cada dato
                        hor[cnt - 1][0] = RS.getString("Materia");
                        hor[cnt - 1][1] = RS.getString("Horas_Teoria");
                        hor[cnt - 1][2] = RS.getString("Horas_Practica");
                        hor[cnt - 1][3] = RS.getString("horas");
                        cnt--;
                        
                    }
                }
            } else {
                //Regresa un String vacio para evitar errores
                hor = new String[1][4];
            }

        } catch (SQLException e) {
            System.out.println("ERROR AL CARGAR EL HORARIO || " + e);
            hor = null;
        }
        temp.setHorario(hor);
        return temp;
    }

    public static Estudiante LoadData(int noctrl) {
        Estudiante aux = new Estudiante();
        aux.setNoctrl(noctrl);
        System.out.println(aux.getNoctrl());
        aux = GetStData(aux);
        aux = GetAdeudos(aux);
        aux = GetBanderas(aux);
        aux = GetHorario(aux);
        System.out.println("Nombre: " + aux.getNombre() + " " + aux.getApellidoP() + " " + aux.getApellidoM());
        return aux;
    }

}
