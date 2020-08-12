package com.example.amscopy.service.api;

import java.io.InputStream;

public interface ExcelService {

    Object batchImport(InputStream is, Integer orgId, String loginId);
}
