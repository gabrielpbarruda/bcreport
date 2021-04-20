package com.neemiasgabriel.sicredi.controller;

import com.neemiasgabriel.sicredi.service.ReportReceiver;
import com.neemiasgabriel.sicredi.service.ReportService;
import com.neemiasgabriel.sicredi.utils.CSVHelper;
import com.neemiasgabriel.sicredi.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ReportController {
  private final ReportService reportService;
  private final ReportReceiver reportReceiver;

  @PostMapping("/upload-file")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (file != null) {
      try {
        reportService.processReport(file);

        message = String.format("Uploaded the file succesfully %s", file.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = String.format("Could not upload the file: %s!", file.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @GetMapping("/bcReportEndPoint")
  public ResponseEntity<byte[]> getAccounts() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", "bcReport.csv"));
    byte[] report = CSVHelper.generateReport(reportReceiver.accounts);

    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(report.length)
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .body(report);
  }
}
