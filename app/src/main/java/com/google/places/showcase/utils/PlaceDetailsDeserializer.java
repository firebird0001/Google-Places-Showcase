package com.google.places.showcase.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.places.showcase.entity.Place;
import com.google.places.showcase.event.PlaceDetailsLoadResponse;

import java.lang.reflect.Type;

/**
 * Json deserializer to {@link com.google.places.showcase.utils.PlaceDetailsDeserializer}
 */
public class PlaceDetailsDeserializer implements JsonDeserializer<PlaceDetailsLoadResponse> {
    private static final String KEY_RESULT = "result";

    @Override
    public PlaceDetailsLoadResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement element = json.getAsJsonObject().get(KEY_RESULT);
        Place place = context.deserialize(element, Place.class);

        PlaceDetailsLoadResponse result = new PlaceDetailsLoadResponse(place);
        CommonUtil.checkResponseForErrors(json, result);
        return result;
    }
}
