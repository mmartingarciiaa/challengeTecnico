package manager;
import java.util.Arrays;

// Clase que representa un sistema de archivos simple con directorios y archivos
// Permite navegar entre directorios, crear archivos y directorios, listar contenidos y eliminar entradas
public class FileSystem {
    private final Directorio raiz;
    private Directorio directorioActual;

    public FileSystem() {
        // El directorio raíz se llama "/" y no tiene padre
        this.raiz = new Directorio("/", null);
        this.directorioActual = this.raiz;
    }

    public Directorio getRaiz() {
        return raiz;
    }

    public Directorio getDirectorioActual() {
        return directorioActual;
    }

    /**
     * Cambia el directorio actual al especificado por dirName.
     * Si dirName es "..", se mueve al directorio padre.
     * Si dirName es un nombre de directorio, busca en el directorio actual.
     * Si no se encuentra, imprime un mensaje de error y retorna false.
     * @param dirName El nombre del directorio al que cambiar.
     * @return true si el cambio fue exitoso, false si no se encontró el directorio o si no se puede subir más.
     */
    public boolean cd(String dirName) {
        if (dirName == null || dirName.isEmpty()) {
            System.out.println("Error: El nombre del directorio no puede ser nulo o vacío.");
            return false;
        }

        // Direcciona al padre
        if (dirName.equals("..")) {
            if (directorioActual.getPadre() != null) {
                directorioActual = directorioActual.getPadre();
                return true;
            } else {
                return false;
            }
        }

        Directorio destino = manejarRuta(dirName);

        if (destino != null) {
            this.directorioActual = destino;
            return true;
        } else {
            System.out.println("Error: Directorio '" + dirName + "' no encontrado.");
            return false;
        }
    }

    /**
     * Crea un nuevo archivo con el nombre especificado en el directorio actual o el directorio especificado si se pasa una ruta.
     * Si el archivo ya existe, imprime un mensaje de error y retorna false.
     * @param fileName El nombre del archivo a crear.
     * @return true si el archivo fue creado exitosamente, false si ya existe o si el nombre es inválido.
     */
    public boolean touch (String fileName){
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Error: El nombre del archivo no puede ser nulo o vacío.");
            return false;
        }

        // En caso de que el nombre del archivo contenga una ruta, separo el nombre del archivo y la ruta del directorio
        String[] partesRuta = fileName.split("/");
        String nombreArchivo = partesRuta[partesRuta.length - 1];
        String rutaDirectorio = "";
        if (partesRuta.length > 1) {
            rutaDirectorio = String.join("/", Arrays.copyOfRange(partesRuta, 0, partesRuta.length - 1));
        }

        Directorio dirDestino;
        if (rutaDirectorio.isEmpty()) { // Si no hay ruta, es en el directorio actual
            dirDestino = directorioActual;
        } else {
            dirDestino = manejarRuta(rutaDirectorio);
        }

        if (dirDestino == null) {
            System.out.println("Error: La ruta del directorio para '" + fileName + "' no existe.");
            return false;
        }

        if (dirDestino.getHijo(nombreArchivo) != null) {
            System.out.println("Error: Ya existe un archivo o directorio con el nombre '" + nombreArchivo + "' en este directorio.");
            return false;
        }

        Archivo nuevoArchivo = new Archivo(nombreArchivo, dirDestino);
        dirDestino.addHijo(nuevoArchivo);        
        return true;
    }

    /**
     * Lista los archivos y carpetas dentro del directorio actual.
     */
    public void ls() {
        if (directorioActual.getHijos().isEmpty()) {
            System.out.println("El directorio está vacío.");
            return;
        }
        for (FileSystemBase hijo : directorioActual.getHijos().values()) {
            System.out.println(hijo.getNombre() + (hijo instanceof Directorio ? "/" : ""));
        }
    }

    /**
     * Crea un nuevo directorio con el nombre especificado en el directorio actual o en el directorio especificado si se pasa una ruta.
     * Si el directorio ya existe, imprime un mensaje de error y retorna false.
     * @param dirName El nombre del directorio a crear.
     * @return true si el directorio fue creado exitosamente, false si ya existe o si el nombre es inválido.
     */
    public boolean mkdir(String dirName) {
        if (dirName == null || dirName.isEmpty()) {
            System.out.println("Error: El nombre del directorio no puede ser nulo o vacío.");
            return false;
        }

        String[] partesRuta = dirName.split("/");
        String nombreDirectorio = partesRuta[partesRuta.length - 1];
        String rutaDirectorioPadre = "";
        if (partesRuta.length > 1) {
            rutaDirectorioPadre = String.join("/", Arrays.copyOfRange(partesRuta, 0, partesRuta.length - 1));
        }

        Directorio dirPadre;
        if (rutaDirectorioPadre.isEmpty()) { // Si no hay ruta, es en el directorio actual
            dirPadre = directorioActual;
        } else {
            dirPadre = manejarRuta(rutaDirectorioPadre);
        }

        if (dirPadre == null) {
            System.out.println("Error: La ruta del directorio padre para '" + dirName + "' no existe.");
            return false;
        }

        if (dirPadre.getHijo(nombreDirectorio) != null) {
            System.out.println("Error: Ya existe un archivo o directorio con el nombre '" + nombreDirectorio + "' en este directorio.");
            return false;
        }

        Directorio nuevoDirectorio = new Directorio(nombreDirectorio, dirPadre);
        dirPadre.addHijo(nuevoDirectorio);
        return true;
    }

    /**
     * Imprime la ruta completa del directorio actual. [cite: 16]
     */
    public void pwd() {
        System.out.println(directorioActual.getRutaCompleta());
    }

    /*
     * Elimina un archivo o directorio especificado por su nombre.
     * Si el nombre es nulo o vacío, imprime un mensaje de error y retorna false
     * Si el directorio no esta vacio no se lo puede eliminar
     */
    public boolean rm (String name) {
        if (name == null || name.isEmpty()) {
            System.out.println("Error: El nombre no puede ser nulo o vacío.");
            return false;
        }

        String[] partesRuta = name.split("/");
        String nombreAEliminar = partesRuta[partesRuta.length - 1];
        String rutaDirectorioPadre = "";
        if (partesRuta.length > 1) {
            rutaDirectorioPadre = String.join("/", Arrays.copyOfRange(partesRuta, 0, partesRuta.length - 1));
        }

        Directorio dirPadre;
        if (rutaDirectorioPadre.isEmpty()) { // Si no hay ruta, es en el directorio actual
            dirPadre = directorioActual;
        } else {
            dirPadre = manejarRuta(rutaDirectorioPadre);
        }

        if (dirPadre == null) {
            System.out.println("Error: La ruta del directorio padre para '" + name + "' no existe.");
            return false;
        }

        FileSystemBase entrada = dirPadre.getHijo(nombreAEliminar);
        if (entrada == null) {
            System.out.println("Error: '" + name + "' no encontrado.");
            return false;
        }

        
        if (entrada instanceof Directorio dirAEliminar) {
            if (!dirAEliminar.getHijos().isEmpty()) {
                System.out.println("Error: El directorio '" + nombreAEliminar + "' no está vacío y no puede ser eliminado.");
                return false;
            }
        }

        dirPadre.removeHijo(nombreAEliminar);
        return true;
    }
    /**
     * Resuelve una ruta (absoluta o relativa) y devuelve el Directorio correspondiente.
     * Este es el método clave para el bonus de "Rutas enteras". 
     * @param ruta La ruta a resolver (ej. "/home/user/docs", "files/images", "../..").
     * @return El objeto Directorio si la ruta es válida, o null si no se encuentra.
     */
    private Directorio manejarRuta(String ruta) {
        Directorio inicio = ruta.startsWith("/") ? raiz : directorioActual;
        String[] partes = ruta.startsWith("/") ? ruta.substring(1).split("/") : ruta.split("/");

        Directorio actual = inicio;

        for (String parte : partes) {
            if (parte.isEmpty()) continue; // Manejar barras dobles o rutas que terminan en barra
            if (actual == null) return null; // Si en algún punto el directorio actual se vuelve nulo, la ruta es inválida

            switch (parte) {
                case ".." -> {
                    if (actual.getPadre() != null) {
                        actual = actual.getPadre();
                    } else if (actual.equals(raiz)) {
                        // Si estamos en la raíz y la parte es "..", nos quedamos en la raíz
                        actual = raiz;
                    } else {
                        // No hay padre para ir
                        return null;
                    }
                }
                case "." -> {
                    // "."" significa el directorio actual, no hacemos nada
                }
                default -> {
                    FileSystemBase siguiente = actual.getHijo(parte);
                    if (siguiente instanceof Directorio dir) {
                        actual = dir;
                    } else {
                        // La parte no es un directorio o no existe
                        return null;
                    }
                }
            }
        }
        return actual;
    }
}
