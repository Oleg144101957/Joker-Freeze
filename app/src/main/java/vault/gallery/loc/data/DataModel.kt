package vault.gallery.loc.data

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leagueofmonkeys.torqueburnou.data.Repo
import leagueofmonkeys.torqueburnou.data.RepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModel{

    @Provides
    @Singleton
    fun provideRepository(app: Application): Repo{
        return RepoImpl(app)
    }
}
