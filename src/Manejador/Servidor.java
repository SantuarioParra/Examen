package Manejador;

import javax.tools.JavaFileObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Servidor {
    public static void main(String []args) throws IOException {

        //Declaracion de los hash basesde datos y tablas
        HashMap<String,HashMap<String,ArrayList<Object>>> basesDatos= new HashMap<> ();


        //Fin de declaracion de los hash basesde datos y tablas

        // Strings para validar y almacenar el comando ingresado por el usuario
        String nombreSchema="";
        String comandoBruto="";
        String comandoBrutoCopia="";
        String comandoLimpio[];
        String columnas[];

        //Declaracion del servidor y su puerto
        ServerSocket servidor= new ServerSocket(2000);
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


                if(comandoLimpio[0].compareTo("exit")==0){//if cerrar sesion
                    System.out.println(comandoLimpio[0]);
                    outDatos.println("Sesion terminada");
                    outDatos.flush();
                    servidor.close();

                }else if(comandoLimpio[0].compareTo("create")==0&&comandoLimpio[1].compareTo("database")==0){//if Creacion de base de datos

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

                }else if(comandoLimpio[0].compareTo("set")==0&&comandoLimpio[1].compareTo("database")==0){// if Seleccionar base de datos

                    nombreSchema="";
                    nombreSchema=comandoLimpio[2];
                    if(basesDatos.containsKey(nombreSchema)==true){                         //verifica si existe la base de datos
                        outDatos.println("Base de datos: "+nombreSchema+" establecida");
                        outDatos.flush();
                    }else{
                        outDatos.println("La base de datos: "+nombreSchema+" no existe creela primero o seleccione una base existente");
                        outDatos.flush();
                    }


                }else if(comandoLimpio[0].compareTo("create")==0&&comandoLimpio[1].compareTo("table")==0){//if Crear tabla en base de datos seleccionada


                    Compilador compilador=new Compilador();
                    if(nombreSchema.compareTo("")==0){                  //if verifica si esta seleccionada una base de datos
                        outDatos.println("No se ha seleccionado una base de datos");
                        outDatos.flush();
                    }else {
                        columnas=comandoLimpio;
                        if (basesDatos.get(nombreSchema).containsKey(comandoLimpio[2])) {
                            outDatos.println("Ya existe una tabla en la base de datos con nombre similar, seleccione un nuevo nombre.");
                            outDatos.flush();
                        } else {
                            //Creacion del objeto dinamico
                            String[] datos;
                            datos = comandoBrutoCopia.split(" ");
                            if ((datos.length - 3) % 2 == 0) {
                                System.out.println(comandoLimpio[2]);
                                basesDatos.get(nombreSchema).put(comandoLimpio[2], new ArrayList<Object>());
                                JavaFileObject file = compilador.compilarObjeto(comandoLimpio[2], datos,basesDatos.get(nombreSchema).get(comandoLimpio[2]));
                                Iterable<? extends JavaFileObject> files = Arrays.asList(file);
                                compilador.compile(files);

                                outDatos.println("Tabla " + comandoLimpio[2] + " creada en la base de datos " + nombreSchema);
                                System.out.println("Clave tabla: " + basesDatos.get(nombreSchema).get(comandoLimpio[2]));
                                outDatos.flush();
                            } else {
                                outDatos.println("La sentencia tiene algun error");
                                outDatos.flush();
                            }
                            //fin creaccion del objeto dinamico


                            for (HashMap.Entry entry : basesDatos.entrySet()) {
                                System.out.println(entry.getKey() + ", " + entry.getValue());
                            }
                        }
                    }
                }else if(comandoLimpio[0].compareTo("delete")==0&&comandoLimpio[1].compareTo("database")==0){ //if borrar base de datos

                    if (basesDatos.containsKey(comandoLimpio[2])==true){

                        basesDatos.remove(comandoLimpio[2]);
                        for (HashMap.Entry entry : basesDatos.entrySet()) {
                            System.out.println(entry.getKey() + ", " + entry.getValue());
                        }
                        outDatos.println("La base de datos: "+comandoLimpio[2]+" fue eliminada exitosamente");
                        outDatos.flush();
                    }else{
                        outDatos.println("La base de datos no existe");
                        outDatos.flush();
                    }
                }else if(comandoLimpio[0].compareTo("delete")==0&&comandoLimpio[1].compareTo("table")==0){// if borrar tablas

                    if (basesDatos.get(nombreSchema).containsKey(comandoLimpio[2])){
                        basesDatos.get(nombreSchema).remove(comandoLimpio[2]);

                        for (HashMap.Entry entry : basesDatos.entrySet()) {
                            System.out.println(entry.getKey() + ", " + entry.getValue());
                        }
                        outDatos.println("La tabla : "+comandoLimpio[2]+" fue eliminada exitosamente");
                        outDatos.flush();
                    }else {
                        outDatos.println("La tabla no existe en la base de datos");
                        outDatos.flush();
                    }

                }else if(comandoLimpio[0].compareTo("insert")==0&&comandoLimpio[1].compareTo("into")==0){       //if insert en la tabla

                    if(nombreSchema.compareTo("")==0){                  //if verifica si esta seleccionada una base de datos
                        outDatos.println("No se ha seleccionado una base de datos");
                        outDatos.flush();
                    }else {

                        if(basesDatos.get(nombreSchema).containsKey(comandoLimpio[2])==true){
                            String[] metodosDatos;
                            metodosDatos = comandoBrutoCopia.split(" ");
                            if ((metodosDatos.length - 3) % 2 == 0) {
                                System.out.println("Nombre Tabla a insertar: "+comandoLimpio[2]);
                                Compilador compilador=new Compilador();
                                System.out.println(compilador.realizarOperacion(comandoLimpio[2],metodosDatos,(HashMap<String,Class<?>>)basesDatos.get(nombreSchema).get(comandoLimpio[2]).get(0)));
                                basesDatos.get(nombreSchema).get(comandoLimpio[2]).add(compilador.realizarOperacion(comandoLimpio[2],metodosDatos,(HashMap<String,Class<?>>)basesDatos.get(nombreSchema).get(comandoLimpio[2]).get(0)));

                                /*/ add a / to uncomment this block
                                djs l = new djs();
                                l.setName("armin");
                                Object lo=l;
                                basesDatos.get(nombreSchema).get(comandoLimpio[2]).add(lo);
                                //*/
                                outDatos.println("Datos insertados correctamente en la tabla: "+comandoLimpio[2]+", de la base de datos: "+ nombreSchema);
                                System.out.println("Contenido de la  tabla: " + basesDatos.get(nombreSchema).get(comandoLimpio[2]).toString());

                                outDatos.flush();
                            } else {
                                outDatos.println("La sentencia tiene algun error");
                                outDatos.flush();
                            }


                        }else{
                            outDatos.println("La tabla a insertar no existe");
                            outDatos.flush();
                        }


                    }

                } else if(comandoLimpio[0].compareTo("select")==0){              //if select
                    String resultado="";
                    String[] metodosDatos;
                    metodosDatos = comandoBrutoCopia.split(" ");
                    Compilador compilador=new Compilador();
                    if(nombreSchema.compareTo("")==0){                  //if verifica si esta seleccionada una base de datos
                        outDatos.println("No se ha seleccionado una base de datos");
                        outDatos.flush();
                    }else {

                        if(comandoLimpio[1].compareTo("from")==0){
                            resultado=compilador.realizarConsulta(comandoLimpio[2],metodosDatos, basesDatos.get(nombreSchema).get(comandoLimpio[2]));
                            outDatos.println("Los valores de la consulta son:  "+resultado);
                            outDatos.flush();
                        }else if(comandoLimpio[1].compareTo("*")==0&&comandoLimpio[2].compareTo("from")==0){
                            outDatos.println("Contenido de la  tabla: " + basesDatos.get(nombreSchema).get(comandoLimpio[3]).toString());
                            outDatos.flush();
                        }
                    }

                }
            }
        }
    }
}
