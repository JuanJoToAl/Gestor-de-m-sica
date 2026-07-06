package gestor_de_musica;

// Conjunto propio para Strings — reemplaza java.util.HashSet
// Internamente usa MiHashMap, guardando true como valor para cada elemento unico
public class MiConjunto {

    private MiHashMap<String, Boolean> mapa;

    public MiConjunto() {
        mapa = new MiHashMap<>();
    }

    // Agrega un elemento al conjunto — O(1) promedio
    public void agregar(String elemento) {
        mapa.put(elemento, true);
    }

    // Verifica si el elemento ya esta en el conjunto — O(1) promedio
    public boolean contiene(String elemento) {
        return mapa.containsKey(elemento);
    }
}
