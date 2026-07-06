package gestor_de_musica;

// Max-Heap propio para objetos Cancion — reemplaza PriorityQueue de Java
// La cancion con mas reproducciones siempre queda en la raiz (indice 0)
// Usa un arreglo con la propiedad: hijo izquierdo = 2i+1, hijo derecho = 2i+2, padre = (i-1)/2
public class MiMaxHeap {

    private Cancion[] datos;
    private int size;
    private static final int CAPACIDAD_INICIAL = 100;

    public MiMaxHeap() {
        datos = new Cancion[CAPACIDAD_INICIAL];
        size = 0;
    }

    // Constructor de copia — usado para obtenerTop5() sin destruir el heap original
    public MiMaxHeap(MiMaxHeap otro) {
        this.datos = new Cancion[otro.datos.length];
        this.size = otro.size;
        System.arraycopy(otro.datos, 0, this.datos, 0, otro.size);
    }

    // Calcula el indice del padre de un nodo
    private int padre(int i) { return (i - 1) / 2; }

    // Calcula el indice del hijo izquierdo de un nodo
    private int hijoIzq(int i) { return 2 * i + 1; }

    // Calcula el indice del hijo derecho de un nodo
    private int hijoDer(int i) { return 2 * i + 2; }

    // Intercambia dos elementos del arreglo
    private void intercambiar(int i, int j) {
        Cancion temp = datos[i];
        datos[i] = datos[j];
        datos[j] = temp;
    }

    // Inserta una cancion y la sube hasta su posicion correcta — O(log n)
    public void insertar(Cancion c) {
        if (size >= datos.length) {
            Cancion[] nuevo = new Cancion[datos.length * 2];
            System.arraycopy(datos, 0, nuevo, 0, size);
            datos = nuevo;
        }
        datos[size] = c;
        size++;
        heapifyArriba(size - 1);
    }

    // Sube el nodo mientras sea mayor que su padre — O(log n)
    private void heapifyArriba(int i) {
        while (i > 0 && datos[i].getReproducciones() > datos[padre(i)].getReproducciones()) {
            intercambiar(i, padre(i));
            i = padre(i);
        }
    }

    // Extrae la cancion con mas reproducciones (la raiz) — O(log n)
    public Cancion extraerMax() {
        if (size == 0) return null;
        Cancion max = datos[0];
        datos[0] = datos[size - 1];
        datos[size - 1] = null;
        size--;
        heapifyAbajo(0);
        return max;
    }

    // Baja el nodo mientras sea menor que alguno de sus hijos — O(log n)
    private void heapifyAbajo(int i) {
        int mayor = i;
        int izq = hijoIzq(i);
        int der = hijoDer(i);

        if (izq < size && datos[izq].getReproducciones() > datos[mayor].getReproducciones()) {
            mayor = izq;
        }
        if (der < size && datos[der].getReproducciones() > datos[mayor].getReproducciones()) {
            mayor = der;
        }

        if (mayor != i) {
            intercambiar(i, mayor);
            heapifyAbajo(mayor);
        }
    }

    // Elimina una cancion especifica del heap — O(n) para encontrarla + O(log n) para reordenar
    public void eliminar(Cancion c) {
        int indice = -1;
        for (int i = 0; i < size; i++) {
            if (datos[i] == c) {
                indice = i;
                break;
            }
        }
        if (indice == -1) return;

        // Reemplaza con el ultimo elemento y reordena
        datos[indice] = datos[size - 1];
        datos[size - 1] = null;
        size--;

        if (indice < size) {
            heapifyArriba(indice);
            heapifyAbajo(indice);
        }
    }

    // Retorna una copia del heap — para obtener el Top5 sin destruir el original
    public MiMaxHeap copiar() {
        return new MiMaxHeap(this);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
