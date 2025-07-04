package manager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Directorio extends FileSystemBase {
    private final Map<String, FileSystemBase> hijos;

    public Directorio(String nombre, Directorio padre) {
        super(nombre, padre);
        this.hijos = new HashMap<>();
    }

    public void addHijo(FileSystemBase hijo) {
        hijos.put(hijo.getNombre(), hijo);
        hijo.setPadre(this);
    }

    public Map<String, FileSystemBase> getHijos() {
        return hijos;
    }

    public FileSystemBase getHijo(String nombre) {
        return hijos.get(nombre);
    }
    
    public boolean removeHijo(String nombre) {
        if (hijos.containsKey(nombre)) {
            hijos.remove(nombre);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Directorio: " + this.getNombre();
    }

    // Sobrescribir equals y hashCode para Directorio
    // Asegurarse de que al comparar directorios, se base en su ruta completa
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Directorio that = (Directorio) o;
        return Objects.equals(getRutaCompleta(), that.getRutaCompleta());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRutaCompleta());
    }
}
