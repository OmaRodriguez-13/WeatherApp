package weatherApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Provider.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClimaApp extends JFrame {

	private JPanel contentPane;
	private static final long serialVersionUID = 1L;
	private JTextField ciudad;
	private JLabel Lciudad, temperatura, descripcion, Lhumedad, Lviento, error;
	private DefaultTableModel modelo = new DefaultTableModel ();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JOptionPane.showMessageDialog(null,
							"Tenga en cuenta que es un servicio de lenguaje nativa inglesa.");
					ClimaApp frame = new ClimaApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClimaApp() {
		super("Conozca el clima de su ciudad!!");
		getContentPane().setLayout(new BorderLayout());

		// Panel de entrada de ciudad
		JPanel panelEntrada = new JPanel(new FlowLayout());
		panelEntrada.add(new JLabel("Ciudad: "));
		ciudad = new JTextField(20);
		panelEntrada.add(ciudad);
		JButton buscar = new JButton("Buscar");
		buscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String apiKey = "7208e6aeb2116e0b55935cb379677e09"; // Reemplaza con tu clave de API
	            String city = ciudad.getText();
	            String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey;

	            try {
	                URL url = new URL(apiUrl);
	                InputStream is = url.openConnection().getInputStream();
	                BufferedReader br = new BufferedReader(new InputStreamReader(is));
	                String linea = br.readLine();
	                StringBuilder sb = new StringBuilder();
	                while (linea != null) {
	                    sb.append(linea);
	                    linea = br.readLine();
	                }
	                JSONObject json = new JSONObject(sb.toString());
	                String nombreC = json.getJSONObject("city").getString("name");
	                
	                JOptionPane.showMessageDialog(null, "Clima de la ciudad: " + nombreC);

	                JSONArray pronostico = json.getJSONArray("list");
	                String[] diasSemana = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
	                
	                modelo.setColumnIdentifiers(new Object[]{"Día de la semana", "Temperatura", "Descripción del clima", "Humedad", "Velocidad del viento"});
	                for (int i = 0; i < pronostico.length(); i += 8) {
	                    JSONObject dia = pronostico.getJSONObject(i);
	                    double tempKelvin = dia.getJSONObject("main").getDouble("temp");
	                    double tempCelsius = tempKelvin - 273.15;
	                    int humedad = dia.getJSONObject("main").getInt("humidity");
	                    double velocidadViento = dia.getJSONObject("wind").getDouble("speed");
	                    String descripcionClima = dia.getJSONArray("weather").getJSONObject(0).getString("description");
	                    
	                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	                    LocalDateTime fechaHora = LocalDateTime.parse(dia.getString("dt_txt"), formatter);
	                    String diaSemana = diasSemana[fechaHora.getDayOfWeek().getValue() - 1];
	                    
	                    
	                    
	                    //String diaSemana = diasSemana[LocalDate.parse(dia.getString("dt_txt")).getDayOfWeek().getValue() - 1];
	                    modelo.addRow(new Object[]{diaSemana, String.format("%.1f°C", tempCelsius), descripcionClima, humedad + "%", String.format("%.1fm/s", velocidadViento)});
	                }

	                
	                //Lciudad.setText("El clima de: " + nombreC);
	                ciudad.setText("");
	                error.setText("");
	            } catch (IOException ex) {
	                //getContentPane().removeAll();
	                //getContentPane().add(panelEntrada, BorderLayout.NORTH);
	                //Lciudad.setText("");
	                error.setText("Error: Ciudad no encontrada");
	            }
				
				/*String apiKey = "7208e6aeb2116e0b55935cb379677e09"; // Reemplaza con tu clave de API
			    String city = ciudad.getText();
			    String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey;

			    try {
			        URL url = new URL(apiUrl);
			        InputStream is = url.openConnection().getInputStream();
			        BufferedReader br = new BufferedReader(new InputStreamReader(is));
			        String linea = br.readLine();
			        StringBuilder sb = new StringBuilder();
			        while (linea != null) {
			            sb.append(linea);
			            linea = br.readLine();
			        }
			        JSONObject json = new JSONObject(sb.toString());
			        String nombreC = json.getJSONObject("city").getString("name");

			        JSONArray pronostico = json.getJSONArray("list");
			        String[] diasSemana = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
			        DefaultTableModel modelo = new DefaultTableModel ();
			        modelo.setRowCount(0);
			        for (int i = 0; i < pronostico.length(); i += 8) {
			            JSONObject dia = pronostico.getJSONObject(i);
			            double tempKelvin = dia.getJSONObject("main").getDouble("temp");
			            double tempCelsius = tempKelvin - 273.15;
			            int humedad = dia.getJSONObject("main").getInt("humidity");
			            double velocidadViento = dia.getJSONObject("wind").getDouble("speed");
			            String descripcionClima = dia.getJSONArray("weather").getJSONObject(0).getString("description");
			            
			            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			            LocalDateTime fechaHora = LocalDateTime.parse(dia.getString("dt_txt"), formatter);
			            String diaSemana = diasSemana[fechaHora.getDayOfWeek().getValue() - 1];
			            
			            //String diaSemana = diasSemana[LocalDate.parse(dia.getString("dt_txt")).getDayOfWeek().getValue() - 1];
			            modelo.addRow(new Object[]{diaSemana, String.format("%.1f°C", tempCelsius), descripcionClima, humedad + "%", String.format("%.1fm/s", velocidadViento)});
			        }

			        Lciudad.setText("El clima de: " + nombreC);
			        temperatura.setText("");
			        descripcion.setText("");
			        Lhumedad.setText("");
			        Lviento.setText("");
			        ciudad.setText("");
			        error.setText("");
			    } catch (IOException ex) {
			        Lciudad.setText("");
			        temperatura.setText("");
			        descripcion.setText("");
			        Lhumedad.setText("");
			        Lviento.setText("");
			        error.setText("Error: Ciudad no encontrada");
			    }*/
				
				/*String apiKey = "7208e6aeb2116e0b55935cb379677e09"; // Reemplaza con tu clave de API
			    String city = ciudad.getText();
			    String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey;

			    try {
			        URL url = new URL(apiUrl);
			        InputStream is = url.openConnection().getInputStream();
			        BufferedReader br = new BufferedReader(new InputStreamReader(is));
			        String linea = br.readLine();
			        StringBuilder sb = new StringBuilder();
			        while (linea != null) {
			            sb.append(linea);
			            linea = br.readLine();
			        }
			        JSONObject json = new JSONObject(sb.toString());
			        String nombreC = json.getJSONObject("city").getString("name");

			        JSONArray pronostico = json.getJSONArray("list");
			        String[] diasSemana = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
			        for (int i = 0; i < pronostico.length(); i += 8) {
			            JSONObject dia = pronostico.getJSONObject(i);
			            double tempKelvin = dia.getJSONObject("main").getDouble("temp");
			            double tempCelsius = tempKelvin - 273.15;
			            int humedad = dia.getJSONObject("main").getInt("humidity");
			            double velocidadViento = dia.getJSONObject("wind").getDouble("speed");
			            String descripcionClima = dia.getJSONArray("weather").getJSONObject(0).getString("description");
			            
			            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			            LocalDateTime fechaHora = LocalDateTime.parse(dia.getString("dt_txt"), formatter);
			            String diaSemana = diasSemana[fechaHora.getDayOfWeek().getValue() - 1];
			            
			            //String diaSemana = diasSemana[LocalDate.parse(dia.getString("dt_txt")).getDayOfWeek().getValue() - 1];
			            System.out.printf("%s: %.1f°C, %s, %d%%, %.1fm/s\n", diaSemana, tempCelsius, descripcionClima, humedad, velocidadViento);
			        }

			        Lciudad.setText("El clima de: " + nombreC);
			        temperatura.setText("");
			        descripcion.setText("");
			        Lhumedad.setText("");
			        Lviento.setText("");
			        ciudad.setText("");
			        error.setText("");
			    } catch (IOException ex) {
			        Lciudad.setText("");
			        temperatura.setText("");
			        descripcion.setText("");
			        Lhumedad.setText("");
			        Lviento.setText("");
			        error.setText("Error: Ciudad no encontrada");
			    }*/
				
				
				/*String apiKey = "7208e6aeb2116e0b55935cb379677e09"; // Reemplaza con tu clave de API
				String city = ciudad.getText();
				String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

				try {
					URL url = new URL(apiUrl);
					InputStream is = url.openConnection().getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String linea = br.readLine();
					StringBuilder sb = new StringBuilder();
					while (linea != null) {
						sb.append(linea);
						linea = br.readLine();
					}
					JSONObject json = new JSONObject(sb.toString());
					String nombreC = json.getString("name");
					double tempKelvin = json.getJSONObject("main").getDouble("temp");
					double tempCelsius = tempKelvin - 273.15;
					// Extrae la humedad del objeto JSON y la muestra en pantalla
					int humedad = json.getJSONObject("main").getInt("humidity");
					// Extrae la velocidad del viento del objeto JSON y la muestra en pantalla
					double velocidadViento = json.getJSONObject("wind").getDouble("speed");
					String descripcionClima = json.getJSONArray("weather").getJSONObject(0).getString("description");
					Lciudad.setText("El clima de: " + nombreC);
					temperatura.setText(String.format("Es de: %.1f °C", tempCelsius));
					descripcion.setText(String.format("Descripción: %s", descripcionClima));
					Lhumedad.setText(String.format("Humedad: %d %%", humedad));
					Lviento.setText(String.format("Velocidad: %s m/s", velocidadViento));

					// System.out.println("Humedad: " + humedad + " %");

					// System.out.println("Velocidad del viento: " + velocidadViento + " m/s");

					ciudad.setText("");
					error.setText("");
				} catch (IOException ex) {
					temperatura.setText("");
					descripcion.setText("");
					error.setText("Error: Ciudad no encontrada");
				}*/
				
				
				
			}
		});
		panelEntrada.add(buscar);
		getContentPane().add(panelEntrada, BorderLayout.NORTH);
		
		JTable tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

		// Panel de resultados
		/*JPanel panelResultados = new JPanel();
		BoxLayout boxLayout = new BoxLayout(panelResultados, BoxLayout.Y_AXIS);
		panelResultados.setLayout(boxLayout);
		*/
		

		/*Lciudad = new JLabel();
		Lciudad.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelResultados.add(Lciudad);

		temperatura = new JLabel();
		temperatura.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelResultados.add(temperatura);

		descripcion = new JLabel();
		descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelResultados.add(descripcion);

		Lhumedad = new JLabel();
		Lhumedad.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelResultados.add(Lhumedad);

		Lviento = new JLabel();
		Lviento.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelResultados.add(Lviento);*/

		//getContentPane().add(panelResultados, BorderLayout.CENTER);

		// Panel de error
		JPanel panelError = new JPanel(new FlowLayout());
		error = new JLabel();
		panelError.add(error);
		getContentPane().add(panelError, BorderLayout.SOUTH);

		setSize(800, 205);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
