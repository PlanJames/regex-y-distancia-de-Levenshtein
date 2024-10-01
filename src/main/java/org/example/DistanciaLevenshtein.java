package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistanciaLevenshtein {

    // Clase para almacenar la palabra y su distancia
    static class PalabraConDistancia {
        String palabra;
        int distancia;

        PalabraConDistancia(String palabra, int distancia) {
            this.palabra = palabra;
            this.distancia = distancia;
        }
    }

    // Método para calcular la distancia de Levenshtein
    public static int calcularDistanciaLevenshtein(String palabra1, String palabra2) {
        int len1 = palabra1.length();
        int len2 = palabra2.length();

        int[][] distancia = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            distancia[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            distancia[0][j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int costo = (palabra1.charAt(i - 1) == palabra2.charAt(j - 1)) ? 0 : 1;
                distancia[i][j] = Math.min(Math.min(
                                distancia[i - 1][j] + 1,
                                distancia[i][j - 1] + 1),
                        distancia[i - 1][j - 1] + costo);
            }
        }

        return distancia[len1][len2];
    }

    // Metodo para extraer palabras de un texto usando regex
    public static List<String> extraerPalabras(String texto) {
        List<String> palabras = new ArrayList<>();
        String regex = "\\b[\\p{L}]+\\b";  // Expresión regular para palabras (incluye letras acentuadas y ñ)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            palabras.add(matcher.group());
        }

        return palabras;
    }

    // Metodo principal
    public static void main(String[] args) {
        // Cargar el archivo de texto desde resources
        InputStream inputStream = DistanciaLevenshtein.class.getClassLoader().getResourceAsStream("discurso.txt");

        if (inputStream == null) {
            System.out.println("Error: No se pudo encontrar el archivo discurso.txt en resources.");
            return;
        }

        StringBuilder contenido = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append(" ");
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        // Extraer palabras del texto
        List<String> palabrasExtraidas = extraerPalabras(contenido.toString());

        // Crear un escaner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa la palabra de referencia: ");
        String palabraReferencia = scanner.nextLine();

        // Lista para almacenar palabras y sus distancias
        List<PalabraConDistancia> palabrasConDistancia = new ArrayList<>();

        // Encontrar las palabras y calcular distancias
        for (String palabra : palabrasExtraidas) {
            int distancia = calcularDistanciaLevenshtein(palabra, palabraReferencia);
            palabrasConDistancia.add(new PalabraConDistancia(palabra, distancia));
        }

        // Ordenar la lista por distancia
        palabrasConDistancia.sort(Comparator.comparingInt(p -> p.distancia));

        // Imprimir resultados ordenados
        System.out.println("Comparando palabras extraídas con la palabra de referencia: " + palabraReferencia);
        for (PalabraConDistancia pd : palabrasConDistancia) {
            System.out.println("Palabra: " + pd.palabra + " | Distancia: " + pd.distancia);
        }
        scanner.close();
    }
}
