

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Descargador {

	private File fichero;

	public Descargador(File fichero) {

		this.fichero = fichero;
	}

	public void descargar() {

		String img, file;
		URL url;
		byte[] buff = new byte[1024 * 32];
		int leidos;

		try (DataInputStream fis = new DataInputStream(new FileInputStream(this.fichero));) {

			while ((img = fis.readLine()) != null) {
				url = new URL(img);
				file = url.getFile();
				try (InputStream in = new BufferedInputStream(url.openStream());
						FileOutputStream fos = new FileOutputStream(
								file.substring(file.lastIndexOf("/") + 1, file.indexOf("?")));) {

					while ((leidos = in.read(buff)) > 0) {
						fos.write(buff, 0, leidos);
					}

					fos.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.out.println("Descarga de imagenes terminada.");

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
	}

	private static String formato(String linea) {

		int indexInterrogante = linea.indexOf("?");

		if (indexInterrogante < 0) {
			return linea.substring(linea.length() - 4);
		} else {
			return linea.substring(indexInterrogante - 4, indexInterrogante);
		}
	}
}
