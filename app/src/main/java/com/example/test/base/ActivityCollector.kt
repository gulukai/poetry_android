package com.example.test.base

import android.app.Activity

object ActivityCollector {
    private val activityList = ArrayList<Activity>()
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityList.remove(activity)
    }

    fun finishAllActivity() {
        for (activity in activityList) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activityList.clear()
    }
}