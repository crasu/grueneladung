package de.crasu.grueneladung;

import com.google.inject.Binder;
import com.google.inject.Module;

public class roboGuiceMainModul implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(InformationHelper.class).to(JsonPowerInformationHelper.class);
        binder.bind(PowerGridInformationRetriever.class).to(PowerGridInformationRetrieverImpl.class);
    }
}
