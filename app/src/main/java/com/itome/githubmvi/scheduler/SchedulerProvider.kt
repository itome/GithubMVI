package com.itome.githubmvi.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler

    fun ui(): Scheduler
}