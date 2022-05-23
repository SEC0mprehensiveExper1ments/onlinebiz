package com.njustc.onlinebiz.test.service.report;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.model.report.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MongoReportService implements ReportService {
    @Override
    public String createReport(Report report, Long userId, Role userRole) {
        return null;
    }

    @Override
    public Report findReport(String reportId, Long userId, Role userRole) {
        return null;
    }

    @Override
    public void updateReport(String reportId, Report report, Long userId, Role userRole) {

    }

    @Override
    public void deleteReport(String reportId, Long userId, Role userRole) {

    }
}
