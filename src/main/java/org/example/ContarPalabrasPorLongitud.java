package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class ContarPalabrasPorLongitud {

    public static void main(String[] args) {
        // Mapa para almacenar las palabras agrupadas por longitud
        Map<Integer, List<String>> palabrasPorLongitud = new HashMap<>();
        Map<Integer, Integer> conteoPorLongitud = new HashMap<>();
        int totalPalabras = 0;  // Contador total de palabras

        // Obtener el txt
        InputStream inputStream = ContarPalabrasPorLongitud.class.getClassLoader().getResourceAsStream("discurso.txt");

        if (inputStream == null) {
            System.out.println("Error: No se pudo encontrar el archivo discurso.txt en resources.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder contenido = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append(" ");
            }
            reader.close();

            // Expresion regular para identificar palabras (alfabeticas, incluyendo acentos y ñ)
            String regex = "\\b[\\p{L}]+\\b";  // Palabras formadas por caracteres unicode (\p{L} incluye letras acentuadas y ñ)
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(contenido.toString());

            // Agrupar las palabras por su longitud y contar cuantas hay de cada longitud
            while (matcher.find()) {
                String palabra = matcher.group();
                int longitud = palabra.length();

                // Incrementar el total de palabras
                totalPalabras++;

                // Si la longitud no esta en el mapa, agregamos una lista vacia
                palabrasPorLongitud.putIfAbsent(longitud, new ArrayList<>());

                // Agregar la palabra a la lista correspondiente
                palabrasPorLongitud.get(longitud).add(palabra);

                // Incrementar el conteo de palabras por longitud
                conteoPorLongitud.put(longitud, conteoPorLongitud.getOrDefault(longitud, 0) + 1);
            }

            // Escribir las palabras en un archivo de salida
            escribirPalabrasEnArchivo(palabrasPorLongitud);

        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        // Imprimir el total de palabras y las cantidades por longitud
        System.out.println("Conteo de palabras por longitud:");
        for (Map.Entry<Integer, Integer> entry : conteoPorLongitud.entrySet()) {
            System.out.println("Palabras de " + entry.getKey() + " letras: " + entry.getValue());
        }

        // Imprimir el total de palabras
        System.out.println("\nTotal de palabras en el archivo: " + totalPalabras);
    }

    // Método para escribir las palabras agrupadas por longitud en un archivo
    public static void escribirPalabrasEnArchivo(Map<Integer, List<String>> palabrasPorLongitud) {
        try (FileWriter writer = new FileWriter("palabras_por_longitud.txt")) {
            for (Map.Entry<Integer, List<String>> entry : palabrasPorLongitud.entrySet()) {
                writer.write("Palabras de " + entry.getKey() + " letras:\n");

                // Escribir las palabras separadas por comas
                writer.write(String.join(", ", entry.getValue()));

                writer.write("\n\n");  // Salto de linea entre grupos
            }
            System.out.println("Palabras agrupadas por longitud escritas en palabras_por_longitud.txt");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}
