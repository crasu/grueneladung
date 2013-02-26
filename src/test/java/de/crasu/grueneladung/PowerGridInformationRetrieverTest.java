package de.crasu.grueneladung;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PowerGridInformationRetrieverTest {
    private PowerGridInformationRetriever pgir;

    @Before
    public void setup() {
        pgir = new PowerGridInformationRetriever();
        pgir.setTwitterHelper(mock(TwitterHelper.class));
    }

    @Test 
    public void parseTweetToPowerGridValues() {        
        String tweetText = "Braunkohle ges.: 8613MW/ " +
                "Steinkohle ges.: 0MW/ Gas ges.: 603MW/ " +
                "Kernenergie ges.: 3960MW/ Gesamt-Summe: 13176MW " +
                "03.02.13/13:29";

        PowerGridValues pgv = pgir.parseTweet(tweetText);
        
        assertThat(pgv.getCoal(), is(8613));        
        assertThat(pgv.getGas(), is(603));
        assertThat(pgv.getNuclear(), is(3960));
        
        assertThat(pgv.getOverallPower(), is(13176));
    }

    @Test(expected=RuntimeException.class)
    public void CalcAverageGasPercentageThrowsOnEmptyList() {
        List<PowerGridValues> pgvs = new ArrayList<PowerGridValues>();
        
        pgir.calcAverageGasPercentage(pgvs);
    }
    
    @Test(expected=RuntimeException.class)
    public void CalcAverageGasPercentageThrowsOnNull() {
        List<PowerGridValues> pgvs = null;
        
        pgir.calcAverageGasPercentage(pgvs);
    }    
    
    @Test
    public void CalcAverageGasPercentage() {
        List<PowerGridValues> pgvs = initPowerGridValues();
        
        double percentage = pgir.calcAverageGasPercentage(pgvs);
        
        assertThat(Math.abs(percentage), lessThan(0.34d));
        assertThat(Math.abs(percentage), greaterThan(0.32d));
    }

    @Test
    public void sumIsCorrect() {
        PowerGridValues pgv = new PowerGridValues();
        pgv.setCoal(5);
        pgv.setGas(4);
        pgv.setNuclear(3);
        
        assertThat(5 + 4 + 3, is(pgv.getOverallPower()));
    }

    @Test
    public void belowAvgGasConsumptionIsGreen() {
        List<PowerGridValues> pgvs = initPowerGridValues();
        PowerGridValues pgv = new PowerGridValues();
        pgv.setCoal(5);
        pgv.setGas(3);
        pgv.setNuclear(3);
        pgvs.add(0, pgv);

        mockPgvs(pgvs);

        assertThat(pgir.isEnergyGreen(), is(true));

        pgvs = initPowerGridValues();
        pgv = new PowerGridValues();
        pgv.setCoal(5);
        pgv.setGas(5);
        pgv.setNuclear(3);
        pgvs.add(0, pgv);

        mockPgvs(pgvs);

        assertThat(pgir.isEnergyGreen(), is(false));
    }

    private void mockPgvs(List<PowerGridValues> pgvs) {
        TwitterHelper twitterHelperMock = mock(TwitterHelper.class);
        when(twitterHelperMock.retrievePowerInformation()).thenReturn(pgvs);
        pgir.setTwitterHelper(twitterHelperMock);
    }

    private List<PowerGridValues> initPowerGridValues() {
        List<PowerGridValues> pgvs = new ArrayList<PowerGridValues>();
        
        for(int i = 1; i < 3; i++) {
            PowerGridValues pgv = new PowerGridValues();
            pgv.setCoal(5 + i);
            pgv.setGas(5 + i);
            pgv.setNuclear(5 + i);
            
            pgvs.add(pgv);
        }
        return pgvs;
    }

}
