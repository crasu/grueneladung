package de.crasu.grueneladung;

import de.crasu.grueneladung.json.PowerInformation;
import de.crasu.grueneladung.json.PowerValueMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonPowerInformationHelper implements InformationHelper {

    @Override
    public List<PowerGridValues> retrievePowerInformation() {
        //TODO don't use string use stream directly
        String response = httpDownload("http://strom-transparent.appspot.com/power");

        ObjectMapper mapper = new ObjectMapper();

        PowerInformation pi = null;
        try {
            pi = mapper.readValue(response, PowerInformation.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    private synchronized String httpDownload(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        try {
            HttpResponse response = client.execute(request);

            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                throw new RuntimeException("Invalid status code " + status.getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            int readBytes = 0;
            byte[] sBuffer = new byte[16384];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }

            return new String(content.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
