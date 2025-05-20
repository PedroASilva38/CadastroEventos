import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;

public class ConsultaViaCep {

    public Map<String, String> buscarDadosPorCep(String cep) {
        if (cep == null || !cep.matches("\\d{8}")) {
            System.out.println("Formato de CEP inválido para a API: " + cep);
            return null;
        }

        String urlViaCep = "https://viacep.com.br/ws/" + cep + "/json/";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlViaCep))
                .GET()
                .build();

        System.out.println("Consultando CEP: " + cep + "...");

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (jsonObject.has("erro") && jsonObject.getBoolean("erro")) {
                    System.out.println("CEP não encontrado na base de dados do ViaCEP.");
                    return null;
                } else {
                    Map<String, String> dadosEndereco = new HashMap<>();
                    dadosEndereco.put("logradouro", jsonObject.optString("logradouro", ""));
                    dadosEndereco.put("bairro", jsonObject.optString("bairro", ""));
                    dadosEndereco.put("localidade", jsonObject.optString("localidade", ""));
                    dadosEndereco.put("uf", jsonObject.optString("uf", ""));
                    return dadosEndereco;
                }
            } else {
                System.out.println("Erro ao consultar o CEP na API. Código HTTP: " + response.statusCode());
                return null;
            }
        } catch (java.io.IOException | InterruptedException e) {
            System.err.println("Erro de conexão ou interrupção ao consultar a API ViaCEP:");
            e.printStackTrace();
            return null;
        }
    }
}