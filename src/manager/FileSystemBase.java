package manager;
import java.util.Objects;

// Clase base para todos los elementos del sistema de archivos
// Contiene propiedades comunes como nombre y padre, y métodos para obtener la ruta completa
public abstract class FileSystemBase {
    private String nombre;
    private Directorio padre;

    public FileSystemBase(String nombre, Directorio padre) {
        this.setNombre(nombre);
        this.setPadre(padre);
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setPadre(Directorio padre) {
        this.padre = padre;
    }

    public Directorio getPadre() {
        return padre;
    }

    // Método para obtener la ruta completa del elemento en el sistema de archivos
    public String getRutaCompleta() {
        if (padre == null) return "/";
        String rutaPadre = padre.getRutaCompleta();
        return (rutaPadre.equals("/") ? "" : rutaPadre) + "/" + nombre;
    }

    // Sobrescribir equals y hashCode para comparar entradas por su ruta completa
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        FileSystemBase that = (FileSystemBase) o;
        return Objects.equals(getRutaCompleta(), that.getRutaCompleta());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRutaCompleta());
    }
}
