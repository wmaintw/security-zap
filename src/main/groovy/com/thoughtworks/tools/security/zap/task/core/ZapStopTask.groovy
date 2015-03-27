package com.thoughtworks.tools.security.zap.task.core

import com.thoughtworks.tools.security.zap.utils.ZapClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ZapStopTask extends DefaultTask {
    @TaskAction
    def zapStop() {
        def zapClient = new ZapClient(project.zap.server.host,
                project.zap.server.port,
                project.zap.server.apiKey)
        try {
            zapClient.shutdown()
        } catch (Exception e) {
            println "Warning: failed to stop ZAP due to connection refused or ZAP already stopped."
        }

        sleep(3 * 1000)
    }
}
