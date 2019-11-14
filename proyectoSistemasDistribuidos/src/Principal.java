
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {

		Scanner in = new Scanner(System.in);
		String peticion;
		File ficheroIntermedio;

		// NUESTRAS COMPONENTES
		Lector lector;

		if (args.length == 0) {
			System.out.println("Introducir una URL o el nombre y formato de un archivo: ");
			peticion = in.nextLine();
		} else {
			peticion = args[1];
		}

		// COMPROBAMOS SI ES UN ARCHIVO O UNA DIRECCION:
		ficheroIntermedio = new File(peticion);

		if (ficheroIntermedio.isFile()) {
			ficheroIntermedio = new File(peticion);
		} else {
			try {
				lector = new Lector(new URL(peticion));
				ficheroIntermedio = lector.leer();

			} catch (MalformedURLException e) {
				System.out.println("La cadena introducida no es un archivo ni una URL.");
				System.out.print("¿Intentar de nuevo? [y/n] ");
				peticion = in.nextLine();

				if (peticion.toLowerCase().equals("y")) {
					main(null);
				}
				in.close();
				return;
			}
		}
		
		in.close();
	}
}
