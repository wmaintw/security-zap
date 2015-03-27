package com.thoughtworks.tools.security.zap.utils

import com.thoughtworks.tools.security.zap.domain.Alert
import com.thoughtworks.tools.security.zap.domain.Risk
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.tools.generic.EscapeTool

import static com.thoughtworks.tools.security.zap.domain.Risk.High
import static com.thoughtworks.tools.security.zap.domain.Risk.Informational
import static com.thoughtworks.tools.security.zap.domain.Risk.Low
import static com.thoughtworks.tools.security.zap.domain.Risk.Medium

class TemplateContentGenerator {
    String buildReportContent(List<Alert> alerts, String website, Date reportDate) {
        VelocityContext context = prepareReportContext(alerts, website, reportDate)

        StringWriter writer = new StringWriter()

        Template reportTemplate = getVelocityTemplate()
        reportTemplate.merge(context, writer)

        return writer.toString()
    }

    private VelocityContext prepareReportContext(List<Alert> alerts, String website, Date reportDate) {
        VelocityContext context = new VelocityContext()
        EscapeTool escapeTool = new EscapeTool()
        context.put("esc", escapeTool)
        context.put("targetWebsite", website)
        context.put("zapRunDate", reportDate)
        context.put("numberOfAlerts", alerts.size())

        HashMap<Risk, List<Alert>> alertsBySeverity = groupAlertsBySeverity(alerts)
        context.put("numberOfHighAlerts", alertsBySeverity.get(High).size())
        context.put("numberOfMediumAlerts", alertsBySeverity.get(Medium).size())
        context.put("numberOfLowAlerts", alertsBySeverity.get(Low).size())
        context.put("numberOfInformationalAlerts", alertsBySeverity.get(Informational).size())

        context.put("alertsByClassification", countAlertsByClassification(alerts))

        ArrayList orderedAlerts = new ArrayList()
        orderedAlerts.addAll(alertsBySeverity.get(High))
        orderedAlerts.addAll(alertsBySeverity.get(Medium))
        orderedAlerts.addAll(alertsBySeverity.get(Low))
        orderedAlerts.addAll(alertsBySeverity.get(Informational))
        context.put("alerts", orderedAlerts)

        return context
    }

    private HashMap<Risk, List<Alert>> groupAlertsBySeverity(List<Alert> alerts) {
        HashMap<Risk, List<Alert>> alertsBySeverity = new HashMap<>()
        alertsBySeverity.put(High, new ArrayList<>())
        alertsBySeverity.put(Medium, new ArrayList<>())
        alertsBySeverity.put(Low, new ArrayList<>())
        alertsBySeverity.put(Informational, new ArrayList<>())

        for (Alert alert : alerts) {
            alertsBySeverity.get(alert.getRisk()).add(alert)
        }

        return alertsBySeverity
    }

    def countAlertsByClassification(List<Alert> alerts) {
        def numberOfAlertsByClassification = [:]

        alerts.each { alert ->
            def numberOfAlert = numberOfAlertsByClassification.get(alert.alert)
            if (numberOfAlert == null) {
                numberOfAlertsByClassification.put(alert.alert, [amount: 1, severity: alert.risk])
            } else {
                numberOfAlertsByClassification.get(alert.alert).amount += 1
            }
        }

        numberOfAlertsByClassification.sort { a, b -> b.value.severity <=> a.value.severity}
    }

    private Template getVelocityTemplate() {
        Properties properties = new Properties()
        properties.setProperty("resource.loader", "class")
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
        VelocityEngine engine = new VelocityEngine(properties)

        return engine.getTemplate("alertReportTemplate.vm")
    }
}
