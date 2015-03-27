package com.thoughtworks.tools.security.zap.task.spider

import com.thoughtworks.tools.security.zap.utils.ZapClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ZapSpiderStopTask extends DefaultTask {
    @TaskAction
    def stopSpider() {
        def zapClient = new ZapClient(project.zap.server.host,
                project.zap.server.port,
                project.zap.server.apiKey)

        zapClient.crawlStop()
    }
}
