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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;

public class Compilador {
    private static String classOutputFolder = Compilador.class.getResource("build/classes").getPath();

    public Compilador() {
    }

    public static JavaFileObject compilarObjeto(String nombre, String[] datos) throws IOException {
        String parametros = "";
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("import java.io.*;");
        System.out.println("import java.io.*;");
        out.println("public class " + nombre + "  implements Serializable {");
        System.out.println("public class " + nombre + "  implements Serializable {");

        int i;
        for(i = 3; i < datos.length; i += 2) {
            out.println(datos[i+1] + " " + datos[i] + ";");
            System.out.println(datos[i+1] + " " + datos[i] + ";");
        }

        out.println("  public " + nombre + "() {");
        System.out.println("  public " + nombre + "() {");

        /*//
        for(i = 3; i < datos.length; i += 2) {
            out.println("this." + datos[i] + "=" + datos[i] + ";");
            System.out.println("this." + datos[i] + "=" + datos[i] + ";");
        }
        //*/

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
                out.print("\""+datos[i]+"\"+this."+datos[i]);
                System.out.print("\""+datos[i]+"\"+this."+datos[i]);
            }else{
                out.print("\""+datos[i]+"\"+this."+datos[i]+" + ");
                System.out.print("\""+datos[i]+"\"+this."+datos[i]+" + ");
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

    public Object realizarOperacion(String nombre, String[] metodosDatos) {
        File file = new File(classOutputFolder);
        Object tabla = new Object();

        try {
            URL url = file.toURL();
            URL[] urls = new URL[]{url};
            System.out.println("Dentro del metodo invoke..");
            ClassLoader loader = new URLClassLoader(urls);
            System.out.println("Crea cargador de clase");
            loader.loadClass(nombre);
            Class[] params = new Class[0];
            System.out.println("Cargo bien la clase");
            Class<?> tClass = Class.forName(nombre);
            tabla = tClass.newInstance();
            System.out.println("Genero bien instancia " + tabla.getClass().getName());
            String methodName = "";

            for(int i = 3; i < metodosDatos.length; i += 2) {
                methodName = "set" + metodosDatos[i];
                Method setNameMethod = tabla.getClass().getMethod(methodName, String.class);
                setNameMethod.invoke(tabla, metodosDatos[i + 1]);
                methodName = "get" + metodosDatos[i];
                Method getNameMethod = tabla.getClass().getMethod(methodName);
                String name = (String)getNameMethod.invoke(tabla);
                System.out.println("Valor devuelto por metodo:" + name);
            }
        } catch (MalformedURLException var17) {
            ;
        } catch (ClassNotFoundException var18) {
            ;
        } catch (Exception var19) {
            var19.printStackTrace();
        }

        return tabla;
    }

    public String realizarConsulta(String nombre, String[] metodosDatos) {
        File file = new File(classOutputFolder);
        String resultado = "";

        try {
            URL url = file.toURL();
            URL[] urls = new URL[]{url};
            System.out.println("Dentro del metodo invoke..");
            ClassLoader loader = new URLClassLoader(urls);
            System.out.println("Crea cargador de clase");
            loader.loadClass(nombre);
            Class[] params = new Class[0];
            System.out.println("Cargo bien la clase");
            Class<?> tClass = Class.forName(nombre);
            Object tabla = tClass.newInstance();
            System.out.println("Genero bien instancia " + tabla.getClass().getName());
            String methodName = "";

            for(int i = 3; i < metodosDatos.length; i += 2) {
                methodName = "get" + metodosDatos[i];
                Method getNameMethod = tabla.getClass().getMethod(methodName);
                String name = (String)getNameMethod.invoke(tabla);
                System.out.println("Valor devuelto por metodo:" + name);
                resultado = resultado + "," + name;
            }
        } catch (MalformedURLException var17) {
            ;
        } catch (ClassNotFoundException var18) {
            ;
        } catch (Exception var19) {
            var19.printStackTrace();
        }

        return resultado;
    }

    public static void main(String[] args) throws Exception {
        String nombre = "tabla1";
        String[] datos = new String[]{"algo", "algo", "algo", "String", "Nombre", "String", "Apellido"};
        JavaFileObject file = compilarObjeto(nombre, datos);
        Iterable<? extends JavaFileObject> files = Arrays.asList(file);
        compile(files);
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