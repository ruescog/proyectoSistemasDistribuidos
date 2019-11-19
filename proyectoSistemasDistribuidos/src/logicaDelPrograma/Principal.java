package logicaDelPrograma;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {
		// DESCARGA LA PAGINA INTRODUCIDA POR TECLADO, FILTRA EL ARCHIVO DESCARGADO O
		// INDICADO POR EL USUARIO (ARGS[0]) Y DESCARGA LOS ARCHIVOS QUE EN EL SE
		// ENCUENTREN.

		// VARIABLES
		Scanner in = new Scanner(System.in);
		String peticion, formatos[];
		File ficheroIntermedio;

		// NUESTRAS COMPONENTES
		Lector lector;
		Buscador buscador;
		Descargador descargador;

		// SI EL NUMERO DE ARGUMENTOS ES 0, SE ENTIENDE QUE ES UNA BUSQUEDA DE URL, SI
		// ES UNO, SE ENTIENDE QUE ES UN ARCHIVO (POR DEFECTO)
		if (args.length == 0) {
			System.out.print("Introducir una URL o el nombre y formato de un archivo: ");
			peticion = in.nextLine();
		} else {
			peticion = args[0];
		}

		// COMPROBAMOS SI ES UN ARCHIVO O UNA DIRECCION (POR SI EL USUARIO NO HA
		// INDICADO FICHERO PERO HA INTRODUCIDO UN FICHERO EN VEZ DE UNA URL)
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
					main(null);
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
		descargador = new Descargador(ficheroIntermedio);
		descargador.descargar();

		in.close();
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
