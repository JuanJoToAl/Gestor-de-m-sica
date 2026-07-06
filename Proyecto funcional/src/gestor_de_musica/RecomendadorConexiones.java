package gestor_de_musica;

// CAMBIO: eliminados los imports de ArrayList, HashMap, List y Map de Java
// Ahora se usan MiHashMap y MiLista, nuestras propias implementaciones
public class RecomendadorConexiones {

    // CAMBIO: Map<Artista, List<Artista>> → MiHashMap<Artista, MiLista<Artista>>
    // El grafo sigue siendo una lista de adyacencia, ahora con estructuras propias
    private MiHashMap<Artista, MiLista<Artista>> grafoArtistas;

    public RecomendadorConexiones() {
        // CAMBIO: new HashMap<>() → new MiHashMap<>()
        this.grafoArtistas = new MiHashMap<>();
    }

    // Crea una conexion bidireccional entre dos artistas del mismo genero
    public void conectarArtistas(Artista a1, Artista a2) {
        // CAMBIO: new ArrayList<>() → new MiLista<>()
        grafoArtistas.putIfAbsent(a1, new MiLista<>());
        grafoArtistas.putIfAbsent(a2, new MiLista<>());

        // CAMBIO: .contains() → .contiene()
        if (!grafoArtistas.get(a1).contiene(a2)) {
            // CAMBIO: .add() → .agregar()
            grafoArtistas.get(a1).agregar(a2);
        }
        if (!grafoArtistas.get(a2).contiene(a1)) {
            grafoArtistas.get(a2).agregar(a1);
        }
    }

    // Retorna los artistas conectados directamente al artista dado
    // CAMBIO: retorna MiLista<Artista> en vez de List<Artista>
    public MiLista<Artista> recomendarSimilares(Artista a) {
        // CAMBIO: new ArrayList<>() como default → new MiLista<>()
        return grafoArtistas.getOrDefault(a, new MiLista<>());
    }
}
