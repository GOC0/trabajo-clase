import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;



public class main {

    public static void main(String[] args) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.facebook.com/login/?locale=es_LA"))
                .build();

        HttpResponse<String> response = client
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .join();

        String contentType = response.headers()
                .firstValue("Content-Type")
                .orElse("desconocido");

        System.out.println(contentType);

        String html = response.body();

        // Contar líneas
        int lineas = html.split("\n").length;

        Document doc = Jsoup.parse(html);
        int parrafos = doc.select("p").size();
        int imagen = doc.select("img").size();
        var form = doc.select("form");
        int formulario= doc.select("form").size();
        int getForms = 0;
        int postForms = 0;

        for (var f : form) {
            String method = f.attr("method").toLowerCase();

            if (method.isEmpty() || method.equals("get")) {
                getForms++;
            } else if (method.equals("post")) {
                postForms++;
            }
        }


        System.out.println("Cantidad de parrafos: " + parrafos);
        System.out.println("Cantidad de imagen: " + imagen);
        System.out.println("Cantidad de lineas: " + lineas);
        System.out.println("Cantidad de form: " + formulario);
        System.out.println("Cantidad de getform: " + getForms);
        System.out.println("Cantidad de postform: " + postForms);

        for (var f : form) {
            String method = f.attr("method").toLowerCase();

            if (method.isEmpty() || method.equals("get")) {
                getForms++;
            } else if (method.equals("post")) {
                postForms++;
                String action = f.attr("action");
                if (action.isEmpty()) {
                    action = "https://www.facebook.com/login/?locale=es_LA";
                }
                String formData = "asignatura=practica1";

                try {
                    HttpRequest postRequest = HttpRequest.newBuilder()
                            .uri(URI.create(action))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("name", "cccc")
                            .POST(HttpRequest.BodyPublishers.ofString(formData))
                            .build();

                    HttpResponse<String> postResponse = client.send(postRequest,
                            HttpResponse.BodyHandlers.ofString());

                    System.out.println("Respuesta POST: " + postResponse.statusCode());

                } catch (Exception e) {
                    System.err.println("Error al enviar POST: " + e.getMessage());
                }
            }
        }
    }
}
