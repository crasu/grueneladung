package de.crasu.grueneladung;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class JsonPowerInformationHelperTest {
    @Test
    public void retrievePowerIntegrationTest() {
        JsonPowerInformationHelper jpih = new JsonPowerInformationHelper();

        List<PowerGridValues> pgvs = jpih.retrievePowerInformation();

        assertThat(pgvs.size(), greaterThan(1));
    }
}
