package manager;
public class Archivo extends FileSystemBase {
    public Archivo(String nombre, Directorio padre) {
        super(nombre, padre);
    }

    @Override
    public String getRutaCompleta() {
        if (this.getPadre() == null || this.getPadre().getNombre().equals("/")) { // Si es el raíz
            return "/" + this.getNombre();
        }
        return this.getPadre().getRutaCompleta() + "/" + this.getNombre();
    }

    @Override
    public String toString() {
        return "Archivo: " + this.getNombre();
    }
}

