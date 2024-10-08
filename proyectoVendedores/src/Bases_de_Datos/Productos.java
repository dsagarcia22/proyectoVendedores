package Bases_de_Datos; // Asegúrate de que el paquete sea correcto

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase que proporciona una lista de productos de la canasta familiar, con precios generados aleatoriamente.
 */
public class Productos {

    // Objeto Random para generar números aleatorios
    private static final Random RANDOM = new Random();

    // Lista de nombres de productos de la canasta familiar
    private static final String[] NOMBRES_PRODUCTOS = {
        "Arroz", "Frijoles", "Azúcar", "Aceite",
        "Leche", "Pan", "Huevos", "Pollo",
        "Carne de res", "Pasta", "Tomates",
        "Cebolla", "Zanahoria", "Manzanas", "Naranjas"
    };

    // Precio máximo y mínimo de los productos
    private static final int MAX_PRECIO = 50000; // Precio máximo de 50,000
    private static final int MIN_PRECIO = 1000;  // Precio mínimo de 1,000

    /**
     * Genera una lista de productos, cada uno con un nombre y un precio aleatorio.
     *
     * @return Una lista de productos con nombres y precios aleatorios.
     */
    public static List<Producto> getListaProductos() {
        List<Producto> listaProductos = new ArrayList<>();
        // Iterar sobre cada nombre de producto y crear un Producto con un precio aleatorio
        for (String nombre : NOMBRES_PRODUCTOS) {
            int precio = obtenerPrecioProductoAleatorio();
            listaProductos.add(new Producto(nombre, precio));
        }
        return listaProductos;
    }

    /**
     * Obtiene un nombre de producto de la lista de productos de manera aleatoria.
     *
     * @return Un nombre de producto aleatorio.
     */
    public static String obtenerNombreProductoAleatorio() {
        return NOMBRES_PRODUCTOS[RANDOM.nextInt(NOMBRES_PRODUCTOS.length)];
    }

    /**
     * Genera un precio aleatorio para un producto dentro del rango especificado.
     *
     * @return Un precio aleatorio entre MIN_PRECIO y MAX_PRECIO.
     */
    public static int obtenerPrecioProductoAleatorio() {
        // Genera un precio entre el precio mínimo y el precio máximo
        return RANDOM.nextInt(MAX_PRECIO - MIN_PRECIO + 1) + MIN_PRECIO;
    }

    /**
     * Clase interna que representa un producto con un nombre y un precio.
     */
    public static class Producto {
        private String nombre; // Nombre del producto
        private int precio;    // Precio del producto

        /**
         * Constructor de la clase Producto.
         *
         * @param nombre El nombre del producto.
         * @param precio El precio del producto.
         */
        public Producto(String nombre, int precio) {
            this.nombre = nombre;
            this.precio = precio;
        }

        /**
         * Obtiene el nombre del producto.
         *
         * @return El nombre del producto.
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Obtiene el precio del producto.
         *
         * @return El precio del producto.
         */
        public int getPrecio() {
            return precio;
        }
    }
}



