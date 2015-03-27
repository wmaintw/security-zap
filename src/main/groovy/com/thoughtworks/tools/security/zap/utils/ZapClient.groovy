package com.thoughtworks.tools.security.zap.utils

import com.thoughtworks.tools.security.zap.domain.Reliability
import com.thoughtworks.tools.security.zap.domain.Alert
import com.thoughtworks.tools.security.zap.domain.Risk
import groovyx.net.http.RESTClient

class ZapClient {
    String zapApiBaseUrl = "http://zap/JSON"
    String host
    int port
    String apiKey

    def ZapClient(String host, int port, String apiKey) {
        this.host = host
        this.port = port
        this.apiKey = apiKey
    }

    def shutdown() {
        println("stopping zap")

        def client = getRestClientFor("core/action/shutdown")
        client.get([query: [apikey: apiKey]])
    }

    def excludeFromProxy(List<String> exclude) {
        def client = getRestClientFor("core/action/excludeFromProxy")
        exclude.each { url ->
            client.get([query: [apikey: apiKey, regex: url]])
        }
    }

    def clearExcludedFromProxy() {
        def client = getRestClientFor("core/action/clearExcludedFromProxy")
        client.get([query: [apikey: apiKey]])

        println("exclusion rules are removed")
    }

    def viewExcludedUrls() {
        def client = getRestClientFor("core/view/excludedFromProxy")
        def resp = client.get([query: [apikey: apiKey]])

        println("urls match following regex will be excluded:")
        resp.data.excludedFromProxy.each { regex ->
            println("${regex}")
        }
    }

    def crawl(String startPoint) {
        def client = getRestClientFor("spider/action/scan")
        client.get([query: [apikey: apiKey, url: startPoint]])

        println "crawl triggered"
    }

    def crawlStatus() {
        def client = getRestClientFor("spider/view/status")
        def resp = client.get([query: [apikey: apiKey]])

        resp.data.status as int
    }

    def crawlResult() {
        def client = getRestClientFor("spider/view/results")
        def resp = client.get([query: [apikey: apiKey]])

        resp.data.results
    }

    def excludeFromCrawl(List<String> exclude) {
        def client = getRestClientFor("spider/action/excludeFromScan")
        exclude.each { url ->
            client.get([query: [apikey: apiKey, regex: url]])
        }
    }

    def clearExcludeFromCrawl() {
        def client = getRestClientFor("spider/action/clearExcludedFromScan")
        client.get([query: [apikey: apiKey]])
    }

    def crawlStop() {
        def client = getRestClientFor("spider/action/stop")
        client.get([query: [apikey: apiKey]])
    }

    def scan(String url) {
        def client = getRestClientFor("ascan/action/scan")
        client.get([query: [apikey: apiKey, url: url]])
    }

    def scanStatus() {
        def client = getRestClientFor("ascan/view/status")
        def resp = client.get([query: [apikey: apiKey]])

        resp.data.status as int
    }

    def excludeFromScan(List<String> exclude) {
        def client = getRestClientFor("ascan/action/excludeFromScan")
        exclude.each { url ->
            client.get([query: [apikey: apiKey, regex: url]])
        }
    }

    def viewExcludedFromScan() {
        def client = getRestClientFor("ascan/view/excludedFromScan")
        def resp = client.get(query: [:])

        println("urls match following regex will be excluded from scan:")
        resp.data.excludedFromScan.each { regex ->
            println("${regex}")
        }
    }

    def clearExcludedFromScan() {
        def client = getRestClientFor("ascan/action/clearExcludedFromScan/")
        client.get(query: [apikey: apiKey])
    }

    def setHostPerScan(int numberOfHost) {
        assert numberOfHost >= 1 && numberOfHost <= 5, "invalid number of host per scan, valid value is 1 to 5"

        def client = getRestClientFor("ascan/action/setOptionHostPerScan")
        client.get(query: [apikey: apiKey, Integer: numberOfHost])
    }

    def setThreadPerHost(int numberOfThread) {
        assert numberOfThread >= 1 && numberOfThread <= 50, "invalid number of thread per host, valid value is: 1 to 50"

        def client = getRestClientFor("ascan/action/setOptionThreadPerHost")
        client.get(query: [apikey: apiKey, Integer: numberOfThread])
    }

    def report(String contextUrl) {
        def alertsResult = getAlertsResult(contextUrl)
        def reportHelper = new ReportHelper()
        reportHelper.generateReport(mapToAlert(alertsResult), contextUrl)
    }

    def getAlertsResult(String contextUrl) {
        def client = getRestClientFor("core/view/alerts")
        def resp = client.get([query: [baseurl: contextUrl]])
        resp.data.alerts
    }

    def mapToAlert(List<Map<String, String>> alertsResult) {
        def mappedAlerts = new ArrayList<Alert>()

        for (Map<String, String> item : alertsResult) {
            def alert = new Alert()
            alert.setId(item['id'] as int)
            alert.setAlert(item['alert'])
            alert.setRisk(item['risk'] as Risk)
            alert.setReliability(item['reliability'] as Reliability)
            alert.setUrl(item['url'])
            alert.setOther(item['other'])
            alert.setParam(item['param'])
            alert.setAttack(item['attack'])
            alert.setEvidence(item['evidence'])
            alert.setDescription(item['description'])
            alert.setReference(item['reference'])
            alert.setSolution(item['solution'])
            alert.setCweId(item['cweid'] as int)
            alert.setWascId(item['wascid'] as int)

            mappedAlerts.add(alert)
        }

        mappedAlerts
    }

    def RESTClient getRestClientFor(String apiUrl) {
        def http = new RESTClient()
        http.setProxy(host, port, "http")
        http.setUri("${zapApiBaseUrl}/${apiUrl}")
        http
    }
}
