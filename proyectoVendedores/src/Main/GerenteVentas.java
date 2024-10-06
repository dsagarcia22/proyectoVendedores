package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerenteVentas {
	// Listas para manejar la informacion de los archivos
	 private List<Vendedor> vendedores = new ArrayList<>();
	 private List<Producto> productos = new ArrayList<>();
	 
	// Carga productos desde un archivo
	 public void llenarListaProductos(String rutaArchivo){
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] data = linea.split(";");
                int price = Integer.parseInt(data[2]);
                // Verifica si el precio es negativo
                if (price < 0) {
                    System.err.println("Error: Precio negativo para el producto " + data[1]);
                    continue; // Omite el producto
                }
                productos.add(new Producto(data[0], data[1], price));
            }
        }
        catch (Exception e) {
			System.err.println("No se encuentra el archivo de productos");
			System.err.println("Error: " + e.getMessage());
		}       
	 }
	 
	// Carga vendedores desde un archivo
    public void llenarListaVendedores(String rutaArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] data = linea.split(";");
                vendedores.add(new Vendedor(data[2] + " " + data[3], data[1]));
            }
        }
        catch (Exception e) {
			System.err.println("No se encuentra el archivo de vendedores");
			System.err.println("Error: " + e.getMessage());
		}
    }
    
    // Procesa las ventas para todos los vendedores  
    public void procesarVentas() {
        for (Vendedor vendor : vendedores) {
            String vendedorRutaArchivo = "Archivos\\" + vendor.getDocumento() + ".txt";
            Map<String, Integer> infoVentas = leerInformacionVentas(vendedorRutaArchivo);
            calcularVentas(infoVentas);
        }
    }
    
    // Lee los datos de ventas de un vendedor
    private Map<String, Integer> leerInformacionVentas(String rutaArchivo) {
        Map<String, Integer> infoVentas = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            reader.readLine(); // Salta el encabezado
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] informacion = linea.split(";");
                String productoId = informacion[0];
                int cantidad;
                try {
                    cantidad = Integer.parseInt(informacion[1]);
                    // Verifica si la cantidad es negativa
                    if (cantidad < 0) {
                        System.err.println("Error: Cantidad negativa para el producto ID " + productoId);
                        continue; // Omite la entrada
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error: Formato inválido para la cantidad del producto ID " + productoId);
                    continue; // Omite la entrada
                }
                infoVentas.put(productoId, cantidad); // Guarda la cantidad vendida
            }
        } catch (IOException e) {
            System.err.println("Error reading vendor file: " + e.getMessage());
        }
        return infoVentas; // Devuelve los datos de ventas leídos
    }

    // Calcula las ventas basándose en los datos leídos
    private void calcularVentas(Map<String, Integer> salesData) {
        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            String productoId = entry.getKey();
            int cantidad = entry.getValue();
            Producto producto = encontrarProductoId(productoId); // Busca el producto por ID
            if (producto != null) {
                producto.aumentarCantidadVendida(cantidad); // Si existe, añade la cantidad vendida
            } else {
                System.err.println("Error: Producto ID " + productoId + " no existe.");
            }
        }
    }
    
    // Busca un producto por ID
    private Producto encontrarProductoId(String id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return producto;  // Retorna el producto encontrado
            }
        }
        return null; // Retorna null si no se encuentra
    }
    
    // Genera los archivos informes de ventas y productos vendidos
    public void generarArchivosReportes(){
    	try {
	        generarReporteMejorVendedor();
	        generarReporteMejorProducto();
		} catch (Exception e) {
			System.err.println("No se pudo crear los archivos reportes");
		}

    }

    // Genera el informe de los mejores vendedores
    private void generarReporteMejorVendedor(){
        Map<String, Integer> ventasVendedor = new HashMap<>();

        for (Vendedor vendor : vendedores) {
            String vendedorRutaArchivo = "Archivos\\" + vendor.getDocumento() + ".txt";
            Map<String, Integer> infoVentas = leerInformacionVentas(vendedorRutaArchivo);
            int ventasTotales = calcularVentasTotales(infoVentas);
            ventasVendedor.put(vendor.getNombre(), ventasTotales);
        }
        
        // Ordena los vendedores por total de ventas
        List<Map.Entry<String, Integer>> vendedoresOrdenados = new ArrayList<>(ventasVendedor.entrySet());
        vendedoresOrdenados.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Escribe el informe en un archivo CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ListaMejoresVendedores.csv"))) {
            for (Map.Entry<String, Integer> entry : vendedoresOrdenados) {
                writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
            }
        }
        catch (Exception e) {
			System.err.println("No se pudo crear el archivo");
		}
    }

    // Calcula las ventas totales basándose en los datos de ventas
    private int calcularVentasTotales(Map<String, Integer> salesData) {
        int total = 0;
        for (Map.Entry<String, Integer> entry : salesData.entrySet()) {
            Producto product = encontrarProductoId(entry.getKey());
            if (product != null) {
                total += product.getPrecio() * entry.getValue(); // Suma las ventas totales
            }
        }
        return total;  // Retorna el total calculado
    }

    // Genera el informe de productos vendidos
    private void generarReporteMejorProducto() throws IOException {
        List<Producto> productosOrdenados = new ArrayList<>(productos);
        productosOrdenados.sort(Comparator.comparingInt(Producto::getCantidadVendida).reversed());

        // Escribe el informe en un archivo CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ListaCantidadProductosVendidos.csv"))) {
            for (Producto product : productosOrdenados) {
                writer.write(product.toString() + "\n");
            }
        }
    }
}
