package logicaDelPrograma;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import logicaDeDatos.Aragna;
import logicaDeDatos.Replicador;

public class Principal {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);

		String peticion, url, formatos[];
		int numIteraciones;
		Aragna aragna;

		// SOLICITA UN MODO DE USO
		if (args.length == 0) {
			System.out.print("¿Desea trabajar de forma manual o automática? [m/a]: ");
			peticion = in.nextLine().toUpperCase();

			if (peticion.equals("M")) {
				descargaManual();
			} else {
				System.out.println("Primera url y cantidad de iteraciones: ");

				System.out.print("	Primera url: ");
				url = in.nextLine();

				System.out.print("	Introducir el formato de los archivos que se quiere extraer: ");
				peticion = in.nextLine();
				
				System.out.print("	Número de iteraciones: ");
				numIteraciones = in.nextInt();

				formatos = peticion.split(",");
				aragna = new Aragna(url, numIteraciones);
				descargaAutomatica(aragna, formatos);
			}
		}

		in.close();
	}

	public static void descargaManual() {

		// VARIABLES NECESARIAS
		Scanner in = new Scanner(System.in);
		File ficheroIntermedio;
		String peticion, formatos[];

		// COMPONENTES
		Lector lector;
		Buscador buscador;
		Descargador descargador;
		Replicador replicador = new Replicador();

		// CODIGO
		System.out.print("Introducir el nombre y formato de un archivo o una dirección URL: ");
		peticion = in.nextLine();

		ficheroIntermedio = new File(peticion);

		if (ficheroIntermedio.isFile()) {
			ficheroIntermedio = new File(peticion);
		} else {
			try {
				// SI ES UNA URL, INTENTA DESCARGAR SU CODIGO FUENTE, PARA UTILIZARLO DESPUES
				lector = new Lector(new URL(peticion));
				ficheroIntermedio = lector.leer();

			} catch (MalformedURLException e) {
				// EN CASO DE QUE EL USUARIO NO INTRODUZCA UNA URL, LANZARÁ DE NUEVO EL PROGRAMA
				// (FORMA DE TRATAR EL ERROR MALFORMED)
				System.out.println("La cadena introducida no es un archivo ni una URL.");
				System.out.print("¿Intentar de nuevo? [y/n] ");
				peticion = in.nextLine();

				// SI EL USUARIO NO QUIERE INTENTAR DE NUEVO, ACABA LA EJECUCION DEL PROGRAMA,
				// SI QUIERE CONTINUAR, LLAMA DE NUEVO AL MAIN SIN ARGUMENTOS
				if (peticion.toLowerCase().equals("y")) {
					descargaManual();
				}
				in.close();
				return;
			}
		}

		// BUSCAMOS LA INFORMACIÓN DE LA WEB QUE DESEAMOS OBTENER (IMAGENES, VIDEOS ...)
		System.out.print("Introducir el formato de los archivos que se quiere extraer de " + peticion + ": ");
		peticion = in.nextLine();

		// SEPARA LOS FORMATOS INTRODUCIDOS POR EL USUARIO POR COMAS, BUSCA EN EL
		// FICHERO GENERADO POR LA COMPONENTE ANTERIOR AQUELLAS FILAS QUE CONTENGAN AL
		// MENOS UN FORMATO QUE EL USUARIO DESEE (ES UN FILTRADO)
		formatos = peticion.split(",");
		buscador = new Buscador(ficheroIntermedio);
		ficheroIntermedio = buscador.buscar(formatos);

		// DECARGA DE LOS ARCHIVOS QUE HAYAN QUEDADO EN EL FICHERO PRODUCIDO POR LA
		// COMPONENTE ANTERIOR. NOTAR QUE SI EL USUARIO FILTRO UN FORMATO, HABRÁ
		// DESCARGAS, SI NO, ESTA OPCION NO VALE PARA NADA, SOLO RETRASA AL PROGRAMA
		// (POSIBLE CORRECCION: SOLICITUD POR TECLADO DE DESCAGAR)
		System.out.print("¿Desea descargar las líneas filtradas? [y/n]: ");
		peticion = in.nextLine().toUpperCase();

		if (peticion.equals("Y")) {
			descargador = new Descargador(ficheroIntermedio);
			descargador.descargar();
		}

		in.close();
	}

	public static void descargaAutomatica(Aragna aragna, String formatos[]) {

		String url;
		File ficheroIntermedio;
		File ficheroLector;

		// COMPONENTES
		Lector lector;
		Buscador buscador;
		Descargador descargador;
		Replicador replicador = new Replicador();

		while ((url = aragna.desenredar()) != null) {
			
			System.out.println("***************************");
			System.out.println("Iteraciones restantes: " + aragna.getIteraciones());
			System.out.println("***************************");
			
			try {
				//LECTOR
				lector = new Lector(new URL(url));
				ficheroLector = lector.leer();
				ficheroIntermedio = new File(ficheroLector.getAbsolutePath());
				
				//BUSCADOR
				buscador = new Buscador(ficheroIntermedio);
				ficheroIntermedio = buscador.buscar(formatos);
				
				//DESCARGADOR
				descargador = new Descargador(ficheroIntermedio);
				descargador.descargar();
				
				//ENREDAR
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
