package com.iDoctor.iDoctor;

import org.springframework.stereotype.Component;

@Component
public class EnvironmentAccess {


    private volatile Long doctorId = Long.valueOf(1);

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    private volatile String apiKey = null;

    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}


