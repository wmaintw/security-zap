package com.thoughtworks.tools.security.zap.domain

enum Risk {
    Informational(1),
    Low(2),
    Medium(3),
    High(4);

    private final int severity

    private Risk(int severity) {
        this.severity = severity
    }
}