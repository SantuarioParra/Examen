package Manejador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class Servidor {
    
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
                System.out.println(">> "+comando+" Recibido");

            }
        }
    }
}
