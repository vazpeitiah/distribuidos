/*
  Error.java
  Permite regresar al cliente REST un mensaje de error
  Carlos Pineda Guerrero 2021
*/

package negocio;

public class Error
{
	String message;

	Error(String message)
	{
		this.message = message;
	}
}
