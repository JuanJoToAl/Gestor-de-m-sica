package gestor_de_musica;

import javazoom.jl.player.Player;
import java.io.FileInputStream;

public class ReproductorLogico {
    // Atributos de la Lista Doble (Playlist)
    private NodoDoble cabeza;
    private NodoDoble cola;
    private NodoDoble actual;
    
    // Atributos de la Cola Simple (Fila de espera)
    private NodoSimple frente;
    private NodoSimple fin;

    private Player reproductorMP3; 
    private Thread hiloMusica;
    
    // Constructor
    public ReproductorLogico() {
        this.cabeza = null;
        this.cola = null;
        this.actual = null;
        this.frente = null;
        this.fin = null;
    }

    // Agregar a la Playlist (Lista Doble)
    public void agregarAPlaylist(Cancion cancion) {
        NodoDoble nuevoNodo = new NodoDoble(cancion);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
            actual = nuevoNodo; 
        } else {
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo;
        }
    }

    // Agregar a la Fila de Espera (Cola FIFO)
    public void agregarAColaEspera(Cancion cancion) {
        NodoSimple nuevoNodo = new NodoSimple(cancion);
        if (frente == null) {
            frente = nuevoNodo;
            fin = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
            fin = nuevoNodo;
        }
    }

// Avanzar y reproducir la siguiente canción
    public Cancion reproducirSiguiente() {
        Cancion cancionAProcesar = null;

        // Priorizar siempre la fila de espera (Cola FIFO)
        if (frente != null) {
            cancionAProcesar = frente.cancion;
            frente = frente.siguiente;
            if (frente == null) {
                fin = null;
            }
        } 
        // Si la cola está vacía, avanza en la playlist normal (Lista Doble)
        else if (actual != null) {
            cancionAProcesar = actual.cancion;
            actual = actual.siguiente;
        }
        
        // Si encontramos una canción se manda a sonar
        if (cancionAProcesar != null) {
            reproducirAudioReal(cancionAProcesar.getRutaArchivo());
        }
        
        return cancionAProcesar; 
    }

    // Retroceder a la canción anterior
    public Cancion reproducirAnterior() {
        if (actual != null && actual.anterior != null) {
            actual = actual.anterior;
            Cancion cancionAnterior = actual.cancion;
            
            // >>> MANDAR A SONAR LA CANCIÓN ANTERIOR FÍSICAMENTE
            reproducirAudioReal(cancionAnterior.getRutaArchivo());
            
            return cancionAnterior; 
        }
        return null; 
    }

    // Obtener la canción seleccionada actualmente
    public Cancion getCancionActual() {
        if (actual != null) {
            return actual.cancion; 
        }
        return null;
    }
    // MÉTODO PARA REPRODUCIR EL AUDIO EN SEGUNDO PLANO
    public void reproducirAudioReal(String ruta) {
        detenerMusica(); // Apaga la canción anterior si había una sonando

        if (ruta == null || ruta.isEmpty()) {
            System.out.println("⚠️ Esta canción no tiene una ruta de archivo MP3 configurada.");
            return;
        }

        hiloMusica = new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(ruta);
                reproductorMP3 = new Player(fis);
                reproductorMP3.play(); // Aquí se queda sonando la música
            } catch (Exception e) {
                System.out.println("⚠️ Error físico al reproducir: " + e.getMessage());
            }
        });

        hiloMusica.start(); // Arranca el hilo en paralelo
    }

    // MÉTODO PARA DETENER LA MÚSICA DE GOLPE
    public void detenerMusica() {
        if (reproductorMP3 != null) {
            reproductorMP3.close();
            reproductorMP3 = null;
        }
        if (hiloMusica != null && hiloMusica.isAlive()) {
            hiloMusica.interrupt();
        }
    }
}