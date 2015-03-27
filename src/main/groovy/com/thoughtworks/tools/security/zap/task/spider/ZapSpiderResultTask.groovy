package com.thoughtworks.tools.security.zap.task.spider

import com.thoughtworks.tools.security.zap.utils.ZapClient
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ZapSpiderResultTask extends DefaultTask {

    @TaskAction
    def crawlResult() {
        println("Crawl result:")

        def zapClient = new ZapClient(project.zap.server.host,
                project.zap.server.port,
                project.zap.server.apiKey)

        println zapClient.crawlResult()
    }
}
