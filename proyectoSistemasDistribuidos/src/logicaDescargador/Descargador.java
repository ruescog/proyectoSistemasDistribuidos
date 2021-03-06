/* Esta componente se encarga de, dado un archivo con ficheros para descargar, efectivamente descargar todas aquellas que pueda.
 */

package logicaDescargador;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Descargador {

	private File fichero;
	public int urlInspeccionadas;
	public int archivosDescargados;

	public Descargador(File fichero) {
		// cada componente descargador est� compuesta por el fichero del cual va a
		// descargar sus lineas

		this.fichero = fichero;
		urlInspeccionadas = 0;
		archivosDescargados = 0;
	}

	public void descargar() {

		ExecutorService poolDeHilos = Executors.newFixedThreadPool(100);
		String img;

		try (DataInputStream fis = new DataInputStream(new FileInputStream(this.fichero));) {

			while ((img = fis.readLine()) != null) {
				poolDeHilos.execute(new DescargadorConcurrente(this, img));
			}
		} catch (IOException e) {
			// mensaje de error en caso de que haya un problema con la apertura de los
			// ficheros o del url.openstream.

			System.err.println("Ha ocurrido un error inesperado con la apertura del fichero " + this.fichero + ".");
		} finally {
			// mensaje de informacion
			if (this.urlInspeccionadas != 0) {
				System.out.print("Porcentaje de �xito de descarga: "
						+ this.archivosDescargados * 100.0 / this.urlInspeccionadas + "%.");
				System.out.print(" Descargado: " + this.archivosDescargados + ", inspeccionados: "
						+ this.urlInspeccionadas + ".");
				System.out.println(" Descarga de imagenes terminada.");
			}

			poolDeHilos.shutdown();
		}
	}

//	public void descargar() {
//		// DESCARGA TODOS LOS ARCHIVOS QUE PUEDA DE LOS CUALES CONTIENE LA INFORMACION
//		// EN EL FICHERO QUE LO DEFINE
//
//		String img, file, formato;
//		URL url;
//		byte[] buff = new byte[1024 * 32];
//		int leidos, indiceBarra, indiceInterrogante, multiplicidad;
//
//		try (DataInputStream fis = new DataInputStream(new FileInputStream(this.fichero));) {
//
//			// PARA CADA LINEA DEL FICHERO ...
//			while ((img = fis.readLine()) != null) {
//
//				// SUPONE QUE ES UNA URL A UN ARCHIVO QUE EST� EN LA NUBE
//				try {
//					url = new URL(img);
//
//					// CREA LOS OBJETOS NECESARIOS PARA LEER DE LA WEB Y GUARDARLO EN LOCAL, NOMBRA
//					// AL OBJETO CON EL NOMBRE QUE TENGA EN LA WEB
//					file = url.getFile();
//
//					// DETERMINA DONDE ESTAR� EL NOMBRE DEL FICHERO
//
//					// +1 PORQUE NO NOS INTERESA LA BARRA ... NOTAR QUE EN CASO DE QUE SEA 0 (NO
//					// HAYA BARRA), ESTO INDICAR� AL PRINCIPIO DE LA CADENA
//					indiceBarra = file.lastIndexOf("/") + 1;
//
//					// LA MAYOR�A DE LOS ARCHIVOS TENDR�N INFORMACI�N ADICIONAL SEPARADA POR EL ?,
//					// NO NOS INTERESA AQUI.
//					indiceInterrogante = file.indexOf("?");
//
//					// ESTE INDICE NO SE "ARREGLA SOLO" COMO EL DE /, AS� QUE SI NO HAY INTERROGANTE
//					// TENEMOS QUE INDICAR QUE TOMAR� COMO NOMBRE TODA LA CADENA
//					if (indiceInterrogante == -1) {
//						indiceInterrogante = file.length();
//					}
//
//					file = file.substring(indiceBarra, indiceInterrogante); // EL NOMBRE DE FICHERO (QUE SE GENERAR�)
//					multiplicidad = Replicador.a�adirDato(file);
//
//					if (multiplicidad == -1) {
//						throw new MultiplicityExceededException("Multiplicidad superada: " + img);
//					}
//
//					if (multiplicidad != 0) { // SI EST� REPETIDO ...
//						formato = file.substring(file.lastIndexOf("."));
//						file = file.substring(0, file.lastIndexOf("."));
//						file = file.concat(" (" + multiplicidad + ")");
//						file = file.concat(formato);
//					}
//
//					try (InputStream in = new BufferedInputStream(url.openStream());
//							FileOutputStream fos = new FileOutputStream(file);) {
//
//						// DESCARGA TODA LA INFORMACI�N DE LA URL QUE INDICABA LA LINEA DEL FICHERO
//						while ((leidos = in.read(buff)) > 0) {
//							fos.write(buff, 0, leidos);
//						}
//
//						fos.flush(); // DE NUEVO, UN FLUSH REDUNDANTE POR EL TRY WITH RESOURCES
//
//						// +1 A ARCHIVOS DESCARGADOS
//						this.archivosDescargados++;
//
//					} catch (IOException e) {
//
//						// MENSAJE DE ERROR EN CASO DE QUE HAYA UN PROBLEMA CON LA APERTURA DE LOS
//						// FICHEROS O DEL URL.OPENSTREAM.
//						System.err.println("Ha ocurrido un error inesperado con la apertura del fichero " + file + ".");
//					} catch (IllegalArgumentException e) {
//						System.err.println("El protocolo o el host son desconocidos.");
//					}
//				} catch (MalformedURLException e) {
//					// MENSAJE DE ERROR EN CASO DE QUE LA URL NO CUMPLA CON LA SINTAXIS (NO EXISTA)
//					System.err.println("La URL especificada no ha podido ser encontrada.");
//				} catch (StringIndexOutOfBoundsException e) {
//					System.err.println("La direcci�n no ha podido ser identificada.");
//				} catch (MultiplicityExceededException e) {
//					System.out.println(e.getMessage());
//
//				} finally {
//					this.urlInspeccionadas++;
//				}
//			}
//
//			// MENSAJE DE INFORMACION
//			if (this.urlInspeccionadas != 0) {
//				System.out.print("Porcentaje de �xito de descarga: "
//						+ this.archivosDescargados * 100.0 / this.urlInspeccionadas + "%.");
//				System.out.print(" Descargado: " + this.archivosDescargados + ", inspeccionados: "
//						+ this.urlInspeccionadas + ".");
//				System.out.println(" Descarga de imagenes terminada.");
//			}
//		} catch (IOException ex1) {
//
//			// MENSAJE DE ERROR EN CASO DE QUE HAYA UN PROBLEMA CON LA APERTURA DE LOS
//			// FICHEROS O DEL URL.OPENSTREAM.
//			System.err.println("Ha ocurrido un error inesperado con la URL.");
//		}
//	}
}
