package Manejador;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;


public class Compilador{
    /** where shall the compiled class be saved to (should exist already) */
    private static String classOutputFolder = "./build/classes";
    public static class MyDiagnosticListener implements DiagnosticListener<JavaFileObject>{
        public void report(Diagnostic<? extends JavaFileObject> diagnostic)
        {

            System.out.println("Line Number->" + diagnostic.getLineNumber());
            System.out.println("code->" + diagnostic.getCode());
            System.out.println("Message->"
                    + diagnostic.getMessage(Locale.ENGLISH));
            System.out.println("Source->" + diagnostic.getSource());
            System.out.println(" ");
        }
    }

    /** java File Object represents an in-memory java source file <br>
     * so there is no need to put the source file on hard disk  **/
    public static class InMemoryJavaFileObject extends SimpleJavaFileObject{
        private String contents = null;

        public InMemoryJavaFileObject(String className, String contents) throws Exception
        {
            super(URI.create("string:///" + className.replace('.', '/')
                    + Kind.SOURCE.extension), Kind.SOURCE);
            this.contents = contents;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors)
                throws IOException
        {
            return contents;
        }
    }

    /** Get a simple Java File Object ,<br>
     * It is just for demo, content of the source code is dynamic in real use case */
    public static JavaFileObject compilarObjeto(String nombre,String datos[])throws IOException{

        String parametros="";
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("import java.io.*;");
        out.println("public class "+nombre+"  implements Serializable {");

        for(int i=3;i<datos.length;i=i+2){
            out.println(datos[i+1]+" "+datos[i]+";");
        }
        out.println("  public "+nombre+"() {");

        for(int i=3;i<datos.length;i=i+2){
            out.println("this."+datos[i]+"="+datos[i]+";");
        }
        out.println("  }");

        for(int i=3;i<datos.length;i=i+2){
            out.println("public "+datos[i+1]+" get"+datos[i]+"(){");
            out.println("return "+datos[i]+";");
            out.println("}");

            out.println("public void set"+datos[i]+"("+datos[i+1]+" "+datos[i]+"){");
            out.println("this."+datos[i]+"="+datos[i]+";");
            out.println("}");
        }

        out.println("}");
        out.close();
        JavaFileObject file = null;
        try
        {
            file = new InMemoryJavaFileObject("/Manejador/"+nombre, writer.toString());
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return file;
    }

    /** compile your files by JavaCompiler */
    public static void compile(Iterable<? extends JavaFileObject> files){
        //get system compiler:
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // for compilation diagnostic message processing on compilation WARNING/ERROR
        MyDiagnosticListener c = new MyDiagnosticListener();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(c,Locale.ENGLISH,null);
        //specify classes output folder
        Iterable options = Arrays.asList("-d", classOutputFolder);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager,c, options, null,files);
        Boolean result = task.call();
        if (result == true)
        {
            System.out.println("Succeeded");
        }
    }

    /** run class from the compiled byte code file by URLClassloader */
    public static void realizarOperacion(String nombre,String []metodosDatos){
        // Create a File object on the root of the directory
        // containing the class file
        File file = new File(classOutputFolder);

        try
        {
            // Convert File to a URL
            URL url = file.toURL(); // file:/classes/demo
            URL[] urls = new URL[] { url };
            System.out.println("Dentro del metodo invoke..");
            // Create a new class loader with the directory
            ClassLoader loader = new URLClassLoader(urls);
            System.out.println("Crea cargador de clase");
            // Load in the class; Class.childclass should be located in
            // the directory file:/class/demo/
            Class thisClass = loader.loadClass(nombre);
            Class params[] = {};
            System.out.println("Cargo bien la clase");

            String ClassName = nombre;
            Class<?> tClass = Class.forName(ClassName); // convert string classname to class
            Object tabla = tClass.newInstance(); // invoke empty constructor
            System.out.println("Genero bien instancia "+tabla.getClass().getName());
            String methodName = "";

            // with single parameter, return void
            methodName = "setNombre";
            Method setNameMethod = tabla.getClass().getMethod(methodName, String.class);
            setNameMethod.invoke(tabla, "Juena"); // pass arg

            // without parameters, return string
            methodName = "getNombre";
            Method getNameMethod = tabla.getClass().getMethod(methodName);
            String name = (String) getNameMethod.invoke(tabla); // explicit cast
            System.out.println("Valor devuelto por metodo:"+name);

        }
        catch (MalformedURLException e)
        {
        }
        catch (ClassNotFoundException e)
        {
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /*
    public static void realizarOperacion(String nombre){
        // Create a File object on the root of the directory
        // containing the class file
        File file = new File(classOutputFolder);

        try
        {
            // Convert File to a URL
            URL url = file.toURL(); // file:/classes/demo
            URL[] urls = new URL[] { url };
            System.out.println("Dentro del metodo invoke..");
            // Create a new class loader with the directory
            ClassLoader loader = new URLClassLoader(urls);
            System.out.println("Crea cargador de clase");
            // Load in the class; Class.childclass should be located in
            // the directory file:/class/demo/
            Class thisClass = loader.loadClass(nombre);
            Class params[] = {};
            System.out.println("Cargo bien la clase");
            //Object paramsObj[] = {};
            /*Object o = (Object)new String("Juancho");
            Object[] param = new Object[]{o};
            Object[] paramsObj = new Object[]{param};
            Object instance = thisClass.newInstance();
            Method thisMethod = thisClass.getDeclaredMethod("setNombre", params);
            */

            /*
            String ClassName = nombre;
            Class<?> tClass = Class.forName(ClassName); // convert string classname to class
            Object tabla = tClass.newInstance(); // invoke empty constructor
            System.out.println("Genero bien instancia "+tabla.getClass().getName());
            String methodName = "";

            // with single parameter, return void
            methodName = "setNombre";
            Method setNameMethod = tabla.getClass().getMethod(methodName, String.class);
            setNameMethod.invoke(tabla, "Juena"); // pass arg

            // without parameters, return string
            methodName = "getNombre";
            Method getNameMethod = tabla.getClass().getMethod(methodName);
            String name = (String) getNameMethod.invoke(tabla); // explicit cast
            System.out.println("Valor devuelto por metodo:"+name);
//            String p = "Juancho";
//            Method thisMethod = thisClass.getDeclaredMethod("setNombre", params)
            /*
            // run the testAdd() method on the instance:
//            thisMethod.invoke(instance, paramsObj);
//            Method m1 = thisClass.getMethod("getNombre", null);
//            Object ob = m1.invoke(instance, null);
//            System.out.println("Dato devuelto: "+ob);
        }
        catch (MalformedURLException e)
        {
        }
        catch (ClassNotFoundException e)
        {
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    */

    public static void main(String[] args) throws Exception
    {
        //1.Construct an in-memory java source file from your dynamic code
        String nombre="tabla1";
        String datos[]={"algo","algo","algo","String","Nombre","String","Apellido"};
        JavaFileObject file = compilarObjeto(nombre,datos);
        Iterable<? extends JavaFileObject> files = Arrays.asList(file);

        //2.Compile your files by JavaCompiler
        compile(files);

        //3.Load your class by URLClassLoader, then instantiate the instance, and call method by reflection
        //runIt(nombre);
        System.out.println("fin del programa..");
    }
}