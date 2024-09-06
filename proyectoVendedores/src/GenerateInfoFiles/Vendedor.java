package GenerateInfoFiles;

import java.util.HashMap;
import java.util.LinkedList;

public class Vendedor {
	private String nombre; // Nombre del vendedor
	private String apellido;  // Apellido del vendedor
	private String tipoDocumento; // Tipo de documento (por ejemplo, cédula)
	private String numeroDocumento;	// Número de documento
	private LinkedList<HashMap<String, String>> productosVendedor = new LinkedList<HashMap<String,String>>(); // Lista de productos del vendedor

	public Vendedor(String nombre, String apellido, String tipoDocumento, String numeroDocumento, LinkedList<HashMap<String, String>> productosVendedor){
		this.nombre = nombre;
		this.apellido = apellido;
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.productosVendedor = productosVendedor;
		
	}
	
	// Métodos para obtener información del vendedor
	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	
	// Método para obtener la lista de productos del vendedor
	public LinkedList<HashMap<String, String>> getProductosVendedor() {
		return productosVendedor;
	}
}
