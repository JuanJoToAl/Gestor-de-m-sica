package gestor_de_musica;

// CAMBIO: ya no se importa java.util.HashMap ni java.util.Map
// Ahora se usa MiHashMap, nuestra propia implementacion
public class CatalogoMusical {

    // CAMBIO: Map<String, Cancion> pasa a MiHashMap<String, Cancion>
    private MiHashMap<String, Cancion> tablaHashCanciones;

    public CatalogoMusical() {
        // CAMBIO: new HashMap<>() pasa a new MiHashMap<>()
        this.tablaHashCanciones = new MiHashMap<>();
    }

    // Agrega una cancion al catalogo
    public void insertarCancion(Cancion cancion) {
        if (cancion != null && cancion.getTitulo() != null) {
            String clave = cancion.getTitulo().toLowerCase();
            // CAMBIO: .put() se mantiene igual — MiHashMap tiene el mismo metodo
            tablaHashCanciones.put(clave, cancion);
        }
    }

    // Imprime todas las canciones del catalogo
    public void mostrarCatalogo() {
        System.out.println("\n--- CATALOGO DE MUSICA DISPONIBLE ---");
        if (tablaHashCanciones.isEmpty()) {
            System.out.println("El catalogo esta vacio en este momento.");
        } else {
            // CAMBIO: .values() ahora retorna MiLista<Cancion> en vez de Collection<Cancion>
            // El for-each funciona igual porque MiLista implementa Iterable
            for (Cancion cancion : tablaHashCanciones.values()) {
                System.out.println("🎵 " + cancion.toString());
            }
        }
    }

    // Busca una cancion por su titulo — O(1) promedio gracias al hash
    public Cancion buscarCancion(String tituloBuscar) {
        if (tituloBuscar == null || tituloBuscar.trim().isEmpty()) {
            return null;
        }
        // CAMBIO: .get() se mantiene igual — MiHashMap tiene el mismo metodo
        return tablaHashCanciones.get(tituloBuscar.toLowerCase());
    }
}
