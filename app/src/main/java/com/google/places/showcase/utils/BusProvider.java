package com.google.places.showcase.utils;

import com.squareup.otto.Bus;

/**
 * Default event bus provider
 */
public final class BusProvider {
    private static final MainThreadBus BUS = new MainThreadBus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // no instances allowed
    }
}
