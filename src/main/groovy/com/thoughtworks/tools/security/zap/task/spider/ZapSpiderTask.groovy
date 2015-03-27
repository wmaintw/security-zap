package com.thoughtworks.tools.security.zap.task.spider

import com.thoughtworks.tools.security.zap.utils.ZapClient
import groovy.time.TimeCategory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import static org.apache.commons.lang.StringUtils.isNotBlank

class ZapSpiderTask extends DefaultTask {

    @TaskAction
    def crawlSite() {
        println("Start to crawl the site")

        def zapClient = getZapClient()

        zapClient.clearExcludeFromCrawl()
        zapClient.excludeFromCrawl(project.zap.scan.exclude)
        zapClient.crawl(getStartPoint())
        waitForCrawlFinished()
    }

    def getStartPoint() {
        def startPoint = project.zap.target.url
        if (isNotBlank(project.zap.target.crawlFrom)) {
            startPoint = project.zap.target.crawlFrom
        }
        println "crawl from: ${startPoint}"

        startPoint
    }

    def waitForCrawlFinished() {
        def zapClient = getZapClient()
        def startTime = new Date()

        while (isCrawlInProgress(zapClient.crawlStatus()) && !isTimeout(startTime)) {
            println "crawl in progress"

            use(TimeCategory) {
                sleep(5 * 1000)
            }
        }
    }

    private boolean isCrawlInProgress(int crawlStatus) {
        crawlStatus < 100
    }

    def isTimeout(Date startTime) {
        def now = new Date()
        use(TimeCategory) {
            return now.after(startTime + 5.minutes)
        }
    }

    private ZapClient getZapClient() {
        new ZapClient(project.zap.server.host,
                project.zap.server.port,
                project.zap.server.apiKey)
    }
}
