package com.thoughtworks.tools.security.zap.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.thoughtworks.tools.security.zap.domain.Alert

import java.nio.file.Paths

import static org.apache.commons.io.FileUtils.deleteQuietly
import static org.apache.commons.io.FileUtils.writeStringToFile

class ReportHelper {
    def generateReport(List<Alert> alerts, String website) {
        def reportDate = new Date()
        writeToJsonFile(alerts, reportDate)
        writeToHtmlFile(alerts, website, reportDate)
    }

    def writeToJsonFile(List<Alert> alerts, Date reportDate) {
        def zapReportDir = getZapReportDir()
        File zapAlertsData = new File(zapReportDir, "zap-alerts-${reportDate.getTime()}.json");

        deleteQuietly(zapAlertsData);

        def mapper = new ObjectMapper()
        writeStringToFile(zapAlertsData, mapper.writeValueAsString(alerts));
    }

    def writeToHtmlFile(List<Alert> alerts, String website, Date reportDate) {
        def zapReportDir = getZapReportDir()
        File zapAlertsReport = new File(zapReportDir, "zap-alerts-${reportDate.getTime()}.html");

        deleteQuietly(zapAlertsReport);

        def contentGenerator = new TemplateContentGenerator()
        writeStringToFile(zapAlertsReport, contentGenerator.buildReportContent(alerts, website, reportDate));

        println "ZAP reports generated at: ${zapAlertsReport.absolutePath}"
    }

    def getZapReportDir() {
        String currentWorkingDir = Paths.get("").toAbsolutePath().toString()
        new File("${currentWorkingDir}/zap-reports")
    }
}
