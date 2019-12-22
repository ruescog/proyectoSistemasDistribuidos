/* Esta componente se encarga de buscar, en un archivo determinado (que genera otra componente) los formatos introducidos por el usuario. Notar que
 * "formato" no tiene por qué ser ".*", puede ser también una secuencia de palabras (que después dará fallo en la descarga si está en modo automático).
 */

package logicaDelPrograma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Buscador {

	private File fichero;

	public Buscador(File fichero) {
		// cada componente buscador está compuesta por el fichero que va a filtrar

		this.fichero = fichero;
	}

	public File buscar(String formato) {

		String[] formatos = new String[1];
		formatos[0] = formato;
		return this.buscar(formatos);
	}

	public File buscar(String[] formatos) {
		// filtra el archivo que lo identifica por los formatos introducidos como
		// parametros, crea un nuevo documento intermedio, buscador.txt, que contiene la
		// informacion filtrada, que puede ser reutilizable

		File f = new File("buscador.txt");
		String linea;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fichero)));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));) {

			while ((linea = br.readLine()) != null) {
				// parte dos veces la linea: una por " y otra por ', que son distintas formas de
				// separar informacion en html
				for (String trozo : linea.split("\"")) {
					for (String subTrozo : trozo.split("'")) {

						// si el resultado cumple alguno de los formatos indicados, lo almacena
						if (Principal.checkFormat(subTrozo, formatos)) {

							// ultima modificacion: por codificación de las web puede tratar el & por su
							// codigo unicode (\u0026), por lo que el buscador deberá volver a cambiar esa
							// codificacion por el caracter al que representa.
							bw.write(subTrozo.replace("\\u0026", "&") + "\r\n");
						}
					}
				}
			}
			bw.flush(); // innecesario por el try with resources, mejor dicho, redundante.

			// mensaje de exito por consola si se ha realizado con éxito.
			System.out.println("Se ha filtrado la petición y se han guardado las coincidencias en " + f.getName());

		} catch (IOException e) {

			// mensaje de error en caso de que haya un problema con la apertura de los
			// ficheros o del url.openstream.
			System.err.println("Ha ocurrido un error inesperado con la apertura de ficheros.");
		}

		return f;
	}
}
