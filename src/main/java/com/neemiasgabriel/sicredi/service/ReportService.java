package com.neemiasgabriel.sicredi.service;

import org.springframework.web.multipart.MultipartFile;

public interface ReportService {

  void processReport(MultipartFile file);
}
