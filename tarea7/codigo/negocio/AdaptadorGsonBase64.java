/*
  AdaptadorGsonBase64.java
  Adaptador GSON para serializar/deserializar byte[] como base 64
  Ver: https://sites.google.com/site/gson/gson-user-guide
  Carlos Pineda Guerrero 2021
*/

package negocio;

import java.lang.reflect.Type;
import java.util.Base64;
import com.google.gson.*;

public class AdaptadorGsonBase64 implements JsonSerializer<byte[]>,JsonDeserializer<byte[]>
{
	public JsonElement serialize(byte[] src,Type typeOfSrc, JsonSerializationContext context)
	{
		return new JsonPrimitive(Base64.getEncoder().encodeToString(src));
	}

	public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	{
		// jax-rs reemplaza cada "+" por " ", pero el decodificador Base64 no reconoce " "
		String s = json.getAsString().replaceAll("\\ ", "+");
		return Base64.getDecoder().decode(s);
	}
}
