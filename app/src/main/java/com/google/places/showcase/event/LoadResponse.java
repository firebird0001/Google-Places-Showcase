package com.google.places.showcase.event;

import android.text.TextUtils;

import com.google.places.showcase.utils.CommonUtil;

/**
 * Base class for load responses
 */
public class LoadResponse {
    private String mStatus;
    private String mErrorMessage;

    public boolean isBadResponse() {
        return !TextUtils.isEmpty(mErrorMessage) || !CommonUtil.STATUS_OK.equals(mStatus);
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }
}
