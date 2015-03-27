Security ZAP Gradle Plugin
------------------------------

## Overview



## Quick Start

要使用`security-zap`插件，只需要按照下面的步骤进行配置就可以了：

**Step 1, 在`build.gradle`文件中应用`security-zap`插件**

```gradle
apply plugin: "security-zap"

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(
                'com.thoughtworks.tools:security-zap:1.0.0'
        )
    }
}
```

**Step 2, 配置gradle的`test`任务**

```gradle
test {
    systemProperty 'zap.proxy', System.properties['zap.proxy']
}
```

**Step 3, 在`build.gradle`文件里配置ZAP**

在进行ZAP的配置之前，请先下载安装[OWASP ZAP Proxy]

```gradle
zap {
    server {
        // ZAP的安装目录(绝对路径)，例如 /Users/wma/Downloads/security/zap-for-linux/ZAP_2.3.1
        home = "/absolute/path/to/ZAP/install/folder"
    }

    target {
        // 目标网站
        url = "http://localhost:8080/WebGoat"
    }
}
```

**Step 4, 为WebDriver设置代理**

```java
public WebDriver setupDriverProxy() {
    DesiredCapabilities cap = new DesiredCapabilities();

    String zapProxySetting = getProperty(ZAP_PROXY_KEY);
    if (StringUtils.isNotBlank(zapProxySetting)) {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(zapProxySetting)
                .setFtpProxy(zapProxySetting)
                .setSslProxy(zapProxySetting);
        cap.setCapability(PROXY, proxy);
    }

    return new FirefoxDriver(cap);
}
```

**Step 5, 启动ZAP并运行测试**

```sh
$ gradle zapStart build -Dzap.proxy=localhost:7070
```

在这行命令里，`zapStart`会从配置`build.gradle`文件里读取ZAP的安装目录信息，把它启动起来, 接下来`build`会运行所有的测试。

你可能发现在命令行里还有`-Dzap.proxy=localhost:7070`这样的设置，它的目的是告诉WebDriver通过`localhost:7070`这个代理来进行测试，而这个代理其实就是ZAP所监听的地址和端口。
做这样的设置主要是为了方便本地开发和测试，因为在本地开发的过程中，你可能不想在每次的测试过程中都运行安全测试（虽然我们推荐你应该尽早、尽量频繁的运行安全测试），只想在代码提交之后，在CI服务器上运行测试的时候顺便运行安全测试，
为了达到这个目的，`security-zap`插件被设计成默认情况下不进行安全测试，除非明确的在命令行里告知WebDriver通过ZAP代理来进行测试。

**Step 6, 生成安全报告**

当所有的WebDriver测试执行完毕后，我们可以让`security-zap`插件生成一份安全报告，命令如下：

```sh
$ gradle zapReport
```

**Step 7, 关闭ZAP**

在完成所有的测试之后，你可以通过下面这条命令关闭ZAP

```sh
$ gradle zapStop
```

## Example

如果你想要查看示例代码，请移步这里：[try-zap]

## Feedback


[OWASP ZAP Proxy]: https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project
[try-zap]: https://github.com/wmaintw/try-zap
