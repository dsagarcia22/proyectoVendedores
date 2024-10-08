package Bases_de_Datos;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Proporciona métodos utilitarios para generar datos aleatorios de vendedores.
 */
public class Nombres_Vendedores {

    // Objeto Random para generar números aleatorios
    public static final Random RANDOM = new Random();

    // Tipos de documentos disponibles en español
    private static final String[] TIPOS_DOCUMENTO = { "CC", "PP", "CE" };
    
    // Nombres y apellidos latinos disponibles
    private static final String[] NOMBRES = { "Juan", "Ana", "Carlos", "Maria", "Luis", "Sofia", "Diego", "Valentina", "Javier", "Camila" };
    private static final String[] APELLIDOS = { "Gomez", "Rodriguez", "Martinez", "Lopez", "Hernandez", "Perez", "Sanchez", "Torres", "Gonzalez", "Ramirez" };

    // Conjunto para almacenar combinaciones de nombre y apellido que ya se han usado
    private static Set<String> nombresUsados = new HashSet<>();

    /**
     * Obtiene un tipo de documento aleatorio de la lista TIPOS_DOCUMENTO.
     *
     * @return Un tipo de documento aleatorio (por ejemplo, "CC", "PP", "CE").
     */
    public static String obtenerTipoDocumentoAleatorio() {
        return TIPOS_DOCUMENTO[RANDOM.nextInt(TIPOS_DOCUMENTO.length)];
    }

    /**
     * Genera un número de documento aleatorio de 8 dígitos, como si fuera un ID.
     *
     * @return Un número de documento aleatorio en formato String con 8 dígitos.
     */
    public static String generarNumeroDocumentoAleatorio() {
        return String.format("%08d", RANDOM.nextInt(100000000));
    }

    /**
     * Obtiene un nombre aleatorio de la lista NOMBRES.
     *
     * @return Un nombre aleatorio.
     */
    public static String obtenerNombreAleatorio() {
        return NOMBRES[RANDOM.nextInt(NOMBRES.length)];
    }

    /**
     * Obtiene un apellido aleatorio de la lista APELLIDOS.
     *
     * @return Un apellido aleatorio.
     */
    public static String obtenerApellidoAleatorio() {
        return APELLIDOS[RANDOM.nextInt(APELLIDOS.length)];
    }

    /**
     * Genera una combinación única de nombre y apellido que no haya sido utilizada antes.
     *
     * @return Un arreglo con un nombre y un apellido, ambos únicos.
     */
    public static String[] generarNombreUnico() {
        String nombre, apellido;
        String combinacionNombre;

        // Genera un nombre y un apellido aleatorios hasta encontrar una combinación única
        do {
            nombre = obtenerNombreAleatorio();
            apellido = obtenerApellidoAleatorio();
            combinacionNombre = nombre + " " + apellido;
        } while (nombresUsados.contains(combinacionNombre));
        
        // Añadir la combinación generada al conjunto de nombres usados para evitar duplicados
        nombresUsados.add(combinacionNombre);
        return new String[] { nombre, apellido };
    }
}


