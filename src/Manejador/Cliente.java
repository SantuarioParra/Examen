package Manejador;

import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String []args) throws IOException {
        String comando="";
        String respuesta="";
        Socket conexion= new Socket("localhost",2000);
        System.out.println("Conexion establecida...");
        System.out.println();
        System.out.println("#########################################");
        System.out.println("<---------> A-DATABASE 2018-2019 <-------->");
        System.out.println("RicoSoft,Inc. SantuarioSQL");
        System.out.println();

        BufferedReader inDatos= new BufferedReader( new InputStreamReader(System.in));
        PrintWriter outDatos= new PrintWriter(new OutputStreamWriter(conexion.getOutputStream()));

        BufferedReader rDatos = new BufferedReader(new InputStreamReader(conexion.getInputStream()));

        while (true) {

            System.out.print(">> ");
            comando = inDatos.readLine();
            outDatos.println(comando);
            outDatos.flush();
            respuesta = rDatos.readLine();
            if(respuesta.compareTo("Sesion terminada")==0){
                System.out.println(">> " + respuesta);
            }else {
                System.out.println(">> " + respuesta);
            }
        }

    }
}
