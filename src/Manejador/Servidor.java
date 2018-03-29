package Manejador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Servidor {
    public static void main(String []args) throws IOException {
        //Declaracion de los hash basesde datos y tablas
        HashMap<String,HashMap<String,LinkedList<Object>>> basesDatos= new HashMap<String,HashMap<String,LinkedList<Object>>> ();
        HashMap<String,LinkedList<Object>> tablas= new HashMap<String,LinkedList<Object>>();
        //Fin de declaracion de los hash basesde datos y tablas

        // Strings para validar y almacenar el comando ingresado por el usuario
        String comandoBruto="";
        String comandoLimpio[];

        //Declaracion del servidor y su puerto
        ServerSocket servidor= new ServerSocket(3000);
        System.out.println("Servidor Iniciado...");
        for(;;){
            Socket conexion= servidor.accept();
            System.out.println("Conexion Establecida... "+ conexion.getInetAddress()+" :"+conexion.getPort());
            BufferedReader inDatos = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            while (true){                                           //ciclo para estar leyendo lo que el cliente envia
                comandoBruto=inDatos.readLine();
                comandoBruto=comandoBruto.toLowerCase();
                comandoLimpio=comandoBruto.split(" ");
                System.out.println(">> "+comandoLimpio[0]+" Recibido");

                //Inicio condicionales del servidor para las acciones de Create,Update,Delete,Insert,Show
                if(comandoLimpio[0].compareTo("create")==0&&comandoLimpio[1].compareTo("database")==0){

                    if(basesDatos.containsKey(comandoLimpio[2])){

                        System.out.println("Ya existe una base de datos con nombre similar.");

                    }else{

                        basesDatos.put(comandoLimpio[2],tablas);
                        System.out.println(basesDatos.get(comandoLimpio[2]));

                        for (HashMap.Entry entry : basesDatos.entrySet()) {
                            System.out.println(entry.getKey() + ", " + entry.getValue());
                        }

                    }

                }


            }
        }
    }
}
