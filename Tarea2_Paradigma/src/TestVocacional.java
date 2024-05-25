import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class TestVocacional extends JFrame {

    private java.util.List<Area> areas;
    private java.util.List<Pregunta> preguntas;
    private Map<Integer, String> respuestas;
    private int preguntaActual;
    private JLabel preguntaLabel;
    private JButton siguienteButton;
    private JButton anteriorButton;
    private JRadioButton siButton;
    private JRadioButton noButton;
    private ButtonGroup respuestaGroup;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TestVocacional test = new TestVocacional();
            test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            test.setSize(1000, 200);
            test.setVisible(true);
        });
    }

    public TestVocacional() {
        areas = new ArrayList<>();
        preguntas = new ArrayList<>();
        respuestas = new HashMap<>();
        cargarDatos("archivo.txt");
        initComponents();
    }

    private void initComponents() {
        setTitle("Test Vocacional");
        setLayout(new BorderLayout());

        // Panel de instrucciones
        JPanel instruccionesPanel = new JPanel();
        instruccionesPanel.setLayout(new BorderLayout());
        JTextArea instrucciones = new JTextArea("Instrucciones: Responda a cada pregunta con 'Sí' o 'No'. Puede avanzar y retroceder entre las preguntas. Solo puede calcular los resultados si ha respondido a todas las preguntas.");
        instrucciones.setWrapStyleWord(true);
        instrucciones.setLineWrap(true);
        instrucciones.setEditable(false);
        instruccionesPanel.add(instrucciones, BorderLayout.CENTER);
        JButton empezarButton = new JButton("Empezar");
        instruccionesPanel.add(empezarButton, BorderLayout.SOUTH);
        add(instruccionesPanel, BorderLayout.NORTH);

        // Panel de preguntas
        JPanel preguntaPanel = new JPanel();
        preguntaPanel.setLayout(new BorderLayout());
        preguntaLabel = new JLabel("Pregunta 1: " + preguntas.get(0).actividad);
        preguntaPanel.add(preguntaLabel, BorderLayout.NORTH);

        siButton = new JRadioButton("Me interesa");
        noButton = new JRadioButton("No me interesa");
        respuestaGroup = new ButtonGroup();
        respuestaGroup.add(siButton);
        respuestaGroup.add(noButton);

        JPanel opcionesPanel = new JPanel();
        opcionesPanel.add(siButton);
        opcionesPanel.add(noButton);
        preguntaPanel.add(opcionesPanel, BorderLayout.CENTER);

        siguienteButton = new JButton("Siguiente");
        anteriorButton = new JButton("Anterior");

        JPanel navegacionPanel = new JPanel();
        navegacionPanel.add(anteriorButton);
        navegacionPanel.add(siguienteButton);
        preguntaPanel.add(navegacionPanel, BorderLayout.SOUTH);

        // Añadir panel de preguntas al frame pero ocultarlo inicialmente
        preguntaPanel.setVisible(false);
        add(preguntaPanel, BorderLayout.CENTER);

        // Action Listeners
        empezarButton.addActionListener(e -> {
            instruccionesPanel.setVisible(false);
            preguntaPanel.setVisible(true);
        });

        siguienteButton.addActionListener(e -> {
            registrarRespuesta();
            if (preguntaActual < preguntas.size() - 1) {
                preguntaActual++;
                actualizarPregunta();
            } else {
                mostrarResultados();
            }
        });

        anteriorButton.addActionListener(e -> {
            if (preguntaActual > 0) {
                preguntaActual--;
                actualizarPregunta();
            }
        });

        preguntaActual = 0;
        actualizarPregunta();
    }

    private void registrarRespuesta() {
        int idPregunta = preguntas.get(preguntaActual).id;
        String respuesta = siButton.isSelected() ? "Sí" : "No";
        respuestas.put(idPregunta, respuesta);
    }

    private void actualizarPregunta() {
        Pregunta pregunta = preguntas.get(preguntaActual);
        preguntaLabel.setText("Pregunta " + pregunta.id + ": " + pregunta.actividad);
        String respuesta = respuestas.getOrDefault(pregunta.id, "");
        if (respuesta.equals("Sí")) {
            siButton.setSelected(true);
        } else if (respuesta.equals("No")) {
            noButton.setSelected(true);
        } else {
            respuestaGroup.clearSelection();
        }
    }

    private void mostrarResultados() {
    // Map para almacenar el puntaje de cada área
    Map<String, Integer> puntajes = new HashMap<>();

    // Inicializar puntajes en 0 para cada área
    for (Area area : areas) {
        puntajes.put(area.nombre, 0);
    }

    // Calcular puntajes según preguntas respondidas
    for (Pregunta pregunta : preguntas) {
        if (respuestas.containsKey(pregunta.id) && respuestas.get(pregunta.id).equals("Sí")) {
            String areaNombre = pregunta.area.nombre;
            puntajes.put(areaNombre, puntajes.get(areaNombre) + 1);
        }
    }

    // Ordenar áreas por puntaje decreciente
    List<Map.Entry<String, Integer>> listaPuntajes = new ArrayList<>(puntajes.entrySet());
    listaPuntajes.sort((a, b) -> b.getValue().compareTo(a.getValue()));

    // Construir mensaje de resultados
    StringBuilder resultados = new StringBuilder("Resultados del Test Vocacional:\n");
    for (Map.Entry<String, Integer> entry : listaPuntajes) {
        resultados.append("\nÁrea ").append(entry.getKey()).append(": Puntaje ").append(entry.getValue());
    }

    // Mostrar resultados en un cuadro de diálogo
    JTextArea textArea = new JTextArea(resultados.toString());
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(400, 300));
    JOptionPane.showMessageDialog(this, scrollPane, "Resultados", JOptionPane.INFORMATION_MESSAGE);
}




    private void cargarDatos(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            Map<String, Area> areaMap = new HashMap<>();
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith("ÁREA")) {
                    String areaNombre = linea.substring(5).trim();
                    String[] profesiones = br.readLine().trim().split(";");
                    Area area = new Area(areaNombre, Arrays.asList(profesiones));
                    areas.add(area);
                    areaMap.put(areaNombre, area);
                } else if (linea.matches("\\d+\\. .*")) {
                    String[] partes = linea.split("\\. ", 2);
                    int id = Integer.parseInt(partes[0]);
                    String actividad = partes[1];
                    String areaNombre = br.readLine().trim();
                    Area area = areaMap.get(areaNombre);
                    if (area != null) {
                        Pregunta pregunta = new Pregunta(id, actividad, area);
                        preguntas.add(pregunta);
                    } else {
                        System.out.println("Área no encontrada para la pregunta: " + actividad);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Area {
        String nombre;
        java.util.List<String> profesiones;

        public Area(String nombre, java.util.List<String> profesiones) {
            this.nombre = nombre;
            this.profesiones = profesiones;
        }
    }

    class Pregunta {
        int id;
        String actividad;
        Area area;

        public Pregunta(int id, String actividad, Area area) {
            this.id = id;
            this.actividad = actividad;
            this.area = area;
        }
    }
}
