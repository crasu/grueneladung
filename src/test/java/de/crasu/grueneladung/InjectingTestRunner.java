package de.crasu.grueneladung;

import android.content.Context;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.xtremelabs.robolectric.Robolectric;
import org.junit.runners.model.InitializationError;
import roboguice.RoboGuice;
import roboguice.inject.ContextScope;
import roboguice.test.RobolectricRoboTestRunner;

public class InjectingTestRunner extends RobolectricRoboTestRunner {
    public InjectingTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void prepareTest(Object test) {
        RoboGuice.setBaseApplicationInjector(Robolectric.application, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(Robolectric.application), new Module() {
            @Override
            public void configure(Binder binder) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        Injector injector = RoboGuice.getInjector(Robolectric.application);

        ContextScope scope = injector.getInstance(ContextScope.class);
        scope.enter(Robolectric.application);
        injector.injectMembers(test);
    }
}
