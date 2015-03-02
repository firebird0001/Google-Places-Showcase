package com.google.places.showcase.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.places.showcase.entity.Place;
import com.google.places.showcase.event.PlaceDetailsLoadResponse;
import com.google.places.showcase.event.PlacesLoadResponse;
import com.google.places.showcase.utils.PlaceDetailsDeserializer;
import com.google.places.showcase.utils.PlaceListDeserializer;

import junit.framework.TestCase;

/**
 * Json deserializer tests
 */
public class JsonDeserializerTest extends TestCase {

    private static final String PLACE_LIST_RESPONSE = "{   \"html_attributions\" : [      \"Listings by \\u003ca href=\\\"http://www.yellowpages.com.au/\\\"\\u003eYellow Pages\\u003c/a\\u003e\"   ],   \"results\" : [      {         \"formatted_address\" : \"529 Kent Street, Sydney NSW, Australia\",         \"geometry\" : {            \"location\" : {               \"lat\" : -33.8750460,               \"lng\" : 151.2052720            }         },         \"icon\" : \"http://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png\",         \"id\" : \"827f1ac561d72ec25897df088199315f7cbbc8ed\",         \"name\" : \"Tetsuya's\",         \"rating\" : 4.30,         \"reference\" : \"CnRmAAAAmmm3dlSVT3E7rIvwQ0lHBA4sayvxWEc4nZaXSSjRtfKRGoYnfr3d5AvQGk4e0u3oOErXsIJwtd3Wck1Onyw6pCzr8swW4E7dZ6wP4dV6AsXPvodwdVyqHgyGE_K8DqSp5McW_nFcci_-1jXb5Phv-RIQTzv5BjIGS0ufgTslfC6dqBoU7tw8NKUDHg28bPJlL0vGVWVgbTg\",         \"types\" : [ \"restaurant\", \"food\", \"establishment\" ]      },      {         \"formatted_address\" : \"Upper Level, Overseas Passenger Terminal/5 Hickson Road, The Rocks NSW, Australia\",         \"geometry\" : {            \"location\" : {               \"lat\" : -33.8583790,               \"lng\" : 151.2100270            }         },         \"icon\" : \"http://maps.gstatic.com/mapfiles/place_api/icons/cafe-71.png\",         \"id\" : \"f181b872b9bc680c8966df3e5770ae9839115440\",         \"name\" : \"Quay\",         \"rating\" : 4.10,         \"reference\" : \"CnRiAAAADmPDOkn3znv_fX78Ma6X5_t7caEGNdSWnpwMIdDNZkLpVKPnQJXP1ghlySO-ixqs28UtDmJaOlCHn18pxpj7UQjRzR4Kmye6Gijoqoox9bpkaCAJatbJGZEIIUwRbTNIE_L2jGo5BDqiosqU2F5QdBIQbXKrvfQuo6rmu8285j7bDBoUrGrN4r6XQ-PVm260PFt5kwc3EfY\",         \"types\" : [ \"cafe\", \"bar\", \"restaurant\", \"food\", \"establishment\" ]      },      {         \"formatted_address\" : \"107 George Street, The Rocks NSW, Australia\",         \"geometry\" : {            \"location\" : {               \"lat\" : -33.8597750,               \"lng\" : 151.2085920            }         },         \"icon\" : \"http://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png\",         \"id\" : \"7beacea28938ae42bcac04faf79a607bf84409e6\",         \"name\" : \"Rockpool\",         \"rating\" : 4.0,         \"reference\" : \"CnRlAAAAVK4Ek78r9yHV56I-zbaTxo9YiroCbTlel-ZRj2i6yGAkLwNMm_flMhCl3j8ZHN-jJyG1TvKqBBnKQS2z4Tceu-1kZupZ1HSo5JWRBKd7qt2vKgT8VauiEBQL-zJiKVzSy5rFfilKDLSiLusmdi88ThIQqqj6hKHn5awdj6C4f59ifRoUg67KlbpuGuuW7S1tAH_EyBl6KE4\",         \"types\" : [ \"restaurant\", \"food\", \"establishment\" ]      },      {         \"formatted_address\" : \"483 George Street, Sydney NSW, Australia\",         \"events\" : [            {              \"event_id\" : \"7lH_gK1GphU\",              \"summary\" : \"Google Maps Developer Meetup: Rockin' out with the Places API\",              \"url\" : \"https://developers.google.com/places\"            }          ],         \"geometry\" : {            \"location\" : {               \"lat\" : -33.8731950,               \"lng\" : 151.2063380            }         },         \"icon\" : \"http://maps.gstatic.com/mapfiles/place_api/icons/civic_building-71.png\",         \"id\" : \"017049cb4e82412aaf0efbde890e82b7f2987c16\",         \"name\" : \"Chinatown Sydney\",         \"rating\" : 4.0,         \"reference\" : \"CnRuAAAAsLNeRQtKD7TEUXWG6gYD7ByOVKjQE61GSyeGZrX-pOPVps2BaLBlH0zBHlrVU9DKhsuXra075loWmZUCbczKDPdCaP9FVJXB2NsZ1q7188pqRFik58S9Z1lcWjyVoVqvdUUt9bDMLqxVT4ENmolbgBIQ9Wy0sgDy0BgWyg5kfPMHCxoUOvmhfKC-lTefXGgnsRqEQwn8M0I\",         \"types\" : [            \"city_hall\",            \"park\",            \"restaurant\",            \"doctor\",            \"train_station\",            \"local_government_office\",            \"food\",            \"health\",            \"establishment\"         ]      }   ],   \"status\" : \"OK\"}";
    private static final String PLACE_DETAILS_RESPONSE = "{   \"html_attributions\" : [],   \"result\" : {      \"address_components\" : [         {            \"long_name\" : \"48\",            \"short_name\" : \"48\",            \"types\" : [ \"street_number\" ]         },         {            \"long_name\" : \"Pirrama Road\",            \"short_name\" : \"Pirrama Road\",            \"types\" : [ \"route\" ]         },         {            \"long_name\" : \"Pyrmont\",            \"short_name\" : \"Pyrmont\",            \"types\" : [ \"locality\", \"political\" ]         },         {            \"long_name\" : \"NSW\",            \"short_name\" : \"NSW\",            \"types\" : [ \"administrative_area_level_1\", \"political\" ]         },         {            \"long_name\" : \"AU\",            \"short_name\" : \"AU\",            \"types\" : [ \"country\", \"political\" ]         },         {            \"long_name\" : \"2009\",            \"short_name\" : \"2009\",            \"types\" : [ \"postal_code\" ]         }      ],      \"events\" : [        {          \"event_id\" : \"9lJ_jK1GfhX\",          \"start_time\" : 1293865200,          \"summary\" : \"<p>A visit from author John Doe, who will read from his latest book.</p>                       <p>A limited number of signed copies will be available.</p>\",          \"url\" : \"http://www.example.com/john_doe_visit.html\"        }      ],      \"formatted_address\" : \"48 Pirrama Road, Pyrmont NSW, Australia\",      \"formatted_phone_number\" : \"(02) 9374 4000\",      \"geometry\" : {         \"location\" : {           \"lat\" : -33.8669710,           \"lng\" : 151.1958750         }      },      \"icon\" : \"http://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png\",      \"id\" : \"4f89212bf76dde31f092cfc14d7506555d85b5c7\",      \"international_phone_number\" : \"+61 2 9374 4000\",      \"name\" : \"Google Sydney\",      \"rating\" : 4.70,      \"reference\" : \"CnRsAAAA98C4wD-VFvzGq-KHVEFhlHuy1TD1W6UYZw7KjuvfVsKMRZkbCVBVDxXFOOCM108n9PuJMJxeAxix3WB6B16c1p2bY1ZQyOrcu1d9247xQhUmPgYjN37JMo5QBsWipTsnoIZA9yAzA-0pnxFM6yAcDhIQbU0z05f3xD3m9NQnhEDjvBoUw-BdcocVpXzKFcnMXUpf-nkyF1w\",      \"reviews\" : [         {            \"aspects\" : [               {                  \"rating\" : 3,                  \"type\" : \"quality\"               }            ],            \"author_name\" : \"Simon Bengtsson\",            \"author_url\" : \"https://plus.google.com/104675092887960962573\",            \"text\" : \"Just went inside to have a look at Google. Amazing.\",            \"time\" : 1338440552869         },         {           \"aspects\" : [              {                 \"rating\" : 3,                 \"type\" : \"quality\"              }             ],            \"author_name\" : \"Felix Rauch Valenti\",            \"author_url\" : \"https://plus.google.com/103291556674373289857\",            \"text\" : \"Best place to work :-)\",            \"time\" : 1338411244325         },         {           \"aspects\" : [              {                 \"rating\" : 3,                 \"type\" : \"quality\"              }             ],            \"author_name\" : \"Chris\",            \"text\" : \"Great place to work, always lots of free food!\",            \"time\" : 1330467089039         }      ],      \"types\" : [ \"establishment\" ],      \"url\" : \"http://maps.google.com/maps/place?cid=10281119596374313554\",      \"vicinity\" : \"48 Pirrama Road, Pyrmont\",      \"website\" : \"http://www.google.com.au/\"   },   \"status\" : \"OK\"}";
    private static final String BAD_RESPONSE_STATUS = "{\"status\" : \"REQUEST_DENIED\"}";
    private static final String BAD_RESPONSE_ERROR_MESSAGE = "{\"error_message\":\"some error\"}";

    public void testPlaceListDeserializer() {
        // init json element
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(PLACE_LIST_RESPONSE);

        // init Gson
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PlacesLoadResponse.class, new PlaceListDeserializer())
                .create();

        // parse response
        PlacesLoadResponse loadResponse = gson.fromJson(jsonElement, PlacesLoadResponse.class);

        // check if response is filled correctly
        assertNotNull(loadResponse);
        assertNotNull(loadResponse.getPlaces());
        assertEquals(loadResponse.getPlaces().size(), 4);

        Place place = loadResponse.getPlaces().get(3);
        assertEquals("483 George Street, Sydney NSW, Australia", place.getAddress());
        assertNotNull(place.getLocation());
        assertEquals("http://maps.gstatic.com/mapfiles/place_api/icons/civic_building-71.png", place.getIcon());
        assertEquals("017049cb4e82412aaf0efbde890e82b7f2987c16", place.getId());
        assertEquals("Chinatown Sydney", place.getName());
        assertTrue(place.getRating() != 0);
        assertNotNull(place.getTypes());
        assertEquals(place.getTypes().size(), 9);
    }

    public void testPlaceDetailsDeserializer() {
        // init json element
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(PLACE_DETAILS_RESPONSE);

        // init Gson
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PlaceDetailsLoadResponse.class, new PlaceDetailsDeserializer())
                .create();

        // parse response
        PlaceDetailsLoadResponse loadResponse = gson.fromJson(jsonElement, PlaceDetailsLoadResponse.class);

        // check if response is filled correctly
        assertNotNull(loadResponse);

        Place place = loadResponse.getPlace();
        assertNotNull(place);
        assertEquals("48 Pirrama Road, Pyrmont NSW, Australia", loadResponse.getPlace().getAddress());
        assertNotNull(place.getLocation());
        assertEquals("http://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png", place.getIcon());
        assertEquals("4f89212bf76dde31f092cfc14d7506555d85b5c7", place.getId());
        assertEquals("+61 2 9374 4000", place.getPhone());
        assertEquals("Google Sydney", place.getName());
        assertTrue(place.getRating() > 0);
        assertNotNull(place.getTypes());
        assertEquals(place.getTypes().size(), 1);
        assertEquals("http://maps.google.com/maps/place?cid=10281119596374313554", place.getUrl());
        assertEquals("http://www.google.com.au/", place.getWebsite());
    }

    public void testBadResponseStatus() {
        checkBadResponse(BAD_RESPONSE_STATUS);
    }

    public void testBadResponseErrorMessage() {
        checkBadResponse(BAD_RESPONSE_ERROR_MESSAGE);
    }

    private void checkBadResponse(String response) {
        // init json element
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response);

        // init Gson
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PlaceDetailsLoadResponse.class, new PlaceDetailsDeserializer())
                .create();

        // parse response
        PlaceDetailsLoadResponse loadResponse = gson.fromJson(jsonElement, PlaceDetailsLoadResponse.class);

        // check if response is considered bad
        assertNotNull(loadResponse);
        assertTrue(loadResponse.isBadResponse());
    }
}
