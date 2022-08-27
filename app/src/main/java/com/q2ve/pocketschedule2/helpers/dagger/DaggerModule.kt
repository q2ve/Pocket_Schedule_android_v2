package com.q2ve.pocketschedule2.helpers.dagger

import com.q2ve.pocketschedule2.model.Model
import com.q2ve.pocketschedule2.model.ModelInterface
import dagger.Module
import dagger.Provides

@Module
class DaggerModule {
	@Provides
	fun provideModel(): ModelInterface {
		return Model{ }
	}
}