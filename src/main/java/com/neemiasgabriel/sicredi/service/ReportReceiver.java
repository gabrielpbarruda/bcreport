package com.neemiasgabriel.sicredi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neemiasgabriel.sicredi.model.AccountInfo;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportReceiver implements MessageListener {

  private ObjectMapper objectMapper = new ObjectMapper();
  public static List<AccountInfo> accounts = new ArrayList<>();

  @Override
  public void onMessage(Message message, byte[] bytes) {
    try {
      accounts = objectMapper.readValue(message.toString(), new TypeReference<List<AccountInfo>>(){});
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
