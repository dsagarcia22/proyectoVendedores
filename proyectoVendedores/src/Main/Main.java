package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		// Listas de vendedores y productos para guardar la informacion de los archivos bases
		LinkedList<HashMap<String, String>> vendedores = new LinkedList<HashMap<String,String>>();
		LinkedList<HashMap<String, String>> productos = new LinkedList<HashMap<String,String>>();
		
		// Popula la lista de productos
		try {
			BufferedReader readerProductos = new BufferedReader(new FileReader("Archivos\\Información productos.txt"));
			String infoProducto;
			while ((infoProducto = readerProductos.readLine()) != null) {
				String[] informacionProducto = infoProducto.split(";");
				HashMap<String, String> producto = new HashMap<String, String>();
				producto.put("ID",informacionProducto[0]);
				producto.put("Nombre",informacionProducto[1]);
				producto.put("Precio",informacionProducto[2]);
				productos.add(producto);
			}
			readerProductos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Popula la lista de vendedores
		try {
			BufferedReader readerVendedores = new BufferedReader(new FileReader("Archivos\\Información vendedores.txt"));
			String infoVendedor;
			while ((infoVendedor = readerVendedores.readLine()) != null) {
				String[] informacionVendedor = infoVendedor.split(";");
				HashMap<String, String> vendedor = new HashMap<String, String>();
				vendedor.put("Nombre", informacionVendedor[2] + " " + informacionVendedor[3]);
				vendedor.put("Documento", informacionVendedor[1]);
				vendedores.add(vendedor);
			}
			readerVendedores.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Crea el archivo con las mejores vendedores
		crearListaMejorVendedores(vendedores,productos);
	}
	
	// Crea un archivo con la lista descendente de los vendedores segun sus ganancias
		public static void crearListaMejorVendedores(LinkedList<HashMap<String, String>> listVendedores, LinkedList<HashMap<String, String>> listProductos) {
			// Lista para almacenar la informacion de los vendedores y sus ganancias
			LinkedHashMap<String, Integer> unorderedListVendedores = new LinkedHashMap<>();
			
			// Itera la lista de vendedores
			for(HashMap<String, String> vendedor: listVendedores) {
				int ganancias = 0;
				try {
					// Lee la informacion del archivo de un vendedor segun su documento
					BufferedReader fileVendedor = new BufferedReader(new FileReader("Archivos\\" + vendedor.get("Documento") + ".txt"));
					fileVendedor.readLine();
					String productosVendedor;
					HashMap<String, String>productos = new HashMap<String, String>();
					
					// Añade todos los productos del vendedor en una hashmap
					while((productosVendedor = fileVendedor.readLine()) != null) {
						String[] infoProductosVendedor = productosVendedor.split(";");
						productos.put(infoProductosVendedor[0], infoProductosVendedor[1]);
					}
					fileVendedor.close();
					
					ganancias = calcularGanancias(listProductos,productos);
					unorderedListVendedores.put(vendedor.get("Nombre"), ganancias);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
			// Convierte de LinkedHashmap a un Entry y lo organiza de forma descendente
			List<Map.Entry<String, Integer>> entryList = new ArrayList<>(unorderedListVendedores.entrySet());
	        entryList.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

	        // Crea un nuevo LinkedHashMap para preservar el orden de lo sitems
	        LinkedHashMap<String, Integer> orderedListVendedores = new LinkedHashMap<>();
	        for (Map.Entry<String, Integer> entry : entryList) {
	            orderedListVendedores.put(entry.getKey(), entry.getValue());
	        }
	        
	        // Crea un archivo y escribe los items de la lista de vendedores
	        try(BufferedWriter writer = new BufferedWriter(new FileWriter("ListaMejoresVendedores.csv"))) {
	            for (String key : orderedListVendedores.keySet()) {
	                writer.write(key);
	                writer.write(";");
	                writer.write(String.valueOf(orderedListVendedores.get(key)));
	                writer.write("\n"); 
	            }
	            writer.close(); 
				
			} catch (IOException e) {
				e.printStackTrace();
			}


	  	}
		
		// Calcula las ganacias totales de todos los productos de un vendedor y devuelve el valor final
		public static int calcularGanancias(LinkedList<HashMap<String, String>> listProductos, HashMap<String, String> productos) {
			int gananciasTotales = 0;
			
			// Itera cada producto del vendor y calcula las ganacias
			for (String Producto: productos.keySet()) {
				HashMap<String, String> foundProduct = findProductById(listProductos, Producto);
				
				int cantidadProducto = Integer.valueOf(productos.get(Producto));
				int precioProducto = Integer.valueOf(foundProduct.get("Precio"));
				
				gananciasTotales += cantidadProducto * precioProducto;
			}
			
			return gananciasTotales;
		}
		
		// Encuentra la informacion de un producto en especifico
	    public static HashMap<String, String> findProductById(LinkedList<HashMap<String, String>> productos, String id) {
	        for (HashMap<String, String> producto : productos) {
	            if (producto.containsKey("ID") && producto.get("ID").equals(id)) {
	                return producto; // Return the found product
	            }
	        }
	        return null; // Return null if not found
	    }


}
