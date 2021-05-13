
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class Cliente {

    public static void main(String[] args) throws Exception {

        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("a. Alta usuario");
            System.out.println("b. Consulta usuario");
            System.out.println("c. Borra usuario");
            System.out.println("d. Salir");
            System.out.print("Opcion: ");

            char opc = br.readLine().charAt(0);

            switch (opc) {
                case 'a':
                    System.out.println("Alta usuario");
                    Usuario usuario = new Usuario();

                    System.out.print("Email: ");
                    usuario.email = br.readLine();

                    System.out.print("Nombre: ");
                    usuario.nombre = br.readLine();

                    System.out.print("Apellido Paterno: ");
                    usuario.apellido_paterno = br.readLine();

                    System.out.print("Apellido Materno: ");
                    usuario.apellido_materno = br.readLine();
                    
                    System.out.print("Fecha de nacimiento: ");
                    usuario.fecha_nacimiento = br.readLine();

                    System.out.print("Telefono: ");
                    usuario.telefono = br.readLine();

                    System.out.print("Genero (M/F): ");
                    usuario.genero = br.readLine();
                    alta_usuario(usuario);
                    break;
                case 'b':
                    System.out.println("Consulta usuario");
                    System.out.print("Ingresa el ID de usuario: ");
                    consultar_usuario(Integer.parseInt(br.readLine()));
                    break;
                case 'c':
                    System.out.println("Borrar usuario");
                    System.out.print("Ingresa el ID de usuario: ");
                    borrar_usuario(Integer.parseInt(br.readLine()));
                    break;
                case 'd':
                    br.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    public static void alta_usuario(Usuario usuario) throws IOException {
        URL url = new URL("http://localhost:8080/Servicio/rest/ws/alta_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // en este caso utilizamos el metodo POST de HTTP
        conexion.setRequestMethod("POST");

        // indica que la peticion estara codificada como URL
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // el metodo web "consulta_usuario" recibe como parametro el id de un usuario,
        // en este caso el id es 10
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        Gson gson = builder.create();
        String body = gson.toJson(usuario);

        String parametros = "usuario=" + URLEncoder.encode(body, "UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());

        os.flush();

        // se debe verificar si hubo error
        if (conexion.getResponseCode() == 200) { // no hubo error
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;
            // el metodo web regresa una string en formato JSON
            while ((respuesta = br.readLine()) != null)
                System.out.println("Se agrego el usuario con ID " + respuesta);
        } else { // hubo error
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta; // el metodo web regresa una instancia de la clase Error en formato JSON
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);
            // dispara una excepcion para terminar el programa
            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        }

        conexion.disconnect();

    }

    public static void consultar_usuario(int id_usuario) throws IOException {

        URL url = new URL("http://localhost:8080/Servicio/rest/ws/consulta_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // en este caso utilizamos el metodo POST de HTTP
        conexion.setRequestMethod("POST");

        // indica que la peticion estara codificada como URL
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // el metodo web "consulta_usuario" recibe como parametro el id de un usuario,
        // en este caso el id es 10
        String parametros = "id_usuario=" + URLEncoder.encode(String.valueOf(id_usuario), "UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());

        os.flush();

        // se debe verificar si hubo error
        if (conexion.getResponseCode() == 200) { // no hubo error
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;

            Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            while ((respuesta = br.readLine()) != null){
                Usuario user = (Usuario) j.fromJson(respuesta, Usuario.class);
                //System.out.println("ID: " + user.id_usuario);
                //System.out.println("Email: " + user.email);
                System.out.println("Nombre: " + user.nombre);
                System.out.println("Apellido Paterno: " + user.apellido_paterno);
                System.out.println("Apellido Materno: " + user.apellido_materno);
                System.out.println("Fecha: " + user.fecha_nacimiento);
                System.out.println("Genero: " + user.genero);
            }

            // el metodo web regresa una string en formato JSON
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);
        } else { // hubo error
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta; // el metodo web regresa una instancia de la clase Error en formato JSON
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);
            // dispara una excepcion para terminar el programa
            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        }

        conexion.disconnect();

    }

    public static void borrar_usuario(int id_usuario) throws IOException {

        URL url = new URL("http://localhost:8080/Servicio/rest/ws/borra_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        // en este caso utilizamos el metodo POST de HTTP
        conexion.setRequestMethod("POST");

        // indica que la peticion estara codificada como URL
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // el metodo web "consulta_usuario" recibe como parametro el id de un usuario,
        // en este caso el id es 10
        String parametros = "id_usuario=" + URLEncoder.encode(String.valueOf(id_usuario), "UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());

        os.flush();

        // se debe verificar si hubo error
        if (conexion.getResponseCode() == 200) { // no hubo error
            //BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            //String respuesta;

            // el metodo web regresa una string en formato JSON
            //while ((respuesta = br.readLine()) != null)
                //System.out.println(respuesta);
            System.out.println("El usuario ha sido borrado");
        } else { // hubo error
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta; // el metodo web regresa una instancia de la clase Error en formato JSON
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);
                // dispara una excepcion para terminar el programa
            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        }

        conexion.disconnect();

    }
}

class Usuario {
    int id_usuario;
    String email;
    String nombre;
    String apellido_paterno;
    String apellido_materno;
    String fecha_nacimiento;
    String telefono;
    String genero;
    byte[] foto;
}

