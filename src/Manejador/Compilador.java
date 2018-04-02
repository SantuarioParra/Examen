//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Manejador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.LoggingMXBean;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

public class Compilador {
    private static String classOutputFolder = Compilador.class.getResource("build/classes").getPath();

    public Compilador() {
    }

    public static JavaFileObject compilarObjeto(String nombre, String[] datos, ArrayList<Object> objects) throws IOException {
        String parametros = "";
        HashMap<String,Class<?>> tipos=new HashMap<>();
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("import java.io.*;");
        System.out.println("import java.io.*;");
        out.println("public class " + nombre + "  implements Serializable {");
        System.out.println("public class " + nombre + "  implements Serializable {");
        int i;
        for(i = 3; i < datos.length; i += 2) {
            out.println(datos[i+1] + " " + datos[i] + ";");
            tipos.put(datos[i],parseType(datos[i+1]));
            System.out.println(datos[i+1] + " " + datos[i] + ";");
        }
        objects.add(tipos);

        out.println("  public " + nombre + "() {");
        System.out.println("  public " + nombre + "() {");

        out.println("  }");
        System.out.println("  }");

        for(i = 3; i < datos.length; i += 2) {
            out.println("public " + datos[i+1] + " get" + datos[i] + "(){");
            System.out.println("public " + datos[i+1] + " get" + datos[i] + "(){");
            out.println("return this." + datos[i] + ";");
            System.out.println("return this." + datos[i] + ";");
            out.println("}");
            System.out.println("}");
            out.println("public void set" + datos[i] + "(" + datos[i+1] + " " + datos[i] + "){");
            System.out.println("public void set" + datos[i] + "(" + datos[i+1] + " " + datos[i] + "){");
            out.println("this." + datos[i] + "=" + datos[i] + ";");
            System.out.println("this." + datos[i] + "=" + datos[i] + ";");
            out.println("}");
            System.out.println("}");
        }
        out.println(" public String toString() {");
        System.out.println(" public String toString() {");
        out.print("return ");
        System.out.print("return ");
        for(i = 3; i < datos.length; i += 2) {
            if(i==datos.length-2){
                out.print("\" "+datos[i]+": \"+this."+datos[i]);
                System.out.print("\" "+datos[i]+": \"+this."+datos[i]);
            }else{
                out.print(" \""+datos[i]+": \"+this."+datos[i]+" + ");
                System.out.print(" \""+datos[i]+": \"+this."+datos[i]+"+ \" , \" + ");
            }

        }
        out.print(";\n");
        System.out.print(";\n");
        out.println("}");
        System.out.println("}");

        out.println("}");
        System.out.println("}");
        out.close();
        Compilador.InMemoryJavaFileObject file = null;

        try {
            file = new Compilador.InMemoryJavaFileObject("/Manejador/" + nombre, writer.toString());
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return file;
    }
    public static Class<?> parseType(final String className) {
        switch (className) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "char":
                return char.class;
            case "void":
                return void.class;
            default:
                String fqn = className.contains(".") ? className : "java.lang.".concat(className);
                try {
                    return Class.forName(fqn);
                } catch (ClassNotFoundException ex) {
                    throw new IllegalArgumentException("Class not found: " + fqn);
                }
        }
    }


    public static void compile(Iterable<? extends JavaFileObject> files) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        Compilador.MyDiagnosticListener c = new Compilador.MyDiagnosticListener();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(c, Locale.ENGLISH, (Charset)null);
        Iterable options = Arrays.asList("-d", classOutputFolder);
        CompilationTask task = compiler.getTask((Writer)null, fileManager, c, options, (Iterable)null, files);
        Boolean result = task.call();
        if (result) {
            System.out.println("Succeeded");
        }

    }

    public Object realizarOperacion(String nombre, String[] metodosDatos,HashMap<String,Class<?>> tipos) {
        Object clase = new Object();
        try {
            Class<?> tClass = null;
            try {
                tClass = new URLClassLoader(new URL[] {new File(classOutputFolder).toURL()}).loadClass(nombre);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("clase cargada"+tClass.getName());
            clase = tClass.newInstance();
            System.out.println("Genero bien instancia " + clase.getClass().getName());
            String methodName = "";

            for(int i = 3; i < metodosDatos.length; i += 2) {
                methodName = "set" + metodosDatos[i];

                Method setNameMethod = clase.getClass().getMethod(methodName,tipos.get(metodosDatos[i]));
                Class <?> tipo=tipos.get(metodosDatos[i]);
                if(tipo.equals(String.class)){
                    setNameMethod.invoke(clase,metodosDatos[i+1]);
                }else{
                    if(tipo.equals(boolean.class)){
                        setNameMethod.invoke(clase,Boolean.parseBoolean(metodosDatos[i+1]));
                    }else if(tipo.equals(byte.class)){
                        setNameMethod.invoke(clase,Byte.parseByte(metodosDatos[i+1]));
                    } else if (tipo.equals(short.class)) {
                        setNameMethod.invoke(clase,Short.parseShort(metodosDatos[i+1]));
                    }else if(tipo.equals(int.class)){
                        setNameMethod.invoke(clase,Integer.parseInt(metodosDatos[i+1]));
                    }else if(tipo.equals(long.class)){
                        setNameMethod.invoke(clase, Long.parseLong(metodosDatos[i+1]));
                    }else if(tipo.equals(float.class)){
                        setNameMethod.invoke(clase, Float.parseFloat(metodosDatos[i+1]));
                    }else if (tipo.equals(double.class)){
                        setNameMethod.invoke(clase, Double.parseDouble(metodosDatos[i+1]));
                    }else if (tipo.equals(char.class)){
                        setNameMethod.invoke(clase, (metodosDatos[i+1]).charAt(0));
                    }
                }

                methodName = "get" + metodosDatos[i];
                Method getNameMethod = clase.getClass().getMethod(methodName);
            }
        } catch (MalformedURLException var17) {
            var17.printStackTrace();
        } catch (Exception var19) {
            var19.printStackTrace();
        }

        return clase;
    }

    public String realizarConsulta(String nombre, String[] metodosDatos,ArrayList<Object> objects) {
        StringBuilder response= new StringBuilder();
        try {
            String methodName = "";
            for (int j=1;j<objects.size();j++){
                response.append("| ");
                for(int i = 3; i < metodosDatos.length; i ++) {
                    methodName = "get" + metodosDatos[i];
                    response.append(metodosDatos[i]).append(": ");
                    Method getNameMethod = objects.get(j).getClass().getMethod(methodName);
                    response.append(getNameMethod.invoke(objects.get(j))).append(" ");
                }
                response.append(" |");

            }

        } catch (Exception var19) {
            var19.printStackTrace();
        }
            /*/
            for(int i = 3; i < metodosDatos.length; i += 2) {
                methodName = "get" + metodosDatos[i];
                Method getNameMethod = tabla.getClass().getMethod(methodName);
                String name = (String)getNameMethod.invoke(tabla);
                System.out.println("Valor devuelto por metodo:" + name);
                resultado = resultado + "," + name;
            }
            // */
        return response.toString();
    }

    public static void main(String[] args) throws Exception {
        String nombre = "tabla1";
        String[] datos = new String[]{"algo", "algo", "algo", "String", "Nombre", "String", "Apellido"};
        //JavaFileObject file = compilarObjeto(nombre, datos, basesDatos.get(nombreSchema).get(comandoLimpio[2]));
        //Iterable<? extends JavaFileObject> files = Arrays.asList(file);
        //compile(files);
        System.out.println("fin del programa..");
    }

    public static class InMemoryJavaFileObject extends SimpleJavaFileObject {
        public String contents = null;

        public InMemoryJavaFileObject(String className, String contents) throws Exception {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.contents = contents;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return this.contents;
        }
    }

    public static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject> {
        public MyDiagnosticListener() {
        }

        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            System.out.println("Line Number->" + diagnostic.getLineNumber());
            System.out.println("code->" + diagnostic.getCode());
            System.out.println("Message->" + diagnostic.getMessage(Locale.ENGLISH));
            System.out.println("Source->" + diagnostic.getSource());
            System.out.println(" ");
        }
    }
}