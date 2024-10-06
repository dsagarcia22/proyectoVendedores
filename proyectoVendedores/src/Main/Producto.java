package Main;

public class Producto {
	private String id;
	private String nombre;
	private int precio;
	private int cantidadVendida;
	
	public Producto(String id, String nombre, int precio) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidadVendida = 0;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public int getPrecio() {
		return precio;
	}

	public int getCantidadVendida() {
		return cantidadVendida;
	}
	
	public void aumentarCantidadVendida(int cantidad) {
		this.cantidadVendida += cantidad;
	}
	
    @Override
    public String toString() {
        return nombre + ";" + precio + ";" + cantidadVendida;
    }

}
