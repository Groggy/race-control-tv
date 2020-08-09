package fr.groggy.racecontrol.tv.core.release

import java.lang.RuntimeException

class ApkDownloadFailedException(val apk: Apk) :
    RuntimeException("Fail to download ${apk.url}")