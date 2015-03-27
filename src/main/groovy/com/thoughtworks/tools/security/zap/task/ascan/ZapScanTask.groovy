package com.thoughtworks.tools.security.zap.task.ascan

import com.thoughtworks.tools.security.zap.utils.ZapClient
import groovy.time.TimeCategory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ZapScanTask extends DefaultTask {

    public static final int DEFAULT_TIMEOUT_THRESHOLD = 60

    @TaskAction
    def startScan() {
        def zapClient = getZapClient()

        println "start to scan the site: ${project.zap.target.url}"

        zapClient.clearExcludedFromScan()
        zapClient.excludeFromScan(project.zap.scan.exclude)
        zapClient.viewExcludedFromScan()

        println "setup ${project.zap.scan.hostPerScan} hosts and ${project.zap.scan.threadPerHost} threads per each host"
        zapClient.setHostPerScan(project.zap.scan.hostPerScan)
        zapClient.setThreadPerHost(project.zap.scan.threadPerHost)

        zapClient.scan(project.zap.target.url)
        waitForScanFinish()
    }

    def waitForScanFinish() {
        def zapClient = getZapClient()
        def scanStatus = 0

        def scanStartTime = new Date()
        println "zap scan started at: ${scanStartTime}"

        while (isScanInProgress(scanStatus) && !isTimeout(scanStartTime)) {
            sleep(10 * 1000)

            scanStatus = zapClient.scanStatus()
            println "progress: ${scanStatus}%"
        }
    }

    private boolean isScanInProgress(scanStatus) {
        scanStatus < 100
    }

    private boolean isTimeout(Date scanStartTime) {
        def now = new Date()
        def timeoutThreshold = getTimeoutThreshold()

        use(TimeCategory) {
            def timeout = now.after(scanStartTime + timeoutThreshold.minutes)

            if (timeout) {
                println "scan timeout, but the scan may still in progress, " +
                        "run gradle task [zapScanStatus] to view the scan progress, " +
                        "or run [zapStop] to force abort the scan, " +
                        "or increase the scan timeout threshold"
            }

            return timeout
        }
    }

    private int getTimeoutThreshold() {
        project.zap.scan.timeout > 0 ? project.zap.scan.timeout : DEFAULT_TIMEOUT_THRESHOLD
    }

    private ZapClient getZapClient() {
        def zapClient = new ZapClient(project.zap.server.host,
                project.zap.server.port,
                project.zap.server.apiKey)
        zapClient
    }
}
