package com.google.places.showcase.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.places.showcase.entity.Place;
import com.google.places.showcase.event.PlacesLoadResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Json deserializer to {@link com.google.places.showcase.event.PlacesLoadResponse}
 */
public class PlaceListDeserializer implements JsonDeserializer<PlacesLoadResponse> {
    private static final String KEY_RESULTS = "results";

    @Override
    public PlacesLoadResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Place> results = new ArrayList<Place>();
        Iterator<JsonElement> iterator = json.getAsJsonObject().get(KEY_RESULTS).getAsJsonArray().iterator();

        while (iterator.hasNext()) {
            results.add((Place) context.deserialize(iterator.next(), Place.class));
        }

        PlacesLoadResponse result = new PlacesLoadResponse(results);
        CommonUtil.checkResponseForErrors(json, result);
        return result;
    }
}
