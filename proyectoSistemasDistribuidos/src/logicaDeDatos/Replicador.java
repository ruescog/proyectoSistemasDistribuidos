/* Esta componente se encarga de bloquear una descarga si ya se ha descagado el numero maximo (determinado por el usuario) de archivos con ese
 * nombre.
 */

package logicaDeDatos;

import java.util.HashMap;
import java.util.Map;

public class Replicador {

	private static Map<String, Integer> bd;
	private static int MAXREPLICAS;

	public Replicador(int maxreplicas) {

		bd = new HashMap<>();
		MAXREPLICAS = maxreplicas;
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
