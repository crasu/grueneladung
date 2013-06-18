package de.crasu.grueneladung;

import de.crasu.grueneladung.json.PowerInformation;
import de.crasu.grueneladung.json.PowerValueMap;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonPowerInformationHelper implements InformationHelper {

    @Override
    public List<PowerGridValues> retrievePowerInformation() {
        PowerInformation pi = null;
        try {
            InputStream response = httpDownload("http://strom-transparent.appspot.com/power");
            pi = (new ObjectMapper()).readValue(response, PowerInformation.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return convertToPowerGridValueses(pi);
    }

    private List<PowerGridValues> convertToPowerGridValueses(PowerInformation pi) {
        List<PowerGridValues> pgvs = new ArrayList<PowerGridValues>();

        for (PowerValueMap pv : pi) {
            PowerGridValues pgv = (new PowerGridValuesBuilder()).
                    withGas(pv.get("gas")).
                    withCoal(pv.get("steinkohle") + pv.get("braunkohle")).
                    withNuclear(pv.get("kernenergie")).
                    build();
            pgvs.add(pgv);
        }
        return pgvs;
    }

    private InputStream httpDownload(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        try {
            HttpResponse response = client.execute(request);

            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                throw new RuntimeException("Invalid status code " + status.getStatusCode());
            }
            return response.getEntity().getContent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
