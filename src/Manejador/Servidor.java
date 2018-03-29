package Manejador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class Servidor {
    HashMap<String,HashMap<String,LinkedList<Object>>> schemas= new HashMap<String,HashMap<String,LinkedList<Object>>> ();
    HashMap<String,LinkedList<Object>> tablas= new HashMap<String,LinkedList<Object>>();

    public static void main(String []args) throws IOException {
        String comando="";
        ServerSocket servidor= new ServerSocket(3000);
        System.out.println("Servidor Iniciado...");
        for(;;){
            Socket conexion= servidor.accept();
            System.out.println("Conexion Establecida... "+ conexion.getInetAddress()+" :"+conexion.getPort());
            BufferedReader inDatos = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            while (true){
                comando=inDatos.readLine();
                comando=comando.toLowerCase();
                System.out.println(">> "+comando+" Recibido");


            }
        }
    }
}
