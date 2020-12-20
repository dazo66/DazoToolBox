package com.dazo66.utils.app

import java.io.File

/**
 * @author Dazo66
 */
class AppDiscover {

    companion object {
        var appDir: String = "app/"
        var appDataDir: String = "data/"

        fun findAppWithName(name: String, apps: List<App>) : App? {
            for (app in apps) {
                if (app.rowName.equals(name)) {
                    return app
                }
            }
            return null
        }
    }

    init {
        val file1 = File(appDir)
        val file2 = File(appDataDir)
        if (!file1.exists()) {
            file1.mkdirs()
        }
        if (!file2.exists()) {
            file2.mkdirs()
        }
    }

    /**
     * 根据 [policy(policyArgs)] name这样的格式进行引用搜索
     * 扫描文件目录下的所有的可执行文件
     * 如果需要运行参数请用bat文件引用执行
     * 可执行文件的数据目录会放在data下的各个app的名字下面
     */
    fun scanApp(oldApps: MutableList<App>) : List<App> {
        var returnApp: MutableList<App> = ArrayList()
        var files = File(appDir).listFiles()
        for (file in files) {
            var app = findAppWithName(file.name, oldApps)
            if (app == null) {
                returnApp.add(buildApp(file))
            } else {
                if (app.updateDate != file.lastModified()) {
                    app.stop()
                    oldApps.remove(app)
                    returnApp.add(buildApp(file))
                }
            }
        }
        returnApp.addAll(oldApps)
        return returnApp
    }

    private fun buildApp(file: File) : App {
        var rowPolicy1 = ""
        var name1 = ""
        var splited1 = file.name.split("-")
        if (splited1.size == 1) {
            rowPolicy1 = ""
            name1 = splited1[0]
        } else if (splited1.size == 2) {
            rowPolicy1 = splited1[0]
            name1 = splited1[1]
        }
        return App(name1, file.absolutePath, appDataDir + file.name
                , file.lastModified(), getPolicy(rowPolicy1), file.name)
    }
}
