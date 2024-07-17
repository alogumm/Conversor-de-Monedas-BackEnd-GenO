
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "your_api_key_here";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Bienvenido al Conversor de Monedas");
            System.out.println("1. Convertir de USD a ARS");
            System.out.println("2. Convertir de USD a BRL");
            System.out.println("3. Convertir de USD a CLP");
            System.out.println("4. Convertir de BRL a USD");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            int option = scanner.nextInt();

            if (option == 5) {
                break;
            }

            System.out.print("Ingrese el valor que desea convertir: ");
            double amount = scanner.nextDouble();

            String fromCurrency = "";
            String toCurrency = "";

            switch (option) {
                case 1:
                    fromCurrency = "USD";
                    toCurrency = "ARS";
                    break;
                case 2:
                    fromCurrency = "USD";
                    toCurrency = "BRL";
                    break;
                case 3:
                    fromCurrency = "USD";
                    toCurrency = "CLP";
                    break;
                case 4:
                    fromCurrency = "BRL";
                    toCurrency = "USD";
                    break;
                default:
                    System.out.println("Opción no válida");
                    continue;
            }

            double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);
            System.out.printf("El valor de %.2f %s corresponde al valor final de %.2f %s\n", amount, fromCurrency, convertedAmount, toCurrency);
        }

        scanner.close();
    }

    public static double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        double rate = getExchangeRate(fromCurrency, toCurrency);
        return amount * rate;
    }

    public static double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            URL url = new URL(API_URL + fromCurrency);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());
                String inline = "";
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();

                JsonObject jsonObject = JsonParser.parseString(inline).getAsJsonObject();
                return jsonObject.get("conversion_rates").getAsJsonObject().get(toCurrency).getAsDouble();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
