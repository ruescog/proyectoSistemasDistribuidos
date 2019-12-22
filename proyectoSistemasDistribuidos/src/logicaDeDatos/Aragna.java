/* Esta componente realiza la siguiente tarea: para cada URL observada, busca otras URLs (que no contengan el formato de búsqueda) a fin de crear una red de URLs para descargar
 * las imágenes de todas ellas, hasta llegar a un nivel máximo de iteraciones marcado por el usuario (en la forma automática).
 */

package logicaDeDatos;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Aragna {

	private Deque<String> red; // estructura cola para ver las nuevas URLs
	private List<String> inspeccionadas; // URLs que han sido vistas
	private int iteraciones; // numero máximo de iteraciones

	public Aragna(int numIteraciones) {

		this.iteraciones = numIteraciones;
		this.red = new ArrayDeque<>();
		this.inspeccionadas = new ArrayList<>();
	}

	public Aragna(String url, int numIteraciones) {

		this(numIteraciones);
		this.red.addFirst(url);
	}

	public int getIteraciones() {

		return this.iteraciones;
	}

	public synchronized void enredar(File fichero, String[] formatos) {
		// interesa que el método sea síncrono para que no puedan añadirse dos redes a
		// la vez, dado que podría violar la seguridad explicada más abajo.
		// NOTA: la parte síncrona podría acotarse más, pero es un método bastante
		// rápido en el que no tiene un peso relevante.
		String linea;
		boolean incluir;

		try (DataInputStream dis = new DataInputStream(new FileInputStream(fichero))) {

			while ((linea = dis.readLine()) != null) {
				incluir = true;
				// si la URL contiene parte de un formato buscado (.mp4, .png ...) no interesa
				// almacenarla porque estará en el otro fichero
				for (String formato : formatos) {
					if (linea.contains(formato)) {
						incluir = false;
					}
				}
				// si cumple lo anterior, no está en la lista de "buscadas" ni en la de "por
				// buscar", se añade.
				if (incluir && !this.inspeccionadas.contains(linea) && !this.red.contains(linea)) {
					linea = linea.substring(linea.indexOf("http"));
					this.red.addLast(linea);
				}
			}

			System.out.println("Las URLs han sido enredadas.");

		} catch (Exception e) {
			System.err.println("Error al añadir una URL a la araña.");
		}
	}

	public synchronized String desenredar() {
		// Al igual que el anterior, interesa que sea sincrono para que no haya
		// problemas al obtener las URLs.

		String url = null;

		// Devuelve una String que representa una URL de la red.
		if (this.red.isEmpty()) {
			System.out.println("***************************");
			System.out.println("La red está vacía.");
			System.out.println("***************************");
		} else if (this.iteraciones != 0) {
			url = this.red.getFirst();
			this.inspeccionadas.add(url);
			this.red.removeFirst();
			this.iteraciones--;
		}

		return url;
	}
}
