package logicaDelPrograma;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Descargador {

	private File fichero;

	public Descargador(File fichero) {
		// CADA COMPONENTE DESCARGADOR ESTÁ COMPUESTA POR EL FICHERO DEL CUAL VA A
		// DESCARGAR SUS LINEAS

		this.fichero = fichero;
	}

	public void descargar() {
		// DESCARGA TODOS LOS ARCHIVOS QUE PUEDA DE LOS CUALES CONTIENE LA INFORMACION
		// EN EL FICHERO QUE LO DEFINE

		String img, file;
		URL url;
		byte[] buff = new byte[1024 * 32];
		int leidos, indiceBarra, indiceInterrogante;

		try (DataInputStream fis = new DataInputStream(new FileInputStream(this.fichero));) {

			// PARA CADA LINEA DEL FICHERO ...
			while ((img = fis.readLine()) != null) {

				// SUPONE QUE ES UNA URL A UN ARCHIVO QUE ESTÁ EN LA NUBE
				url = new URL(img);

				// CREA LOS OBJETOS NECESARIOS PARA LEER DE LA WEB Y GUARDARLO EN LOCAL, NOMBRA
				// AL OBJETO CON EL NOMBRE QUE TENGA EN LA WEB
				file = url.getFile();

				// DETERMINA DONDE ESTARÁ EL NOMBRE DEL FICHERO

				indiceBarra = file.lastIndexOf("/") + 1; // +1 PORQUE NO NOS INTERESA LA BARRA ... NOTAR QUE EN CASO DE
															// QUE SEA 0 (NO HAYA BARRA), ESTO INDICARÁ AL PRINCIPIO DE
															// LA CADENA
				indiceInterrogante = file.indexOf("?"); // LA MAYORÍA DE LOS ARCHIVOS TENDRÁN INFORMACIÓN ADICIONAL
														// SEPARADA POR EL ?, NO NOS INTERESA AQUI.

				if (indiceInterrogante == -1) {
					indiceInterrogante = file.length(); // ESTE INDICE NO SE "ARREGLA SOLO" COMO EL DE /, ASÍ QUE SI NO
														// HAY INTERROGANTE TENEMOS QUE INDICAR QUE TOMARÁ COMO NOMBRE
														// TODA LA CADENA
				}

				try (InputStream in = new BufferedInputStream(url.openStream());
						FileOutputStream fos = new FileOutputStream(file.substring(indiceBarra, indiceInterrogante));) {

					// DESCARGA TODA LA INFORMACIÓN DE LA URL QUE INDICABA LA LINEA DEL FICHERO
					while ((leidos = in.read(buff)) > 0) {
						fos.write(buff, 0, leidos);
					}

					fos.flush(); // DE NUEVO, UN FLUSH REDUNDANTE POR EL TRY WITH RESOURCES

				} catch (IOException e) {

					// MENSAJE DE ERROR EN CASO DE QUE HAYA UN PROBLEMA CON LA APERTURA DE LOS
					// FICHEROS O DEL URL.OPENSTREAM.
					System.err.println("Ha ocurrido un error inesperado.");
				}
			}

			// MENSAJE DE ÉXTIO
			System.out.println("Descarga de imagenes terminada.");

		} catch (MalformedURLException e) {
			// MENSAJE DE ERROR EN CASO DE QUE LA URL NO CUMPLA CON LA SINTAXIS (NO EXISTA)
			System.err.println("La URL especificada no ha podido ser encontrada.");

		} catch (IOException ex1) {

			// MENSAJE DE ERROR EN CASO DE QUE HAYA UN PROBLEMA CON LA APERTURA DE LOS
			// FICHEROS O DEL URL.OPENSTREAM.
			System.err.println("Ha ocurrido un error inesperado.");
		}
	}
}
