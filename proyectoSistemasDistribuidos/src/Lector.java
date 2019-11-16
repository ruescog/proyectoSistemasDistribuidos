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
		// CADA COMPONENTE LECTOR ESTA COMPUESTA POR LA URL QUE DEBE LEER

		this.url = url;
	}

	public File leer() {
		// GENERA UN FICHERO INTERMEDIO DE LECTURA, EN EL QUE GUARDARÁ, ÍNTEGRA (POSIBLE
		// REUTILIZACION) EL CODIGO FUENTE AL QUE TENGA ACCESO DE LA URL CON LA QUE SE
		// CREÓ

		File f = new File("lector.txt");
		String linea;

		try (DataInputStream dis = new DataInputStream(this.url.openStream());
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));) {

			// LEE LINEA POR LINEA (POSIBLEMENTE NO SEAN "LINEAS" COMO EL USUARIO CREE QUE
			// ES UNA LINEA) Y ALMACENA LA INFORMACIÓN SIN MODIFICAR EN EL FICHERO
			// lector.txt
			while ((linea = dis.readLine()) != null) {
				bw.write(linea + "\r\n");
			}

			// MENSAJE DE EXITO
			System.out.println("La web ha sido leida. El resultado ha sido almacenado en " + f.getName());

		} catch (UnknownHostException e) {

			// MENSAJE DE ERROR EN CASO DE QUE LA URL NO SEA ENCONTRADA (NO EXISTA)
			System.err.println("El host indicado no ha podido ser encontrado.");

		} catch (IOException e) {

			// MENSAJE DE ERROR EN CASO DE QUE HAYA UN PROBLEMA CON LA APERTURA DE LOS
			// FICHEROS O DEL URL.OPENSTREAM.
			System.err.println("Ha ocurrido un error inesperado.");

		} finally {

			return f;
		}
	}
}
