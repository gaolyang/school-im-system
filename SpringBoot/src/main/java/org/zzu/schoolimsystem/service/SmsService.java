package org.zzu.schoolimsystem.service;

public interface SmsService {
    void sendVerifySms(String phone, String code, String orderNo);
    void SimulatesendVerifySms(String phone, String code, String orderNo);
}
