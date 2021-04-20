package com.neemiasgabriel.sicredi.service;

import com.neemiasgabriel.sicredi.model.AccountInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.neemiasgabriel.sicredi.utils.CSVHelper.csvToAccounts;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
  private final ReceitaService receitaService;
  private final RedisTemplate template;
  private final ChannelTopic topic;

  @Override
  public void processReport(MultipartFile file) {
    try {
      List<AccountInfo> accounts = csvToAccounts(file.getInputStream());

      for (AccountInfo account : accounts) {
        boolean result = receitaService.atualizarConta(
          account.getAgencia(),
          account.getConta(),
          account.getSaldo(),
          account.getStatus());

        account.setResult(result ? "OK" : "Fail");
      }

      template.convertAndSend(topic.getTopic(), accounts);

    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(String.format("Fail to process CSV file: %s", e.getMessage()));
    }
  }
}
