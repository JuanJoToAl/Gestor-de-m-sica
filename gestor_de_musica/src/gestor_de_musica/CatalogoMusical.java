package gestor_de_musica;

import java.util.HashMap;
import java.util.Map;

public class CatalogoMusical {
    
    // Implementación de la Tabla Hash solicitada en tu menú
    // La llave (Key) será el título de la canción en minúsculas para búsquedas rápidas (O(1))
    // El valor (Value) será el objeto Cancion completo
    private Map<String, Cancion> tablaHashCanciones;

    // Constructor
    public CatalogoMusical() {
        this.tablaHashCanciones = new HashMap<>();
    }

    // Método para agregar una canción al catálogo
    public void insertarCancion(Cancion cancion) {
        if (cancion != null && cancion.getTitulo() != null) {
            // Convertimos el título a minúsculas para que sea fácil de buscar después
            String clave = cancion.getTitulo().toLowerCase();
            tablaHashCanciones.put(clave, cancion);
        }
    }

    // Método para imprimir todas las canciones
    public void mostrarCatalogo() {
        System.out.println("\n--- CATÁLOGO DE MÚSICA DISPONIBLE ---");
        if (tablaHashCanciones.isEmpty()) {
            System.out.println("El catálogo está vacío en este momento.");
        } else {
            // Recorremos los valores del HashMap
            for (Cancion cancion : tablaHashCanciones.values()) {
                System.out.println("🎵 " + cancion.toString());
            }
        }
    }

    // Método para buscar una canción específica por su título
    public Cancion buscarCancion(String tituloBuscar) {
        if (tituloBuscar == null || tituloBuscar.trim().isEmpty()) {
            return null;
        }
        // Buscamos la llave en el mapa (la convertimos a minúsculas para que coincida)
        return tablaHashCanciones.get(tituloBuscar.toLowerCase());
    }
}