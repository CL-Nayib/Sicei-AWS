package com.castul.AwsREST.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SimpleNotificationService {

   @Value("${aws.sns.topic.arn}")
   private String topicArn;

   private final SnsClient snsClient;

   public SimpleNotificationService(SnsClient snsClient) {
      this.snsClient = snsClient;
   }

   public String sendNotification(String subject, String message) {
      PublishRequest request = PublishRequest.builder()
            .topicArn(topicArn)
            .subject(subject)
            .message(message)
            .build();

      PublishResponse response = snsClient.publish(request);
      return response.messageId();
   }
}