package gestor_de_musica;

// HashMap propio usando arreglo de listas enlazadas (encadenamiento para colisiones)
// Reemplaza java.util.HashMap
public class MiHashMap<K, V> {

    private static final int CAPACIDAD = 16;

    // Nodo interno que guarda clave, valor y referencia al siguiente (para colisiones)
    private class NodoHash {
        K clave;
        V valor;
        NodoHash siguiente;

        NodoHash(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    @SuppressWarnings("unchecked")
    private Object[] tabla = new Object[CAPACIDAD];
    private int size;

    public MiHashMap() {
        size = 0;
    }

    // Calcula el indice del bucket usando el hashCode de la clave — O(1)
    private int calcularIndice(K clave) {
        return Math.abs(clave.hashCode()) % CAPACIDAD;
    }

    // Inserta o actualiza un par clave-valor — O(1) promedio
    public void put(K clave, V valor) {
        int indice = calcularIndice(clave);
        NodoHash actual = (NodoHash) tabla[indice];

        // Si la clave ya existe, actualiza su valor
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }

        // Si no existe, inserta al inicio del bucket (encadenamiento)
        NodoHash nuevo = new NodoHash(clave, valor);
        nuevo.siguiente = (NodoHash) tabla[indice];
        tabla[indice] = nuevo;
        size++;
    }

    // Busca y retorna el valor asociado a la clave — O(1) promedio
    public V get(K clave) {
        int indice = calcularIndice(clave);
        NodoHash actual = (NodoHash) tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    // Verifica si una clave existe en el mapa — O(1) promedio
    public boolean containsKey(K clave) {
        return get(clave) != null;
    }

    // Solo inserta si la clave no existe — O(1) promedio
    public void putIfAbsent(K clave, V valor) {
        if (!containsKey(clave)) {
            put(clave, valor);
        }
    }

    // Retorna el valor de la clave, o el valor por defecto si no existe — O(1) promedio
    public V getOrDefault(K clave, V valorPorDefecto) {
        V resultado = get(clave);
        return resultado != null ? resultado : valorPorDefecto;
    }

    // Retorna todos los valores almacenados como MiLista — O(n)
    public MiLista<V> values() {
        MiLista<V> lista = new MiLista<>();
        for (int i = 0; i < CAPACIDAD; i++) {
            NodoHash actual = (NodoHash) tabla[i];
            while (actual != null) {
                lista.agregar(actual.valor);
                actual = actual.siguiente;
            }
        }
        return lista;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
