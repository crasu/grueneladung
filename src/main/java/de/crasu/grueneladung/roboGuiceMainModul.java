package de.crasu.grueneladung;

import com.google.inject.Binder;
import com.google.inject.Module;

public class roboGuiceMainModul implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(TwitterHelper.class).to(TwitterHelperImpl.class);
        binder.bind(PowerGridInformationRetriever.class).to(PowerGridInformationRetrieverImpl.class);
    }
}
