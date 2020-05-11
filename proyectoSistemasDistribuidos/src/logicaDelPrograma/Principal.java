/* Esta parte es el contacto del programa con el usuario.
 */

package logicaDelPrograma;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import logicaDeDatos.Aragna;
import logicaDeDatos.Replicador;
import logicaDescargador.Descargador;

public class Principal {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		String peticion, url, formatos[];
		int numIteraciones, replicas;
		Aragna aragna;

		// solicita un modo de uso
		if (args.length == 0) {
			System.out.print("¿Desea trabajar de forma manual, manual por defecto o automática? [m/mf/a]: ");
			peticion = in.nextLine().toUpperCase();

			if (peticion.equals("M")) {
				descargaManual();
			} else if (peticion.equals("MF")) {
				descargaManualDefecto();
			} else {
				System.out.println("Primera url y cantidad de iteraciones: ");

				System.out.print("	Primera url: ");
				url = in.nextLine();

				System.out.print("	Introducir el formato de los archivos que se quiere extraer: ");
				peticion = in.nextLine();

				System.out.print("	Número de iteraciones: ");
				numIteraciones = in.nextInt();

				System.out.print("	Número máximo de replicas de un mismo archivo: ");
				replicas = in.nextInt();

				formatos = peticion.split(",");
				aragna = new Aragna(url, numIteraciones);
				descargaAutomatica(aragna, formatos, replicas);
			}
		}

		in.close();
	}

	public static void descargaManual() {

		// variables necesarias
		Scanner in = new Scanner(System.in);
		File ficheroIntermedio;
		String peticion, formatos[];

		// componentes
		Lector lector;
		Buscador buscador;
		Descargador descargador;
		Replicador replicador = new Replicador(10);

		// codigo
		System.out.print("Introducir el nombre y formato de un archivo o una dirección URL: ");
		peticion = in.nextLine();

		ficheroIntermedio = new File(peticion);

		if (ficheroIntermedio.isFile()) {
			ficheroIntermedio = new File(peticion);
		} else {
			try {
				// si es una url, intenta descargar su codigo fuente, para utilizarlo despues
				lector = new Lector(new URL(peticion));
				ficheroIntermedio = lector.leer();

			} catch (MalformedURLException e) {
				// en caso de que el usuario no introduzca una url, lanzará de nuevo el programa
				// (forma de tratar el error malformed)
				System.out.println("La cadena introducida no es un archivo ni una URL.");
				System.out.print("¿Intentar de nuevo? [y/n] ");
				peticion = in.nextLine();

				// si el usuario no quiere intentar de nuevo, acaba la ejecucion del programa,
				// si quiere continuar, llama de nuevo al main sin argumentos
				if (peticion.toLowerCase().equals("y")) {
					descargaManual();
				}
				in.close();
				return;
			}
		}

		// buscamos la información de la web que deseamos obtener (imagenes, videos ...)
		System.out.print("Introducir el formato de los archivos que se quiere extraer de " + peticion + ": ");
		peticion = in.nextLine();

		// separa los formatos introducidos por el usuario por comas, busca en el
		// fichero generado por la componente anterior aquellas filas que contengan al
		// menos un formato que el usuario desee (es un filtrado)
		formatos = peticion.split(",");
		buscador = new Buscador(ficheroIntermedio);
		ficheroIntermedio = buscador.buscar(formatos);

		// decarga de los archivos que hayan quedado en el fichero producido por la
		// componente anterior. notar que si el usuario filtro un formato, habrá
		// descargas, si no, esta opcion no vale para nada, solo retrasa al programa
		// (posible correccion: solicitud por teclado de descagar)
		System.out.print("¿Desea descargar las líneas filtradas? [y/n]: ");
		peticion = in.nextLine().toUpperCase();

		if (peticion.equals("Y")) {
			descargador = new Descargador(ficheroIntermedio);
			descargador.descargar();
		}

		in.close();
	}

	public static void descargaManualDefecto() {

		// Extension del metodo anterior para automatizar la descarga manual

		// variables necesarias
		Scanner in = new Scanner(System.in);
		File ficheroIntermedio;
		String formatos[];

		// componentes
		Lector lector;
		Buscador buscador;
		Descargador descargador;
		Replicador replicador = new Replicador(1, "listaNegra.txt");

		// codigo
		ficheroIntermedio = new File("lector.txt");

		formatos = new String[2];
		formatos[0] = ".png";
		formatos[1] = ".jpg";
		buscador = new Buscador(ficheroIntermedio);
		ficheroIntermedio = buscador.buscar(formatos);

		descargador = new Descargador(ficheroIntermedio);
		descargador.descargar();

		ficheroIntermedio = new File("lector.txt");
		ficheroIntermedio.delete();
		ficheroIntermedio = new File("buscador.txt");
		ficheroIntermedio.delete();
		
		in.close();
	}

	public static void descargaAutomatica(Aragna aragna, String formatos[], int replicas) {

		// variables
		String url;
		File ficheroIntermedio;
		File ficheroLector;

		// componentes
		Lector lector;
		Buscador buscador;
		Descargador descargador;
		Replicador replicador = new Replicador(replicas);

		// codigo

		while ((url = aragna.desenredar()) != null) {

			System.out.println("***************************");
			System.out.println("Iteraciones restantes: " + aragna.getIteraciones());
			System.out.println("***************************");

			try {
				// lector
				lector = new Lector(new URL(url));
				ficheroLector = lector.leer();
				ficheroIntermedio = new File(ficheroLector.getAbsolutePath());

				// buscador
				buscador = new Buscador(ficheroIntermedio);
				ficheroIntermedio = buscador.buscar(formatos);

				// descargador
				descargador = new Descargador(ficheroIntermedio);
				descargador.descargar();

				// enredar
				buscador = new Buscador(ficheroLector);
				ficheroLector = buscador.buscar("http");
				aragna.enredar(ficheroLector, formatos);

			} catch (MalformedURLException e) {
				System.err.println("Error con la URL " + url);
			}
		}
	}

	public static boolean checkFormat(String linea, String[] formatos) {
		// CHECKEA QUE LA LINEA CONTIENE ALGUNO DE LOS FORMATOS (FILTROS) DE LA CADENA
		// DE STRINGS FORMATOS.

		for (String formato : formatos) {
			if (linea.contains(formato)) {
				return true;
			}
		}

		return false;
	}
}
