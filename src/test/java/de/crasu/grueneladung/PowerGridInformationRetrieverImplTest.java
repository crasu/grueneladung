package de.crasu.grueneladung;

import com.google.inject.Inject;
import roboguice.test.RobolectricRoboTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(InjectingTestRunner.class)
public class PowerGridInformationRetrieverImplTest {
    @Inject
    private PowerGridInformationRetrieverImpl pgir;

    @Before
    public void setup() {
        pgir.setTwitterHelper(mock(TwitterHelper.class));
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
        TwitterHelperImpl twitterHelperMock = mock(TwitterHelperImpl.class);
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
