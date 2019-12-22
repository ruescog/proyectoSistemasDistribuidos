/* Una excepcion de este tipo será lanzada si el archivo que se intenta descargar se ha descargado ya un numero de veces (determinado por el usuario)
 */

package logicaDescargador;

public class MultiplicityExceededException extends Exception {

	private String mensaje;

	public MultiplicityExceededException(String mensaje) {

		this.mensaje = mensaje;
	}

	public String getMessage() {

		return this.mensaje;
	}
}
