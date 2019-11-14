import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.UnknownHostException;

public class Lector {

	private URL url;

	public Lector(URL url) {

		this.url = url;
	}

	public File leer() {

		File f = new File("lector.txt");
		String linea;

		try (DataInputStream dis = new DataInputStream(this.url.openStream());
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));) {

			while ((linea = dis.readLine()) != null) {
				bw.write(linea + "\r\n");
			}

			System.out.println("La web ha sido leida. El resultado ha sido almacenado en " + f.getName());

		} catch (UnknownHostException e) {
			System.out.println("La URL especificada no ha podido ser encontrada.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			return f;
		}
	}
}
