package Manejador;

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
                comandoBruto=comandoBruto.toLowerCase();
                comandoLimpio=comandoBruto.split(" ");
                System.out.println(">> "+comandoLimpio[0]+" "+comandoLimpio[1]+" Recibido");

                //Inicio condicionales del servidor para las acciones de Create,Update,Delete,Insert,Show

                if(comandoLimpio[0].compareTo("create")==0&&comandoLimpio[1].compareTo("database")==0){//if Creacion de tabla

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

                    if(basesDatos.get(nombreSchema).containsKey(comandoLimpio[2])){
                        outDatos.println("Ya existe una tabla en la base de datos con nombre similar, seleccione un nuevo nombre.");
                        outDatos.flush();
                    }else {

                        System.out.println(comandoLimpio[2]);
                        basesDatos.get(nombreSchema).put(comandoLimpio[2], new LinkedList<>());
                        outDatos.println("Tabla " + comandoLimpio[2] + " creada en la base de datos " + nombreSchema);
                        System.out.println("Clave tabla: " + basesDatos.get(nombreSchema).get(comandoLimpio[2]));
                        outDatos.flush();
                        //Creacion del objeto dinamico
                        String[]datos= new String[comandoLimpio.length-6];
                        Stack<String> parentesis= new Stack<>();
                        for (int i=3; i<comandoLimpio.length;i++) {
                            if(comandoLimpio[i].compareTo("(")==0){
                                parentesis.push(comandoLimpio[i]);
                            }else if(comandoLimpio[i].compareTo(")")==0){
                                parentesis.pop();
                            }else{
                                datos[i]=comandoLimpio[i];
                            }
                        }
                        for(int i=0;i<datos.length;i++){
                            System.out.println(datos[i]);
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
