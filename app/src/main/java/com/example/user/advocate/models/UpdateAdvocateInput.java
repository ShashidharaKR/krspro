package com.example.user.advocate.models;

public class UpdateAdvocateInput {
    public String id;
    public String cases;
    public String experience;
    public String dob;
    public String lattitude;
    public String longitude;
    public String type;

    public UpdateAdvocateInput(String id,String cases, String experience, String dob, String lattitude, String longitude, String type) {
        this.id = id;
        this.cases = cases;
        this.experience = experience;
        this.dob = dob;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.type = type;
    }
}
