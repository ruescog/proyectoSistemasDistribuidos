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
		// AÑADE UN DATO AL SISTEMA. EN CASO DE QUE YA EXISTIERA, AUMENTA EN UNO SU
		// MULTIPLICIDAD. EN CUALQUIER CASO, DEVUELVE UN INT QUE INDICA EL NUMERO DE
		// COPIA DEL ACTUAL ELEMENTO - 1

		Integer multiplicidad;
		int copia;

		// EN CASO DE QUE EXISTA YA EL DATO
		if (bd.containsKey(linea)) {
			multiplicidad = bd.get(linea);

			if (multiplicidad == MAXREPLICAS)
				return -1;

			copia = multiplicidad.intValue() + 1;
			bd.put(linea, new Integer(copia));

			return multiplicidad;

		} else {
			bd.put(linea, new Integer(1));
			return 0; // INDICADOR DE DATO NUEVO
		}
	}
}
