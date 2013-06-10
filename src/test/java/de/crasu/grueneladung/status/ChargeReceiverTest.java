package de.crasu.grueneladung.status;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.BatteryManager;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.xtremelabs.robolectric.Robolectric;
import de.crasu.grueneladung.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.RoboGuice;

import java.util.ArrayList;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(InjectingTestRunner.class)
public class ChargeReceiverTest {
    @Before
    public void configure() { //TODO clean this up
        RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE, RoboGuice.newDefaultRoboModule(Robolectric.application), Modules.override(new roboGuiceMainModul()).with(new Module() {
            @Override
            //TODO cleanup this config
            public void configure(Binder binder) {
                PowerGridInformationRetriever mock = mock(PowerGridInformationRetriever.class);
                when(mock.isEnergyGreen()).thenReturn(false);
                binder.bind(PowerGridInformationRetriever.class).toInstance(mock);
            }
        }));
    }

    @After
    public void teardown() {
        RoboGuice.util.reset();
    }

    @Test
    public void testHandleReceive() throws Exception {
        ChargeReceiver chargeReceiver = new ChargeReceiver();

        Injector injector = RoboGuice.getInjector(Robolectric.application);
        injector.injectMembers(chargeReceiver);

        Intent intent = new Intent();
        intent.putExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_FULL);

        chargeReceiver.handleReceive(Robolectric.application, intent);
        assertThat(chargeReceiver.chargeCounter.getCount(), is(0L));
    }
}
