package GenerateInfoFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class GenerateInfoFiles {

	public static void main(String[] args) {
		// Lista de hashmaps para los productos
				LinkedList<HashMap<String, String>> productos = new LinkedList<HashMap<String,String>>();
				// Lista de objetos para los vendedores 
				LinkedList<Vendedor> vendedores = new LinkedList<Vendedor>();
				
				// Carga la información de los productos desde el archivo "Productos.txt"
				try {
					BufferedReader readerProductos = new BufferedReader(new FileReader("Base de datos\\Productos.txt"));
					String lineProductos;
					// Divide la informacion de cada producto y lo agrega a la lista como un hashmap
					while ((lineProductos = readerProductos.readLine()) != null) {
						String[] informacionProducto = lineProductos.split(";");
						HashMap<String, String> producto = new HashMap<String, String>();
						producto.put("ID", informacionProducto[0]);
						producto.put("Nombre", informacionProducto[1]);
						producto.put("Precio", informacionProducto[2]);
						productos.add(producto);
					}			
					readerProductos.close();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				// Carga la información de los vendedores desde el archivo "Vendedores.txt"
				try {
					BufferedReader readerVendedores = new BufferedReader(new FileReader("Base de datos\\Vendedores.txt"));
					Random randomizador = new Random();
					String lineVendedor;
					while ((lineVendedor = readerVendedores.readLine()) != null) {
						String[] informacionVendedor = lineVendedor.split(";");
						//	Lee los datos de cada vendedor en la base de datos, crea un objeto "Vendedor" con esa información y lo agrega a la lista.
						//	Selecciona un número aleatorio de productos para cada vendedorr
						int numeroDeProductos = randomizador.nextInt(9) + 1;
						LinkedList<HashMap<String, String>> productosVendedor = selectRandomItems(productos, numeroDeProductos);
						for (int i = 0; i < productosVendedor.size(); i++) {
							 int cantidadVendidos = randomizador.nextInt(24) + 1;
							 productosVendedor.get(i).put("Cantidad Vendidos",String.valueOf(cantidadVendidos));  
					    }
						Vendedor vendedor = new Vendedor(informacionVendedor[0], informacionVendedor[1], informacionVendedor[2], informacionVendedor[3], productosVendedor);
						vendedores.add(vendedor);
					}
					readerVendedores.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Crea los archivos necesarios con la información de los vendedores y sus productos.
				crearArchivo1(vendedores);
				crearArchivo2(vendedores);
				crearArchivo3(vendedores);

	}
	
	// Método para seleccionar elementos aleatorios de la lista de productost
	public static LinkedList<HashMap<String, String>> selectRandomItems(LinkedList<HashMap<String, String>> list, int n) {
        LinkedList<HashMap<String, String>> result = new LinkedList<HashMap<String,String>>();
        HashSet<Integer> selectedIndices = new HashSet<>();
        Random random = new Random();

        while (result.size() < n && selectedIndices.size() < list.size()) {
            int index = random.nextInt(list.size());
            if (!selectedIndices.contains(index)) {
                selectedIndices.add(index);
                result.add(list.get(index));
            }
        }

        return result;
    }
	
	// Escribe la información del vendedor y sus productos en un archivo individual
	public static void crearArchivo1(LinkedList<Vendedor> listaVendedores) {
		for(Vendedor vendedor: listaVendedores) {
			// Usa el numero de documento de cada vendedor como nombre del archivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Archivos\\"+vendedor.getNumeroDocumento()+".txt"))) {
    			// Información del vendedor
                writer.write(vendedor.getTipoDocumento());
                writer.write(";");
                writer.write(vendedor.getNumeroDocumento());
                writer.write("\n");
                // Productos del vendedor
                LinkedList<HashMap<String, String>> productos = vendedor.getProductosVendedor();
                for (int i = 0; i < productos.size(); i++) {
					writer.write(productos.get(i).get("ID"));
					writer.write(";");
					writer.write(productos.get(i).get("Cantidad Vendidos"));
					writer.write("\n");
				}
                writer.close();
         
            } catch (IOException e) {
                e.printStackTrace();
            }
            
		}
	}
	
	// Crea un archivo con la lista de todos los vendedores
	public static void crearArchivo2(LinkedList<Vendedor> listaVendedores) {
		 try (BufferedWriter writer = new BufferedWriter(new FileWriter("Archivos\\Información vendedores.txt"))){
			 for(Vendedor vendedor: listaVendedores) {
	            writer.write(vendedor.getTipoDocumento());
	            writer.write(";");
	            writer.write(vendedor.getNumeroDocumento());
	            writer.write(";");
	            writer.write(vendedor.getNombre());
	            writer.write(";");
	            writer.write(vendedor.getApellido());
	            writer.write("\n");  
			 }
			 writer.close(); 
	
		 } catch (IOException e) {
			 e.printStackTrace();
		}
			 
	}
	
	//	Crea un archivo con la lista de todos los productos de los vendedores
	public static void crearArchivo3(LinkedList<Vendedor> listaVendedores) {
		LinkedList<HashMap<String, String>> productos = new LinkedList<HashMap<String,String>>();
		// Reúne los productos de todos los vendedores en una sola lista
		for(Vendedor vendedor: listaVendedores) {
			for (int i = 0; i < vendedor.getProductosVendedor().size(); i++) {
				if(!productos.contains(vendedor.getProductosVendedor().get(i))) {
					productos.add(vendedor.getProductosVendedor().get(i));
				}
			}
		}
		// Escribe todos los productos en el archivo
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Archivos\\Información productos.txt"))){
			for (HashMap<String, String> producto : productos) {
	            writer.write(producto.get("ID"));
	            writer.write(";");
	            writer.write(producto.get("Nombre"));
	            writer.write(";");
	            writer.write(producto.get("Precio"));
	            writer.write("\n"); 
			}
			writer.close(); 
		}catch (IOException e) {
			 e.printStackTrace();
		}
	}

}
