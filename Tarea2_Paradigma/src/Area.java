import java.util.List;

public class Area {
    private String nombre;
    private List<String> profesiones;

    public Area(String nombre, List<String> profesiones) {
        this.nombre = nombre;
        this.profesiones = profesiones;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getProfesiones() {
        return profesiones;
    }
}
