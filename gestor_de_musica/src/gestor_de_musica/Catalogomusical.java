package gestor_de_musica;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Módulo 1: CatalogoMusical
 *
 * Encapsula una Tabla Hash (HashMap) para almacenar y recuperar canciones.
 *
 * Complejidad de búsqueda:
 *   HashMap internamente usa una función de hash para mapear cada clave (título)
 *   directamente a un "bucket" de memoria. En el caso promedio no hay colisiones,
 *   por lo que tanto get() como put() operan en tiempo O(1) — sin importar cuántas
 *   canciones haya en el catálogo.
 */
public class CatalogoMusical {

    // --- Estructura interna: Tabla Hash ---
    // Clave  → título de la canción (String, en minúsculas para búsqueda insensible a mayúsculas)
    // Valor  → objeto Cancion completo
    private HashMap<String, Cancion> tablaCanciones;

    // Índice secundario por ID, útil para los módulos de Ranking y Reproductor
    // que reciben un objeto Cancion pero necesitan actualizarlo por referencia
    private HashMap<String, Cancion> indicePorId;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public CatalogoMusical() {
        this.tablaCanciones = new HashMap<>();
        this.indicePorId    = new HashMap<>();
    }

    // -------------------------------------------------------------------------
    // Métodos obligatorios
    // -------------------------------------------------------------------------

    /**
     * Inserta una canción en el catálogo.
     *
     * Se usa el TÍTULO en minúsculas como clave principal para que buscarCancion()
     * sea insensible a mayúsculas. También se indexa por ID para integración con
     * los módulos de Ranking y Reproductor.
     *
     * Complejidad: O(1) promedio (operación put() de HashMap).
     *
     * @param c Canción a insertar (no debe ser null).
     */
    public void insertarCancion(Cancion c) {
        if (c == null) {
            System.out.println("[CatalogoMusical] Error: no se puede insertar una canción nula.");
            return;
        }

        String claveTitulo = c.getTitulo().toLowerCase();
        String claveId     = c.getId();

        if (tablaCanciones.containsKey(claveTitulo)) {
            System.out.println("[CatalogoMusical] Advertencia: ya existe una canción con el título \""
                    + c.getTitulo() + "\". Se sobreescribirá.");
        }

        // O(1) — HashMap calcula hash(claveTitulo) y escribe directamente en el bucket
        tablaCanciones.put(claveTitulo, c);
        indicePorId.put(claveId, c);

        System.out.println("[CatalogoMusical] Canción insertada: \"" + c.getTitulo()
                + "\" | ID: " + claveId);
    }

    /**
     * Busca una canción por su título en tiempo O(1).
     *
     * La búsqueda es insensible a mayúsculas/minúsculas.
     *
     * Complejidad: O(1) promedio — HashMap calcula hash(titulo) y accede
     * directamente al bucket sin recorrer toda la tabla.
     *
     * @param titulo Título de la canción a buscar.
     * @return El objeto Cancion si existe, o null si no se encontró.
     */
    public Cancion buscarCancion(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            System.out.println("[CatalogoMusical] Error: el título de búsqueda no puede estar vacío.");
            return null;
        }

        // O(1) — acceso directo por hash
        Cancion encontrada = tablaCanciones.get(titulo.toLowerCase());

        if (encontrada == null) {
            System.out.println("[CatalogoMusical] No se encontró ninguna canción con el título \""
                    + titulo + "\".");
        }

        return encontrada;
    }

    // -------------------------------------------------------------------------
    // Métodos de integración con otros módulos
    // -------------------------------------------------------------------------

    /**
     * Busca una canción por su ID único.
     * Útil para el RankingServicio, que identifica canciones por objeto/referencia.
     *
     * Complejidad: O(1) promedio.
     *
     * @param id ID de la canción.
     * @return El objeto Cancion si existe, o null.
     */
    public Cancion buscarPorId(String id) {
        if (id == null || id.isBlank()) return null;
        return indicePorId.get(id);
    }

    /**
     * Elimina una canción del catálogo por título.
     * Necesario si el ReproductorLogico o el RankingServicio necesitan limpiar
     * canciones obsoletas.
     *
     * Complejidad: O(1) promedio.
     *
     * @param titulo Título de la canción a eliminar.
     * @return true si fue eliminada, false si no existía.
     */
    public boolean eliminarCancion(String titulo) {
        Cancion c = tablaCanciones.remove(titulo.toLowerCase());
        if (c != null) {
            indicePorId.remove(c.getId());
            System.out.println("[CatalogoMusical] Canción \"" + titulo + "\" eliminada.");
            return true;
        }
        System.out.println("[CatalogoMusical] No se encontró \"" + titulo + "\" para eliminar.");
        return false;
    }

    /**
     * Devuelve todas las canciones del catálogo como lista.
     * El RecomendadorConexiones y el RankingServicio pueden usarlo para
     * inicializar sus propias estructuras (grafo y heap).
     *
     * Complejidad: O(n) — recorre todos los valores del HashMap.
     *
     * @return Lista con todas las canciones almacenadas.
     */
    public List<Cancion> obtenerTodas() {
        return new ArrayList<>(tablaCanciones.values());
    }

    /**
     * Filtra canciones por género.
     * Útil para el RecomendadorConexiones al buscar artistas del mismo género.
     *
     * Complejidad: O(n).
     *
     * @param genero Género musical a filtrar.
     * @return Lista de canciones del género indicado.
     */
    public List<Cancion> buscarPorGenero(String genero) {
        List<Cancion> resultado = new ArrayList<>();
        for (Cancion c : tablaCanciones.values()) {
            if (c.getGenero().equalsIgnoreCase(genero)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    /**
     * Devuelve el número de canciones en el catálogo.
     *
     * @return Tamaño del catálogo.
     */
    public int size() {
        return tablaCanciones.size();
    }

    /**
     * Muestra en consola todas las canciones almacenadas.
     * Útil para depuración y para el menú principal en Gestor_de_musica.java.
     */
    public void mostrarCatalogo() {
        if (tablaCanciones.isEmpty()) {
            System.out.println("[CatalogoMusical] El catálogo está vacío.");
            return;
        }
        System.out.println("\n===== CATÁLOGO MUSICAL (" + tablaCanciones.size() + " canciones) =====");
        for (Cancion c : tablaCanciones.values()) {
            System.out.println("  • [" + c.getId() + "] " + c.getTitulo()
                    + " — " + c.getArtista()
                    + " | Género: " + c.getGenero()
                    + " | Reproducciones: " + c.getReproducciones());
        }
        System.out.println("=================================================\n");
    }
}
