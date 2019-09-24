package services;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Kudos;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import play.Logger;
import play.libs.Json;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class KudosService {
    @Inject
    public KudosService() {

    }

    public List<Kudos> getKudos(String userId) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            HttpHost target = new HttpHost("localhost", 9102, "http");
            HttpGet getRequest = new HttpGet("/kudos/target/" + userId);

            Logger.info("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                String body = EntityUtils.toString(entity);
                List<Kudos> list = Json.mapper().readValue(body, new TypeReference<List<Kudos>>(){});

                return list;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        return new ArrayList<>();
    }

    public void deleteByUserId(String userId) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        String uri = String.format("/stats/user/%s/delete", userId);
        try {
            HttpHost target = new HttpHost("localhost", 9103, "http");
            HttpGet getRequest = new HttpGet(uri);

            Logger.info("[Delete Kudos] Borrar kudos para el usuario: " + userId);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }
}
