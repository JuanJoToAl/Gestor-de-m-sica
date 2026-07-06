package gestor_de_musica;

// CAMBIO: eliminados los imports de PriorityQueue, List y ArrayList de Java
// Ahora se usan MiMaxHeap y MiLista, nuestras propias implementaciones
public class RankingServicio {

    // CAMBIO: PriorityQueue<Cancion> → MiMaxHeap
    private MiMaxHeap maxHeap;

    public RankingServicio() {
        // CAMBIO: ya no se necesita comparador — MiMaxHeap compara por reproducciones internamente
        this.maxHeap = new MiMaxHeap();
    }

    // Incrementa el contador de reproducciones y reorganiza el heap
    public void actualizarReproduccion(Cancion c) {
        // CAMBIO: maxHeap.remove(c) → maxHeap.eliminar(c)
        maxHeap.eliminar(c);

        c.incrementarReproducciones();

        // CAMBIO: maxHeap.add(c) → maxHeap.insertar(c)
        maxHeap.insertar(c);
    }

    // Retorna las 5 canciones mas reproducidas en orden descendente
    // CAMBIO: retorna MiLista<Cancion> en vez de List<Cancion>
    public MiLista<Cancion> obtenerTop5() {
        MiLista<Cancion> top5 = new MiLista<>();

        // CAMBIO: se usa el constructor de copia de MiMaxHeap en vez de new PriorityQueue<>(maxHeap)
        MiMaxHeap tempHeap = maxHeap.copiar();

        // Extraemos hasta 5 maximos del heap temporal sin tocar el original
        for (int i = 0; i < 5 && !tempHeap.isEmpty(); i++) {
            // CAMBIO: tempHeap.poll() → tempHeap.extraerMax()
            // CAMBIO: top5.add() → top5.agregar()
            top5.agregar(tempHeap.extraerMax());
        }

        return top5;
    }

    // CAMBIO: retorna MiMaxHeap en vez de PriorityQueue<Cancion>
    public MiMaxHeap getHeap() {
        return this.maxHeap;
    }
}
