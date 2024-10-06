package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
				producto.put("Cantidad", "0");
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
		crearListaCantiadadProductosTotales(productos,vendedores);
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
	    
	    // Crea el archivo de los productos oraganizados por cantidad vendida
	    public static void crearListaCantiadadProductosTotales(LinkedList<HashMap<String, String>> productos, LinkedList<HashMap<String, String>> vendedores) {
	    	// Iterate sobre todos los vendedores
	    	for (HashMap<String, String> vendedor : vendedores) {
	    	    String documento = vendedor.get("Documento");

	    	    // Construya la ruta del archivo en función del ID del documento del vendedor
	    	    String vendedorFilePath = "Archivos\\" + vendedor.get("Documento") + ".txt";

	    	    // Lea los datos de ventas del vendedor
	    	    try (BufferedReader readerVendedor = new BufferedReader(new FileReader(vendedorFilePath))) {
	    	        // Lee e ignora la primera línea
	    	        readerVendedor.readLine();

	    	        String infoProductoVendido;
	    	        while ((infoProductoVendido = readerVendedor.readLine()) != null) {
	    	            String[] parts = infoProductoVendido.split(";");

	    	            if (parts.length < 2) {
	    	                System.err.println("Error: Invalid format in vendor file for documento " + documento);
	    	                continue;
	    	            }

	    	            String productoID = parts[0]; // Producto ID
	    	            int cantidadVendida;

	    	            try {
	    	                cantidadVendida = Integer.parseInt(parts[1]); // Cantidad Vendida
	    	            } catch (NumberFormatException e) {
	    	                System.err.println("Error: Invalid quantity format for product ID " + productoID);
	    	                continue;
	    	            }

	    	            // Actualizar la cantidad en la lista de productos
	    	            for (HashMap<String, String> producto : productos) {
	    	                if (producto.get("ID").equals(productoID)) {
	    	                    int cantidadActual = Integer.parseInt(producto.get("Cantidad"));
	    	                    producto.put("Cantidad", String.valueOf(cantidadActual + cantidadVendida));
	    	                    break; // Detener la búsqueda una vez encontrado el producto
	    	                }
	    	            }
	    	        }
	    	    } catch (IOException e) {
	    	        System.err.println("Error leyendo el archivo del vendedor " + documento + ": " + e.getMessage());
	    	    }
	    	}
	    	
	    	ordenarProductosPorCantidad(productos);
	    	
	    	 try(BufferedWriter writer = new BufferedWriter(new FileWriter("ListaCantidadProductosVendidos.csv"))) {
	    		 
	 	        for (HashMap<String, String> producto : productos) {
		            writer.write(producto.get("Nombre"));
		            writer.write(";");
		            writer.write(producto.get("Precio"));
		            writer.write(";");
		            writer.write(producto.get("Cantidad"));
		            writer.write("\n");
		        }   
	 	        writer.close();
					
	    	 } catch (IOException e) {
					e.printStackTrace();
	    	 }  	
	    }
	    
	    // Ordena la lista de productos por cantidad de forma descendente
	    private static void ordenarProductosPorCantidad(LinkedList<HashMap<String, String>> productos) {
	        Collections.sort(productos, new Comparator<HashMap<String, String>>() {
	            @Override
	            public int compare(HashMap<String, String> p1, HashMap<String, String> p2) {
	                int cantidad1 = Integer.parseInt(p1.get("Cantidad"));
	                int cantidad2 = Integer.parseInt(p2.get("Cantidad"));
	                return Integer.compare(cantidad2, cantidad1); // Descending order
	            }
	        });
	    }

}
