package ru.missclick.course_kmp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import ru.missclick.course_kmp.di.initKoin

class CourseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@CourseApplication)
            androidLogger()
        }
    }
}