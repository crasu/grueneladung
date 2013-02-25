package de.crasu.grueneladung.status;

import android.app.Activity;
import android.content.Context;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ChargeCounterTest {
    private ChargeCounter chargeCounter;
    private Context contextMock;

    @Before
    public void setup() {
        contextMock = new Activity();

        chargeCounter = new ChargeCounter(contextMock);
        chargeCounter.reset();
    }

    @Test
    public void chargeCounterBasicFunctionsAreWorking() {
        chargeCounter.count();
        chargeCounter.count();
        assertThat(chargeCounter.getCount(), is(2L));

        chargeCounter.reset();
        assertThat(chargeCounter.getCount(), is(0L));
    }

    @Test
    public void chargeCountIsRetained() {
        chargeCounter.count();
        chargeCounter = new ChargeCounter(contextMock);
        assertThat(chargeCounter.getCount(), is(1L));
    }


}
