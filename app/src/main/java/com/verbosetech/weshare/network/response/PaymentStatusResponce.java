package com.verbosetech.weshare.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentStatusResponce {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("payfor")
    @Expose
    private String payfor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayfor() {
        return payfor;
    }

    public void setPayfor(String payfor) {
        this.payfor = payfor;
    }
}
