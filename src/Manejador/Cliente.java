package Manejador;

import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String []args) throws IOException {
        String comando="";
        Socket conexion= new Socket("localhost",3000);
        System.out.println("Conexion establecida...");
        System.out.println();
        System.out.println("#########################################");
        System.out.println("<---------> A-DATABASE 2018-2019 <-------->");
        System.out.println("RicoSoft,Inc.");
        System.out.println();
        BufferedReader inDatos= new BufferedReader( new InputStreamReader(System.in));
        PrintWriter outDatos= new PrintWriter(new OutputStreamWriter(conexion.getOutputStream()));
        while (true){
            System.out.print(">> ");
            comando=inDatos.readLine();
            outDatos.println(comando);
            outDatos.flush();
        }
    }
}
