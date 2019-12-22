/* Dada una URL, esta componente del proyecto se encarga de obtener su código fuente para tratarlo posteriormente.
 */

package logicaDelPrograma;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.UnknownHostException;

public class Lector {

	private URL url;

	public Lector(URL url) {
		// cada componente lector esta compuesta por la url que debe leer

		this.url = url;
	}

	public File leer() {
		// genera un fichero intermedio de lectura, en el que guardará, íntegra (posible
		// reutilizacion) el codigo fuente al que tenga acceso de la url con la que se
		// creó

		File f = new File("lector.txt");
		String linea;

		try (DataInputStream dis = new DataInputStream(this.url.openStream());
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));) {

			// lee linea por linea (posiblemente no sean "lineas" como el usuario cree que
			// es una linea) y almacena la información sin modificar en el fichero
			// lector.txt
			while ((linea = dis.readLine()) != null) {
				bw.write(linea + "\r\n");
			}

			// mensaje de exito
			System.out.println("La web " + this.url.toString() + " ha sido leida. El resultado ha sido almacenado en " + f.getName());

		} catch (UnknownHostException e) {

			// mensaje de error en caso de que la url no sea encontrada (no exista)
			System.err.println("El host indicado no ha podido ser encontrado.");

		} catch (IOException e) {

			// mensaje de error en caso de que haya un problema con la apertura de los
			// ficheros o del url.openstream.
			System.err.println("Ha ocurrido un error inesperado con la apertura de ficheros.");

		} finally {

			return f;
		}
	}
}
