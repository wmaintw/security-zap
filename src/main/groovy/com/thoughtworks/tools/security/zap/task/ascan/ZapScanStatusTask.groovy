package com.thoughtworks.tools.security.zap.task.ascan

import com.thoughtworks.tools.security.zap.utils.ZapClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ZapScanStatusTask extends DefaultTask {
    @TaskAction
    def scanStatus() {
        def zapClient = new ZapClient(project.zap.server.host,
                project.zap.server.port,
                project.zap.server.apiKey)

        println "scan progress: ${zapClient.scanStatus()}%"
    }
}
