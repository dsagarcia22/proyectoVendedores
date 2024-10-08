package GenerateInfoFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;  
import java.util.Map;
import Bases_de_Datos.*;

/**
 * Clase que genera archivos de información sobre vendedores, productos y ventas.
 */
public class GenerateInfoFiles {

    // Constantes para los nombres de las carpetas
    private static final String CARPETA_PRINCIPAL = "Documentos";
    private static final String CARPETA_VENTAS = "Ventas";
    private static final String CARPETA_VENDEDORES = "Vendedores";
    private static final String CARPETA_PRODUCTOS = "Productos";

    // Cantidad de vendedores, productos y configuraciones de ventas
    private static final int NUM_VENDEDORES = 8;
    private static final int NUM_PRODUCTOS = 12;
    private static final int NUM_PRODUCTOS_POR_VENDEDOR = 5;
    private static final int MAX_CANTIDAD = 5;

    /**
     * Método principal que ejecuta la generación de archivos.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Crear instancias de las carpetas principales
        File carpetaPrincipal = new File(CARPETA_PRINCIPAL);
        File carpetaVentas = new File(carpetaPrincipal, CARPETA_VENTAS);
        File carpetaVendedores = new File(carpetaPrincipal, CARPETA_VENDEDORES);
        File carpetaProductos = new File(carpetaPrincipal, CARPETA_PRODUCTOS);

        // Crear las carpetas necesarias
        crearCarpeta(carpetaPrincipal);
        crearCarpeta(carpetaVentas);
        crearCarpeta(carpetaVendedores);
        crearCarpeta(carpetaProductos);

        // Mapa para almacenar los números de documento de los vendedores
        Map<String, String> numerosDocumento = new HashMap<>();

        // Crear archivos de información de vendedores, ventas y productos
        crearArchivoInfoVendedores(new File(carpetaVendedores, "vendedores_info.txt"), numerosDocumento);
        crearArchivosVentas(carpetaVentas, numerosDocumento);
        crearArchivoProductos(new File(carpetaProductos, "productos_info.txt"));
    }

    /**
     * Crea una carpeta en el sistema de archivos.
     *
     * @param carpeta La carpeta a crear.
     */
    private static void crearCarpeta(File carpeta) {
        if (carpeta.exists()) {
            System.out.println("La carpeta ya existe: " + carpeta.getAbsolutePath());
        } else if (carpeta.mkdirs()) {
            System.out.println("Carpeta creada: " + carpeta.getAbsolutePath());
        } else {
            System.err.println("Error al crear la carpeta: " + carpeta.getName());
        }
    }

    /**
     * Crea un archivo con la información de los vendedores. Cada vendedor tiene un tipo de documento, número de documento, nombre y apellido.
     *
     * @param archivo           El archivo donde se almacenará la información de los vendedores.
     * @param numerosDocumento  Un mapa que asocia los números de documento con los identificadores de los vendedores.
     */
    private static void crearArchivoInfoVendedores(File archivo, Map<String, String> numerosDocumento) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (int i = 0; i < NUM_VENDEDORES; i++) {
                // Generar datos aleatorios del vendedor
                String tipoDocumento = Nombres_Vendedores.obtenerTipoDocumentoAleatorio();
                String numeroDocumento = Nombres_Vendedores.generarNumeroDocumentoAleatorio();
                String[] nombre = Nombres_Vendedores.generarNombreUnico();
                String nombreVendedor = nombre[0];
                String apellidoVendedor = nombre[1];
                
                // Escribir los datos del vendedor en el archivo
                bw.write(String.format("%s;%s;%s;%s%n", tipoDocumento, numeroDocumento, nombreVendedor, apellidoVendedor));
                
                // Almacenar el número de documento en el mapa de vendedores
                numerosDocumento.put(numeroDocumento, "Vendedor_" + i);
            }
            System.out.println("Archivo de vendedores generado correctamente en: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al generar el archivo de vendedores: " + archivo.getAbsolutePath());
            e.printStackTrace();
        }
    }

    /**
     * Crea archivos de ventas para cada vendedor. Cada archivo contiene el número de documento del vendedor y una lista de ventas con identificadores de productos y cantidades.
     *
     * @param carpeta          La carpeta donde se crearán los archivos de ventas.
     * @param numerosDocumento Un mapa que asocia los números de documento con los identificadores de los vendedores.
     */
    private static void crearArchivosVentas(File carpeta, Map<String, String> numerosDocumento) {
        for (String numeroDocumento : numerosDocumento.keySet()) {
            File archivoVentas = new File(carpeta, "ventas_" + numeroDocumento + ".txt");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoVentas))) {
                // Escribir el encabezado con el número de documento del vendedor
                bw.write(String.format("DNI;%s%n", numeroDocumento));
                
                // Generar y escribir las ventas del vendedor
                for (int j = 0; j < NUM_PRODUCTOS_POR_VENDEDOR; j++) {
                    String idProducto = "P" + String.format("%03d", j + 1);
                    int cantidad = Nombres_Vendedores.RANDOM.nextInt(MAX_CANTIDAD) + 1; // Cantidad aleatoria entre 1 y MAX_CANTIDAD
                    bw.write(String.format("%s;%d;%n", idProducto, cantidad));
                }
                System.out.println("Archivo de ventas generado correctamente para el vendedor: " + numeroDocumento);
            } catch (IOException e) {
                System.err.println("Error al generar el archivo de ventas para el vendedor con número de documento: " + numeroDocumento);
                e.printStackTrace();
            }
        }
    }

    /**
     * Crea un archivo con información pseudo-aleatoria sobre los productos.
     *
     * @param archivo El archivo donde se almacenará la información de los productos.
     */
    private static void crearArchivoProductos(File archivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (int i = 0; i < NUM_PRODUCTOS; i++) {
                // Generar datos aleatorios del producto
                String idProducto = "P" + String.format("%03d", i + 1);
                String nombreProducto = Productos.obtenerNombreProductoAleatorio();
                int precio = Productos.obtenerPrecioProductoAleatorio();
                
                // Escribir los datos del producto en el archivo
                bw.write(String.format("%s;%s;%,d%n", idProducto, nombreProducto, precio));
            }
            System.out.println("Archivo de productos generado correctamente en: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al generar el archivo de productos: " + archivo.getAbsolutePath());
            e.printStackTrace();
        }
    }
}


