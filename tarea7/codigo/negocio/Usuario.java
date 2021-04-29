/*
  Carlos Pineda Guerrero 2021
*/

package negocio;

import com.google.gson.*;

public class Usuario
{
  String email;
  String nombre;
  String apellido_paterno;
  String apellido_materno;
  String fecha_nacimiento;
  String telefono;
  String genero;
  byte[] foto;

  // @FormParam necesita un metodo que convierta una String al objeto de tipo Usuario
  public static Usuario valueOf(String s) throws Exception
  {
    Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
    return (Usuario)j.fromJson(s,Usuario.class);
  }
}
