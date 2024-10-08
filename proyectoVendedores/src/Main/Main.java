package Main;

import java.io.*;
import java.util.*;

/**
 * Clase principal del programa que procesa la información de ventas y genera archivos CSV de recaudación de vendedores y productos vendidos.
 */
public class Main {

    // Constantes para ubicaciones de carpetas y archivos
    private static final String CARPETA_VENTAS = "Documentos/Ventas";
    private static final String ARCHIVO_CSV_VENDEDORES = "Documentos/reportes/recaudacion.csv";
    private static final String ARCHIVO_CSV_PRODUCTOS = "Documentos/reportes/productos_vendidos.csv"; // CSV para productos vendidos
    private static final String ARCHIVO_VENDEDORES = "Documentos/Vendedores/vendedores_info.txt";
    private static final String ARCHIVO_PRODUCTOS = "Documentos/Productos/productos_info.txt";

    public static void main(String[] args) {
        // Mapas para almacenar la recaudación de cada vendedor y otras informaciones necesarias
        Map<String, Double> recaudacionVendedores = new HashMap<>();
        Map<String, String> nombresVendedores = cargarNombresVendedores();
        Map<String, Double> preciosProductos = cargarPreciosProductos();
        Map<String, String> nombresProductos = cargarNombresProductos(); // Cargar nombres de productos
        Map<String, Integer> cantidadVendidaPorProducto = new HashMap<>(); // Mapa para almacenar cantidad vendida por producto

        // Procesar los archivos de ventas para cada vendedor
        File carpetaVentas = new File(CARPETA_VENTAS);
        File[] archivosVentas = carpetaVentas.listFiles((dir, name) -> name.startsWith("ventas_") && name.endsWith(".txt"));

        if (archivosVentas != null) {
            for (File archivo : archivosVentas) {
                procesarArchivoVentas(archivo, recaudacionVendedores, preciosProductos, cantidadVendidaPorProducto);
            }
        }

        // Guardar la información de vendedores en el archivo CSV
        guardarCSV(recaudacionVendedores, nombresVendedores);

        // Generar el CSV de productos vendidos
        guardarCSVProductosVendidos(cantidadVendidaPorProducto, preciosProductos, nombresProductos);
    }

    /**
     * Carga los nombres de los vendedores desde un archivo y los almacena en un mapa.
     *
     * @return Un mapa que asocia números de documento con los nombres completos de los vendedores.
     */
    private static Map<String, String> cargarNombresVendedores() {
        Map<String, String> nombresVendedores = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_VENDEDORES))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                String numeroDocumento = datos[1];
                String nombreVendedor = datos[2] + " " + datos[3];
                nombresVendedores.put(numeroDocumento, nombreVendedor);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los nombres de los vendedores: " + ARCHIVO_VENDEDORES);
            e.printStackTrace();
        }
        return nombresVendedores;
    }

    /**
     * Carga los precios de los productos desde un archivo y los almacena en un mapa.
     *
     * @return Un mapa que asocia identificadores de producto con sus precios.
     */
    private static Map<String, Double> cargarPreciosProductos() {
        Map<String, Double> preciosProductos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PRODUCTOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                String idProducto = datos[0];
                double precio = Double.parseDouble(datos[2].replace(",", "."));
                preciosProductos.put(idProducto, precio);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los precios de los productos: " + ARCHIVO_PRODUCTOS);
            e.printStackTrace();
        }
        return preciosProductos;
    }

    /**
     * Carga los nombres de los productos desde un archivo y los almacena en un mapa.
     *
     * @return Un mapa que asocia identificadores de producto con sus nombres.
     */
    private static Map<String, String> cargarNombresProductos() {
        Map<String, String> nombresProductos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PRODUCTOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                String idProducto = datos[0];
                String nombreProducto = datos[1];
                nombresProductos.put(idProducto, nombreProducto);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar los nombres de los productos: " + ARCHIVO_PRODUCTOS);
            e.printStackTrace();
        }
        return nombresProductos;
    }

    /**
     * Procesa un archivo de ventas, calcula la recaudación de cada vendedor y la cantidad vendida de cada producto.
     *
     * @param archivo                El archivo de ventas a procesar.
     * @param recaudacionVendedores  Un mapa para almacenar la recaudación de cada vendedor.
     * @param preciosProductos       Un mapa con los precios de los productos.
     * @param cantidadVendidaPorProducto Un mapa para almacenar la cantidad vendida de cada producto.
     */
    private static void procesarArchivoVentas(File archivo, Map<String, Double> recaudacionVendedores, Map<String, Double> preciosProductos, Map<String, Integer> cantidadVendidaPorProducto) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Saltar la línea del encabezado
            String numeroDocumento = archivo.getName().replace("ventas_", "").replace(".txt", "");

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                if (datos.length < 2) {
                    System.err.println("Formato incorrecto en la línea: " + linea);
                    continue;
                }

                String idProducto = datos[0];
                int cantidad;

                try {
                    cantidad = Integer.parseInt(datos[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Cantidad no válida en la línea: " + linea);
                    continue;
                }

                Double precioProducto = preciosProductos.get(idProducto);
                if (precioProducto == null || precioProducto <= 0) {
                    System.err.println("Precio no válido para el producto: " + idProducto);
                    continue;
                }

                double totalRecaudado = cantidad * precioProducto * 1000;
                recaudacionVendedores.merge(numeroDocumento, totalRecaudado, Double::sum);

                // Actualizar la cantidad vendida del producto
                cantidadVendidaPorProducto.merge(idProducto, cantidad, Integer::sum);
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el archivo de ventas: " + archivo.getAbsolutePath());
            e.printStackTrace();
        }
    }

    /**
     * Guarda la recaudación de los vendedores en un archivo CSV.
     *
     * @param recaudacionVendedores Un mapa con la recaudación de cada vendedor.
     * @param nombresVendedores     Un mapa con los nombres de los vendedores.
     */
    private static void guardarCSV(Map<String, Double> recaudacionVendedores, Map<String, String> nombresVendedores) {
        List<String[]> datosCSV = new ArrayList<>();

        for (Map.Entry<String, Double> entry : recaudacionVendedores.entrySet()) {
            String numeroDocumento = entry.getKey();
            String nombreVendedor = nombresVendedores.get(numeroDocumento);
            double totalRecaudado = entry.getValue();
            datosCSV.add(new String[]{nombreVendedor, String.valueOf(totalRecaudado)});
        }

        // Ordenar de mayor a menor según la recaudación
        datosCSV.sort((a, b) -> Double.compare(Double.parseDouble(b[1]), Double.parseDouble(a[1])));

        File reportesDir = new File("Documentos/reportes");
        if (!reportesDir.exists()) {
            reportesDir.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ARCHIVO_CSV_VENDEDORES), "UTF-8"))) {
            bw.write("Nombre;Recaudacion");
            bw.newLine();
            for (String[] datos : datosCSV) {
                bw.write(String.join(";", datos));
                bw.newLine();
            }
            System.out.println("Archivo CSV de vendedores guardado en: " + ARCHIVO_CSV_VENDEDORES);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo CSV: " + ARCHIVO_CSV_VENDEDORES);
            e.printStackTrace();
        }
    }

    /**
     * Guarda la información de los productos vendidos en un archivo CSV.
     *
     * @param cantidadVendidaPorProducto Un mapa con la cantidad vendida de cada producto.
     * @param preciosProductos           Un mapa con los precios de los productos.
     * @param nombresProductos           Un mapa con los nombres de los productos.
     */
    private static void guardarCSVProductosVendidos(Map<String, Integer> cantidadVendidaPorProducto, Map<String, Double> preciosProductos, Map<String, String> nombresProductos) {
        List<String[]> datosCSVProductos = new ArrayList<>();

        // Llenar la lista con los datos de productos
        for (Map.Entry<String, Integer> entry : cantidadVendidaPorProducto.entrySet()) {
            String idProducto = entry.getKey();
            int cantidadVendida = entry.getValue();
            double precioUnitario = preciosProductos.get(idProducto);
            String nombreProducto = nombresProductos.get(idProducto);

            datosCSVProductos.add(new String[]{nombreProducto, String.valueOf(precioUnitario), String.valueOf(cantidadVendida)});
        }

        // Ordenar por cantidad vendida de mayor a menor
        datosCSVProductos.sort((a, b) -> Integer.compare(Integer.parseInt(b[2]), Integer.parseInt(a[2])));

        // Guardar en CSV
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ARCHIVO_CSV_PRODUCTOS), "UTF-8"))) {
            bw.write("NombreProducto;PrecioUnitario;CantidadVendida");
            bw.newLine();
            for (String[] datos : datosCSVProductos) {
                bw.write(String.join(";", datos));
                bw.newLine();
            }
            System.out.println("Archivo CSV de productos vendidos guardado en: " + ARCHIVO_CSV_PRODUCTOS);
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo CSV de productos: " + ARCHIVO_CSV_PRODUCTOS);
            e.printStackTrace();
        }
    }
}





