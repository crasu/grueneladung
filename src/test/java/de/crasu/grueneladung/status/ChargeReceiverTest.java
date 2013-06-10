package de.crasu.grueneladung.status;

import android.content.Intent;
import android.os.BatteryManager;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import de.crasu.grueneladung.InjectingTestRunner;
import de.crasu.grueneladung.PowerGridInformationRetriever;
import de.crasu.grueneladung.roboGuiceMainModul;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(InjectingTestRunner.class)
public class ChargeReceiverTest {
    PowerGridInformationRetriever mock = mock(PowerGridInformationRetriever.class);

    @Before
    public void configure() {
        overwriteInjectionModule(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(PowerGridInformationRetriever.class).toInstance(mock);
            }
        }
        );
    }

    @After
    public void shutdown() {
        RoboGuice.util.reset();
    }

    @Test
    public void testHandleReceive() throws Exception {
        Intent intent = new Intent();
        intent.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_FULL);

        ChargeReceiver chargeReceiver = setupChargeReceiver();

        chargeReceiver.handleReceive(Robolectric.application, intent);
        waitForThread();
        assertThat(chargeReceiver.chargeCounter.getCount(), is(1L));

        chargeReceiver = setupChargeReceiver();
        chargeReceiver.handleReceive(Robolectric.application, intent);
        waitForThread();
        assertThat(chargeReceiver.chargeCounter.getCount(), is(2L));
    }

    private ChargeReceiver setupChargeReceiver() {
        Injector injector = RoboGuice.getInjector(Robolectric.application);

        ChargeReceiver chargeReceiver = new ChargeReceiver();
        injector.injectMembers(chargeReceiver);
        when(mock.isEnergyGreen()).thenReturn(true);

        return chargeReceiver;
    }

    private void waitForThread() throws InterruptedException {
        Thread.sleep(200);
    }

    private void overwriteInjectionModule(Module module) {
        RoboGuice.setBaseApplicationInjector(Robolectric.application,
                RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(Robolectric.application),
                Modules.override(new roboGuiceMainModul()).
                        with(module));
    }
}
