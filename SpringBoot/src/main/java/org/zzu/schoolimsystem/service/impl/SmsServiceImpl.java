package org.zzu.schoolimsystem.service.impl;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zzu.schoolimsystem.service.SmsService;

import java.util.concurrent.CompletableFuture;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.sign-name}")
    private String signName;

    @Value("${aliyun.sms.template-code}")
    private String templateCode;


    @Override
    public void SimulatesendVerifySms(String phone, String code, String orderNo) {
        System.out.println("========== 模拟短信发送 ==========");
        System.out.println("手机号: " + phone);
        System.out.println("工单号: " + orderNo);
        System.out.println("验证码: " + code);
        System.out.println("确认链接: https://islet.v.zzu.edu.cn/z/school/uc?orderId=" + orderNo);
        System.out.println("================================");
    }

    @Override
    public void sendVerifySms(String phone, String code, String orderNo) {
        AsyncClient client = null;
        try {
            StaticCredentialProvider provider = StaticCredentialProvider.create(
                    Credential.builder()
                            .accessKeyId(accessKeyId)
                            .accessKeySecret(accessKeySecret)
                            .build()
            );

            client = AsyncClient.builder()
                    .region("cn-qingdao")
                    .credentialsProvider(provider)
                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride("dysmsapi.aliyuncs.com")
                    )
                    .build();

            String templateParam = String.format(
                    "{\"code\":\"%s\",\"orderNo\":\"%s\"}",
                    code, orderNo
            );

            SendSmsRequest request = SendSmsRequest.builder()
                    .signName(signName)
                    .templateCode(templateCode)
                    .phoneNumbers(phone)
                    .templateParam(templateParam)
                    .build();

            CompletableFuture<SendSmsResponse> response = client.sendSms(request);
            SendSmsResponse resp = response.get();

            if (resp.getBody() == null || !"OK".equals(resp.getBody().getCode())) {
                throw new RuntimeException("短信发送失败：" +
                        (resp.getBody() == null ? "响应为空" : resp.getBody().getMessage()));
            }

        } catch (Exception e) {
            throw new RuntimeException("短信发送异常：" + e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
}
