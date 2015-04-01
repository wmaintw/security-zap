package com.thoughtworks.tools.security.zap.task.core

import com.thoughtworks.tools.security.zap.utils.ReportHelper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import static groovy.io.FileType.FILES

class ZapPurgeReportTask extends DefaultTask {
    @TaskAction
    def zapPurgeReport() {
        def amountOfFilesToRetrain = project.zap.target.amountOfReportFilesToRetrain
        def files = getAllReportFiles()

        if (files.size() > amountOfFilesToRetrain) {
            files = files.sort { a, b -> a.lastModified() <=> b.lastModified() }.reverse()

            for (int i = amountOfFilesToRetrain; i < files.size(); i++) {
                files[i].delete()
            }
        }
    }

    def getAllReportFiles() {
        def files = []
        def helper = new ReportHelper()
        helper.getZapReportDir().eachFile(FILES) { file -> files << file }
        files
    }
}
