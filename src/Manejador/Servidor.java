package Manejador;

import javax.tools.JavaFileObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class Servidor {
    public static void main(String []args) throws IOException {

        //Declaracion de los hash basesde datos y tablas
        HashMap<String,HashMap<String,LinkedList<Object>>> basesDatos= new HashMap<> ();


        //Fin de declaracion de los hash basesde datos y tablas

        // Strings para validar y almacenar el comando ingresado por el usuario
        String nombreSchema="";
        String comandoBruto="";
        String comandoBrutoCopia="";
        String comandoLimpio[];

        //Declaracion del servidor y su puerto
        ServerSocket servidor= new ServerSocket(3000);
        System.out.println("Servidor Iniciado...");

        for(;;){
            Socket conexion= servidor.accept();
            System.out.println("Conexion Establecida... "+ conexion.getInetAddress()+" :"+conexion.getPort());
            BufferedReader inDatos = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            PrintWriter outDatos= new PrintWriter(new OutputStreamWriter(conexion.getOutputStream()));

            while (true){                                           //ciclo para estar leyendo lo que el cliente envia

                comandoBruto=inDatos.readLine();
                comandoBrutoCopia=comandoBruto;
                comandoBruto=comandoBruto.toLowerCase();
                comandoLimpio=comandoBruto.split(" ");

                if(comandoLimpio.length==1){
                    System.out.println(">> "+comandoLimpio[0]+" Recibido");
                }else {
                    System.out.println(">> " + comandoLimpio[0] + " " + comandoLimpio[1] + " Recibido");
                }
                //Inicio condicionales del servidor para las acciones de Create,Update,Delete,Insert,Show


                if(comandoLimpio[0].compareTo("exit")==0){
                    System.out.println(comandoLimpio[0]);
                    outDatos.println("Sesion terminada");
                    outDatos.flush();
                    servidor.close();

                }else if(comandoLimpio[0].compareTo("create")==0&&comandoLimpio[1].compareTo("database")==0){//if Creacion de tabla

                    if(basesDatos.containsKey(comandoLimpio[2])){

                        outDatos.println("Ya existe una base de datos con nombre similar, seleccione un nuevo nombre.");
                        outDatos.flush();

                    }else{

                        basesDatos.put(comandoLimpio[2],new HashMap<>());
                        System.out.println(basesDatos.get(comandoLimpio[2]));

                        outDatos.println("Base de datos "+comandoLimpio[2]+" creada.");
                        outDatos.flush();

                        for (HashMap.Entry entry : basesDatos.entrySet()) {
                            System.out.println(entry.getKey() + ", " + entry.getValue());
                        }

                    }

                }else if(comandoLimpio[0].compareTo("set")==0&&comandoLimpio[1].compareTo("database")==0){// if poner(Set) base de datos

                    nombreSchema="";
                    nombreSchema=comandoLimpio[2];
                    outDatos.println("Base de datos: "+nombreSchema+" establecida");
                    outDatos.flush();

                }else if(comandoLimpio[0].compareTo("create")==0&&comandoLimpio[1].compareTo("table")==0){//if Crear tabla en base de datos seleccionada


                    Compilador compilador=new Compilador();
                    if(basesDatos.get(nombreSchema).containsKey(comandoLimpio[2])){
                        outDatos.println("Ya existe una tabla en la base de datos con nombre similar, seleccione un nuevo nombre.");
                        outDatos.flush();
                    }else {
                        //Creacion del objeto dinamico
                        String[]datos;
                        String parametros="";
                           datos=comandoBrutoCopia.split(" ");
                           if((datos.length-3)%2==0){
                               System.out.println(comandoLimpio[2]);
                               basesDatos.get(nombreSchema).put(comandoLimpio[2], new LinkedList<>());
                               JavaFileObject file = compilador.compilarObjeto(comandoLimpio[2],datos);
                               Iterable<? extends JavaFileObject> files = Arrays.asList(file);
                               compilador.compile(files);

                               outDatos.println("Tabla " + comandoLimpio[2] + " creada en la base de datos " + nombreSchema);
                               System.out.println("Clave tabla: " + basesDatos.get(nombreSchema).get(comandoLimpio[2]));
                               outDatos.flush();
                           }else{
                               outDatos.println("La sentencia tiene algun error");
                               outDatos.flush();
                           }
                        //fin creaccion del objeto dinamico


                        for (HashMap.Entry entry : basesDatos.entrySet()) {
                            System.out.println(entry.getKey() + ", " + entry.getValue());
                        }
                    }

                }


            }
        }
    }
}
