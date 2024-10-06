package Main;

public class Main {

	public static void main(String[] args) {
		GerenteVentas gerente = new GerenteVentas();
		try {
			gerente.llenarListaProductos("Archivos\\Información productos.txt"); // Carga los productos
			gerente.llenarListaVendedores("Archivos\\Información vendedores.txt"); // Carga los vendedores
			gerente.procesarVentas(); // Procesa la informacion de las ventas
			gerente.generarArchivosReportes(); // Genera los archivos informes	
			System.out.println("Archivos Creados");
		} catch (Exception e) {
			System.err.println("Algo salio mal con la lectura de archivos");
		}

	} 

}
