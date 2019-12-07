package logicaDescargador;

public class MultiplicityExceededException extends Exception{

	private String mensaje;
	
	public MultiplicityExceededException(String mensaje) {
		
		this.mensaje = mensaje;
	}
	
	public String getMessage() {
		
		return this.mensaje;
	}
}
