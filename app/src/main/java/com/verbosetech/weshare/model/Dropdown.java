
package com.verbosetech.weshare.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dropdown {

    @SerializedName("caste")
    @Expose
    private List<Caste> caste = null;
    @SerializedName("state")
    @Expose
    private List<State> state = null;
    @SerializedName("city")
    @Expose
    private List<City> city = null;
    @SerializedName("mothertounge")
    @Expose
    private List<Mothertounge> mothertounge = null;

    public List<Caste> getCaste() {
        return caste;
    }

    public void setCaste(List<Caste> caste) {
        this.caste = caste;
    }

    public List<State> getState() {
        return state;
    }

    public void setState(List<State> state) {
        this.state = state;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

    public List<Mothertounge> getMothertounge() {
        return mothertounge;
    }

    public void setMothertounge(List<Mothertounge> mothertounge) {
        this.mothertounge = mothertounge;
    }

}
