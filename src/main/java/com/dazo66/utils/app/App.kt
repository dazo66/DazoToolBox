package com.dazo66.utils.app

class App(val name: String,
          val path: String,
          val dataPath: String,
          val policy: LaunchPolicy) {

    val thread: AppThread = AppThread(this)

}
