package logicaDelPrograma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Buscador {

	private File fichero;

	public Buscador(File fichero) {
		// CADA COMPONENTE BUSCADOR ESTÁ COMPUESTA POR EL FICHERO QUE VA A FILTRAR

		this.fichero = fichero;
	}

	public File buscar(String formato) {

		String[] formatos = new String[1];
		formatos[0] = formato;
		return this.buscar(formatos);
	}

	public File buscar(String[] formatos) {
		// FILTRA EL ARCHIVO QUE LO IDENTIFICA POR LOS FORMATOS INTRODUCIDOS COMO
		// PARAMETROS, CREA UN NUEVO DOCUMENTO INTERMEDIO, buscador.txt, QUE CONTIENE LA
		// INFORMACION FILTRADA, QUE PUEDE SER REUTILIZABLE

		File f = new File("buscador.txt");
		String linea;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fichero)));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));) {

			while ((linea = br.readLine()) != null) {
				// PARTE DOS VECES LA LINEA: UNA POR " Y OTRA POR ', QUE SON DISTINTAS FORMAS DE
				// SEPARAR INFORMACION EN HTML
				for (String trozo : linea.split("\"")) {
					for (String subTrozo : trozo.split("'")) {

						// SI EL RESULTADO CUMPLE ALGUNO DE LOS FORMATOS INDICADOS, LO ALMACENA
						if (Principal.checkFormat(subTrozo, formatos)) {

							// ULTIMA MODIFICACION: POR CODIFICACIÓN DE LAS WEB PUEDE TRATAR EL & POR SU
							// CODIGO UNICODE (\u0026), POR LO QUE EL BUSCADOR DEBERÁ VOLVER A CAMBIAR ESA
							// CODIFICACION POR EL CARACTER AL QUE REPRESENTA.
							bw.write(subTrozo.replace("\\u0026", "&") + "\r\n");
						}
					}
				}
			}
			bw.flush(); // INNECESARIO POR EL TRY WITH RESOURCES, MEJOR DICHO, REDUNDANTE.

			// MENSAJE DE EXITO POR CONSOLA SI SE HA REALIZADO CON ÉXITO.
			System.out.println("Se ha filtrado la petición y se han guardado las coincidencias en " + f.getName());

		} catch (IOException e) {

			// MENSAJE DE ERROR EN CASO DE QUE HAYA UN PROBLEMA CON LA APERTURA DE LOS
			// FICHEROS O DEL URL.OPENSTREAM.
			System.err.println("Ha ocurrido un error inesperado.");
		}

		return f;
	}
}
