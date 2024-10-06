package Main;

public class Vendedor {
	private String documento;
	private String nombre;
	private int dineroRecaudado;
	
	public Vendedor(String nombre, String documento) {
		this.documento = documento;
		this.nombre = nombre;
		this.dineroRecaudado = 0;
	}

	public String getDocumento() {
		return documento;
	}

	public String getNombre() {
		return nombre;
	}

	public int getCantidadRecaudada() {
		return dineroRecaudado;
	}
	
	public void aumentarCantidadRecaudada(int cantidad) {
		this.dineroRecaudado += cantidad;
	}
	
	@Override
	public String toString() {
		return nombre + ";" + dineroRecaudado;
	}
}
