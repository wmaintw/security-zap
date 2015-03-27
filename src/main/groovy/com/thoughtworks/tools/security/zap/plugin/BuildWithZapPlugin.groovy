package com.thoughtworks.tools.security.zap.plugin

import com.thoughtworks.tools.security.zap.extension.ScanExtension
import com.thoughtworks.tools.security.zap.extension.ZapConfigurationExtension
import com.thoughtworks.tools.security.zap.task.ascan.ZapScanStatusTask
import com.thoughtworks.tools.security.zap.task.ascan.ZapScanTask
import com.thoughtworks.tools.security.zap.task.spider.ZapSpiderResultTask
import com.thoughtworks.tools.security.zap.extension.ServerExtension
import com.thoughtworks.tools.security.zap.extension.TargetExtension
import com.thoughtworks.tools.security.zap.task.core.ZapReportTask
import com.thoughtworks.tools.security.zap.task.core.ZapStartTask
import com.thoughtworks.tools.security.zap.task.core.ZapStopTask
import com.thoughtworks.tools.security.zap.task.spider.ZapSpiderStopTask
import com.thoughtworks.tools.security.zap.task.spider.ZapSpiderTask
import org.apache.commons.lang.SystemUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildWithZapPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create("zap", ZapConfigurationExtension)
        project.zap.extensions.create("server", ServerExtension)
        project.zap.extensions.create("target", TargetExtension)
        project.zap.extensions.create("scan", ScanExtension)

        project.tasks.create("zapReport", ZapReportTask)
        project.tasks.create("zapStop", ZapStopTask)

        project.tasks.create("zapStart", ZapStartTask)
        project.tasks.getByName("zapStart").doLast {
            verifyZapStarted()
            applyExclusion()
        }
        project.tasks.getByName("zapStart").dependsOn(project.tasks.getByName("zapStop"))

        project.tasks.create("zapCrawl", ZapSpiderTask)
        project.tasks.create("zapCrawlStop", ZapSpiderStopTask)
        project.tasks.create("zapCrawlResult", ZapSpiderResultTask)

        project.tasks.create("zapScan", ZapScanTask)
        project.tasks.getByName("zapScan").dependsOn(project.tasks.getByName("zapCrawl"))
        
        project.tasks.create("zapScanStatus", ZapScanStatusTask)
    }
}
