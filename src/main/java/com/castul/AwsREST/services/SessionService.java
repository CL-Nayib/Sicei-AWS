package com.castul.AwsREST.services;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Service
public class SessionService {

   private final DynamoDbClient dynamoDbClient;

   public SessionService(DynamoDbClient dynamoDbClient) {
      this.dynamoDbClient = dynamoDbClient;
   }

   public void createSession(int alumnoId, String sessionString) {
      String id = UUID.randomUUID().toString();

      Map<String, AttributeValue> item = new HashMap<>();
      item.put("id", AttributeValue.builder().s(id).build());
      item.put("fecha", AttributeValue.builder().n(String.valueOf(System.currentTimeMillis() / 1000)).build());
      item.put("alumnoId", AttributeValue.builder().n(String.valueOf(alumnoId)).build());
      item.put("active", AttributeValue.builder().bool(true).build());
      item.put("sessionString", AttributeValue.builder().s(sessionString).build());

      dynamoDbClient.putItem(PutItemRequest.builder()
            .tableName("sesiones-alumnos")
            .item(item)
            .build());
   }

   public Map<String, AttributeValue> getSession(int alumnoId, String sessionString) {
      try {
         // Crear un filtro para buscar por sessionString
         Map<String, AttributeValue> expressionValues = new HashMap<>();
         expressionValues.put(":alumnoId", AttributeValue.builder().n(String.valueOf(alumnoId)).build());
         expressionValues.put(":sessionString", AttributeValue.builder().s(sessionString).build());

         ScanRequest scanRequest = ScanRequest.builder()
               .tableName("sesiones-alumnos")
               .filterExpression("alumnoId = :alumnoId AND sessionString = :sessionString")
               .expressionAttributeValues(expressionValues)
               .build();

         // Ejecutar el scan
         ScanResponse response = dynamoDbClient.scan(scanRequest);

         // Verificar si hay resultados
         if (!response.items().isEmpty()) {
            return response.items().get(0); // Devolver el primer resultado
         } else {
            return null; // No se encontró ningún registro
         }

      } catch (Exception e) {
         System.err.println("Error al consultar la sesión: " + e.getMessage());
         return null;
      }
   }

   public boolean logoutSession(String sessionString) {
      Map<String, AttributeValue> expressionValues = new HashMap<>();
      expressionValues.put(":sessionString", AttributeValue.builder().s(sessionString).build());

      ScanRequest scanRequest = ScanRequest.builder()
            .tableName("sesiones-alumnos")
            .filterExpression("sessionString = :sessionString")
            .expressionAttributeValues(expressionValues)
            .build();

      ScanResponse response = dynamoDbClient.scan(scanRequest);

      if (response.items().isEmpty()) {
         return false;
      }

      Map<String, AttributeValue> sessionItem = response.items().get(0);
      String id = sessionItem.get("id").s();

      Map<String, AttributeValueUpdate> updates = new HashMap<>();
      updates.put("active", AttributeValueUpdate.builder()
            .value(AttributeValue.builder().bool(false).build())
            .action(AttributeAction.PUT)
            .build());

      Map<String, AttributeValue> key = new HashMap<>();
      key.put("id", AttributeValue.builder().s(id).build());

      dynamoDbClient.updateItem(UpdateItemRequest.builder()
            .tableName("sesiones-alumnos")
            .key(key)
            .attributeUpdates(updates)
            .build());

      return true;
   }

}
