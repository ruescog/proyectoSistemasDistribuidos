/* Esta componente se encarga de bloquear una descarga si ya se ha descagado el numero maximo (determinado por el usuario) de archivos con ese
 * nombre.
 */

package logicaDeDatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import logicaDelPrograma.Principal;

public class Replicador {

	private static Map<String, Integer> bd;
	private static int MAXREPLICAS;

	public Replicador(int maxreplicas) {

		bd = new HashMap<>();
		MAXREPLICAS = maxreplicas;
	}

	public Replicador(int maxreplicas, String ruta) {
		// Genera entradas en la base de datos según lo encontrado en el fichero de la
		// ruta (lista negra)

		this(maxreplicas);
		
		File listaNegra = new File(ruta);
		String linea;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(listaNegra)))) {

			// Para cada linea se guarda una entrada en la BD ...
			while ((linea = br.readLine()) != null) {
				bd.put(linea, MAXREPLICAS);
			}

			// mensaje de exito por consola si se ha realizado con éxito.
			System.out.println("Se ha tenido en cuenta la lista negra");

		} catch (IOException e) {
			// mensaje de error en caso de que haya un problema con la apertura de los
			// ficheros
			System.err.println("Ha ocurrido un error inesperado con la apertura de ficheros.");
		}

	}

	public static int añadirDato(String linea) {
		// añade un dato al sistema. en caso de que ya existiera, aumenta en uno su
		// multiplicidad. en cualquier caso, devuelve un int que indica el numero de
		// copia del actual elemento - 1

		Integer multiplicidad;
		int copia;

		// en caso de que exista ya el dato
		if (bd.containsKey(linea)) {
			multiplicidad = bd.get(linea);

			if (multiplicidad == MAXREPLICAS)
				return -1;

			copia = multiplicidad.intValue() + 1;
			bd.put(linea, new Integer(copia));

			return multiplicidad;

		} else {
			bd.put(linea, new Integer(1));
			return 0; // indicador de dato nuevo
		}
	}
}
