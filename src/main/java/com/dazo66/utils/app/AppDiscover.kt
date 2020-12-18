package com.dazo66.utils.app

/**
 * @author Dazo66
 */
class AppDiscover {

    companion object {
        var appDir: String = "app/"
        var appDataDir: String = "data/"
    }

    /**
     * 根据 [policy(policyArgs)] name这样的格式进行引用搜索
     * 扫描文件目录下的所有的可执行文件
     * 如果需要运行参数请用bat文件引用执行
     * 可执行文件的数据目录会放在data下的各个app的名字下面
     */
    fun scanApp(oldApps: List<App>) : List<App> {

    }


}
