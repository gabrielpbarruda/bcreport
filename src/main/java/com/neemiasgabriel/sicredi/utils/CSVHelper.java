package com.neemiasgabriel.sicredi.utils;

import com.neemiasgabriel.sicredi.model.AccountInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
  public static byte[] generateReport(List<AccountInfo> accounts) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

      CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL
        .withHeader("agencia", "conta", "saldo", "status", "result").withDelimiter(';'));

      for (AccountInfo account : accounts) {
        csvPrinter.printRecord(
          account.getAgencia(),
          account.getConta(),
          account.getSaldo(),
          account.getStatus(),
          account.getResult());
      }

      csvPrinter.flush();

      return out.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static List<AccountInfo> csvToAccounts(InputStream is) {
    try {
      BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
      CSVParser csvParser = new CSVParser(
        fileReader,
        CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withDelimiter(';'));

      List<AccountInfo> accounts = new ArrayList<>();

      csvParser.getRecords().forEach(csvRecord -> {
        AccountInfo account = new AccountInfo(
          csvRecord.get("agencia"),
          csvRecord.get("conta"),
          Double.parseDouble(csvRecord.get("saldo").replace(",", ".")),
          csvRecord.get("status")
        );

        accounts.add(account);
      });

      return accounts;
    } catch (IOException e) {
      throw new RuntimeException(String.format("Fail to parse CSV file: %s", e.getMessage()));
    }
  }
}
