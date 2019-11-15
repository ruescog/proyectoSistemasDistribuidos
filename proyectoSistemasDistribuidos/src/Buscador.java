

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

		this.fichero = fichero;
	}

	public File buscar(String [] formatos) {

		File f = new File("buscador.txt");
		String linea;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.fichero)));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));) {

			while ((linea = br.readLine()) != null) {
				for (String trozo : linea.split("\"")) {
					for (String subTrozo : trozo.split("'")) {
						if (Principal.checkFormat(subTrozo, formatos)) {
							bw.write(subTrozo.replace("\\u0026", "&") + "\r\n");
						}
					}
				}
			}
			bw.flush();

			System.out.println("Se ha filtrado la petición y se han guardado las coincidencias en " + f.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return f;
	}
}
