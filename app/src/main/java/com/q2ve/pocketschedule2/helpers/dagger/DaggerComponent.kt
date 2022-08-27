package com.q2ve.pocketschedule2.helpers.dagger

import com.q2ve.pocketschedule2.model.ModelInterface
import dagger.Component

@Component(modules = [DaggerModule::class])
interface DaggerComponent {
	fun getModel(): ModelInterface
}