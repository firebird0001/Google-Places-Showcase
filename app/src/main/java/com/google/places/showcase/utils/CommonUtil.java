package com.google.places.showcase.utils;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.places.showcase.R;
import com.google.places.showcase.event.LoadResponse;
import com.google.places.showcase.fragment.ProgressDialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Common keys and utils
 */
public final class CommonUtil {
    public static final String API_KEY = "AIzaSyBzp3L8cf5nP9bzTT2AiVTnnTsQEpK9ETo";//"AIzaSyBpnXflfU3RtKVaHQttZgpY02JjO_iLVPc";
    public static final String KEY_PLACE_ID = "placeId";
    public static final String STATUS_OK = "OK";

    private static final String KEY_STATUS = "status";
    private static final String KEY_ERROR_MESSAGE = "error_message";
    private static final String PHOTO_URI
            = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    private static final String TAG = "Util";
    private static final String TAG_PROGRESS_DIALOG = "progressDialog";

    /**
     * Get image URI from photo reference
     * @param photoReference value from Places API
     * @return image string URI
     */
    public static String getImageUri(String photoReference) {
        return PHOTO_URI + photoReference + "&key=" + CommonUtil.API_KEY;
    }

    /**
     * Show progress dialog
     * @param fragmentManager
     */
    public static void showProgressDialog(FragmentManager fragmentManager) {
        ProgressDialogFragment progressDialog = ProgressDialogFragment.newInstance();
        progressDialog.setCancelable(false);
        progressDialog.show(fragmentManager, TAG_PROGRESS_DIALOG);
    }

    /**
     * Hide progress dialog
     * @param fragmentManager
     */
    public static void hideProgressDialog(FragmentManager fragmentManager) {
        ProgressDialogFragment progressDialog = (ProgressDialogFragment) fragmentManager
                .findFragmentByTag(TAG_PROGRESS_DIALOG);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * Read text file to string
     */
    public static String readAssetFileToString(Context context, String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = context.getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ( (str = in.readLine()) != null ) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing asset " + name);
                }
            }
        }

        return null;
    }

    /**
     * Check json response and fill {@link com.google.places.showcase.event.LoadResponse}
     * with status and error message (if available)
     * @param element json response
     * @param response {@link com.google.places.showcase.event.LoadResponse} instance
     */
    public static void checkResponseForErrors(JsonElement element, LoadResponse response) {
        if (element != null) {
            JsonElement statusElement = element.getAsJsonObject().get(KEY_STATUS);
            if (statusElement == null) {
                response.setErrorMessage("Invalid response - no status");
                return;
            }

            String status = statusElement.getAsString();
            response.setStatus(status);
            if (!STATUS_OK.equals(status)) {
                JsonElement errorMessageElement = element.getAsJsonObject().get(KEY_ERROR_MESSAGE);
                if (errorMessageElement == null) {
                    response.setErrorMessage("Bad status received");
                } else {
                    response.setErrorMessage(errorMessageElement.getAsString());
                }
            }
        }
    }

    /**
     * Show info to user about bad response
     */
    public static void reportBadResponse(Context context, LoadResponse response) {
        Toast.makeText(context,
                context.getString(R.string.bad_response) + response.getErrorMessage(),
                Toast.LENGTH_LONG).show();
    }
}
