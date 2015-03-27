package com.thoughtworks.tools.security.zap.extension

class ScanExtension {
    String crawlFrom
    List<String> exclude
    int timeout
    int hostPerScan = 2
    int threadPerHost = 2
}
