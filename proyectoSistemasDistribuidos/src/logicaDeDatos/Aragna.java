package logicaDeDatos;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Aragna {

	private Deque<String> red;
	private List<String> inspeccionadas;
	private int iteraciones;

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

	public boolean enredar(String url) {

		if (!(this.red.contains(url) || this.inspeccionadas.contains(url))) {
			this.red.addLast(url);
			return true;
		} else {
			return false;
		}
	}

	public void enredar(File fichero, String[] formatos) {

		String linea;
		boolean incluir;

		try (DataInputStream dis = new DataInputStream(new FileInputStream(fichero))) {

			while ((linea = dis.readLine()) != null) {
				incluir = true;
				for (String formato : formatos) {
					if (linea.contains(formato)) {
						incluir = false;
					}
				}
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

	public String desenredar() {

		String url = null;

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
