package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {

    boolean send(Map<String, Object> param, String phone);

    void sendEmail(String email, String code);

    String getCode();
}
