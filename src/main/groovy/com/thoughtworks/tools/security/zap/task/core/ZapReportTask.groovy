package com.thoughtworks.tools.security.zap.task.core

import com.thoughtworks.tools.security.zap.utils.ZapClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ZapReportTask extends DefaultTask {
    @TaskAction
    def zapReport() {
        def zapClient = new ZapClient(project.zap.server.host,
                project.zap.server.port,
                project.zap.server.apiKey)

        waitForZapPrepareAlerts()

        zapClient.report(project.zap.target.url)
    }

    private waitForZapPrepareAlerts() {
        sleep(5000)
    }
}
