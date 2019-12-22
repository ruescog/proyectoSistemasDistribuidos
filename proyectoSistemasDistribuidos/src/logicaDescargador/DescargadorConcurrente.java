package logicaDescargador;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import logicaDeDatos.Replicador;

public class DescargadorConcurrente implements Runnable {

	private String imgUrl;
	private Descargador padre;

	public DescargadorConcurrente(Descargador padre, String imgUrl) {

		this.imgUrl = imgUrl;
	}

	public void run() {

		URL url;
		String file, formato;
		int indiceBarra, indiceInterrogante, multiplicidad, leidos;
		byte[] buff = new byte[1024 * 32];

		try {
			url = new URL(this.imgUrl);

			// CREA LOS OBJETOS NECESARIOS PARA LEER DE LA WEB Y GUARDARLO EN LOCAL, NOMBRA
			// AL OBJETO CON EL NOMBRE QUE TENGA EN LA WEB
			file = url.getFile();

			// DETERMINA DONDE ESTAR� EL NOMBRE DEL FICHERO

			// +1 PORQUE NO NOS INTERESA LA BARRA ... NOTAR QUE EN CASO DE QUE SEA 0 (NO
			// HAYA BARRA), ESTO INDICAR� AL PRINCIPIO DE LA CADENA
			indiceBarra = file.lastIndexOf("/") + 1;

			// LA MAYOR�A DE LOS ARCHIVOS TENDR�N INFORMACI�N ADICIONAL SEPARADA POR EL ?,
			// NO NOS INTERESA AQUI.
			indiceInterrogante = file.indexOf("?");

			// ESTE INDICE NO SE "ARREGLA SOLO" COMO EL DE /, AS� QUE SI NO HAY INTERROGANTE
			// TENEMOS QUE INDICAR QUE TOMAR� COMO NOMBRE TODA LA CADENA
			if (indiceInterrogante == -1) {
				indiceInterrogante = file.length();
			}

			file = file.substring(indiceBarra, indiceInterrogante); // EL NOMBRE DE FICHERO (QUE SE GENERAR�)
			multiplicidad = Replicador.a�adirDato(file);

			if (multiplicidad == -1) {
				throw new MultiplicityExceededException("Multiplicidad superada: " + this.imgUrl);
			}

			if (multiplicidad != 0) { // SI EST� REPETIDO ...
				formato = file.substring(file.lastIndexOf("."));
				file = file.substring(0, file.lastIndexOf("."));
				file = file.concat(" (" + multiplicidad + ")");
				file = file.concat(formato);
			}

			try (InputStream in = new BufferedInputStream(url.openStream());
					FileOutputStream fos = new FileOutputStream(file);) {

				// DESCARGA TODA LA INFORMACI�N DE LA URL QUE INDICABA LA LINEA DEL FICHERO
				while ((leidos = in.read(buff)) > 0) {
					fos.write(buff, 0, leidos);
				}

				fos.flush(); // DE NUEVO, UN FLUSH REDUNDANTE POR EL TRY WITH RESOURCES

				padre.archivosDescargados++;
			} catch (IOException e) {

				// MENSAJE DE ERROR EN CASO DE QUE HAYA UN PROBLEMA CON LA APERTURA DE LOS
				// FICHEROS O DEL URL.OPENSTREAM.
				System.err.println("Ha ocurrido un error inesperado con la apertura del fichero " + file + ".");
			} catch (IllegalArgumentException e) {

				System.err.println("El protocolo o el host son desconocidos.");
			} finally {
				padre.urlInspeccionadas++;
			}
		} catch (MalformedURLException mue) {

			// MENSAJE DE ERROR EN CASO DE QUE HAYA UN PROBLEMA CON LA APERTURA DE LOS
			// FICHEROS O DEL URL.OPENSTREAM.
			System.err.println("Ha ocurrido un error inesperado con la URL.");
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("La direcci�n no ha podido ser identificada.");
		} catch (MultiplicityExceededException e) {
			System.out.println(e.getMessage());
		}
	}
}
