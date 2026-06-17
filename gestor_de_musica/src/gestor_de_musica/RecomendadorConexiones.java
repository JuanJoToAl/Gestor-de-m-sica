package gestor_de_musica; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecomendadorConexiones {
    
    // Grafo representado por un Map (Lista de adyacencia)
    private Map<Artista, List<Artista>> grafoArtistas;

    public RecomendadorConexiones() {
        this.grafoArtistas = new HashMap<>();
    }

    /**
     * Crea una relación de similitud/colaboración bidireccional entre ambos artistas.
     */
    public void conectarArtistas(Artista a1, Artista a2) {
        grafoArtistas.putIfAbsent(a1, new ArrayList<>());
        grafoArtistas.putIfAbsent(a2, new ArrayList<>());

        if (!grafoArtistas.get(a1).contains(a2)) {
            grafoArtistas.get(a1).add(a2);
        }
        if (!grafoArtistas.get(a2).contains(a1)) {
            grafoArtistas.get(a2).add(a1);
        }
    }

    /**
     * Retorna la lista de artistas directamente conectados al nodo del artista actual 
     */
    public List<Artista> recomendarSimilares(Artista a) {
        return new ArrayList<>(grafoArtistas.getOrDefault(a, new ArrayList<>()));
    }
}