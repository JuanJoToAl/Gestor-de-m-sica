package gestor_de_musica;

import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

// Módulo de Estadísticas (RankingServicio)
// Gestiona el ordenamiento dinámico de popularidad en tiempo real mediante un Heap de máximos
public class RankingServicio {

    // Montículo de máximos para ordenar las canciones por reproducciones
    private PriorityQueue<Cancion> maxHeap;

    // Inicializa el servicio configurando la cola de prioridad con comparador inverso
    public RankingServicio() {
        // Se usa un comparador inverso basado en el número de reproducciones
        this.maxHeap = new PriorityQueue<>((c1, c2) -> Integer.compare(c2.getReproducciones(), c1.getReproducciones()));
    }

    // Incrementa el contador de reproducciones y reorganiza el heap
    public void actualizarReproduccion(Cancion c) {
        // Se remueve la canción antes de modificarla para no corromper el heap
        maxHeap.remove(c);
        
        // Se incrementa el contador de reproducciones de la canción
        c.incrementarReproducciones();
        
        // Se reinserta la canción actualizada en el heap para reordenarla
        maxHeap.add(c);
    }

    // Retorna las 5 canciones más reproducidas en orden descendente
    public List<Cancion> obtenerTop5() {
        List<Cancion> top5 = new ArrayList<>();
        
        // Se crea una copia temporal del heap para no destruir la estructura original
        PriorityQueue<Cancion> tempHeap = new PriorityQueue<>(maxHeap);
        
        // Se extraen hasta 5 elementos del heap temporal de manera ordenada
        for (int i = 0; i < 5 && !tempHeap.isEmpty(); i++) {
            top5.add(tempHeap.poll());
        }
        
        return top5;
    }

    // Devuelve el heap original (útil para propósitos de testeo)
    public PriorityQueue<Cancion> getHeap() {
        return this.maxHeap;
    }
}