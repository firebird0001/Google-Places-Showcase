package com.google.places.showcase.test.mock;

import android.net.Uri;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Mock retrofit client with slow response time
 */
public class MockClient implements Client {

    private static final String RESPONSE_STRING = "response string";
    private static final int HTTP_STATUS_OK = 200;
    // request delay in seconds
    private static final int REQUEST_DELAY = 30;

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());

        System.out.println("fetching uri: " + uri.toString());

        try {
            TimeUnit.SECONDS.sleep(REQUEST_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Response(request.getUrl(), HTTP_STATUS_OK, "nothing", Collections.EMPTY_LIST,
                new TypedByteArray("application/json", RESPONSE_STRING.getBytes()));
    }
}