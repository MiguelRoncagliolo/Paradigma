public class Pregunta {
    private int id;
    private String actividad;
    private boolean respuesta;
    private String area;

    public Pregunta(int id, String actividad, String area) {
        this.id = id;
        this.actividad = actividad;
        this.area = area;
        this.respuesta = false;
    }

    public int getId() {
        return id;
    }

    public String getActividad() {
        return actividad;
    }

    public boolean isRespondida() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }

    public String getArea() {
        return area;
    }
}
