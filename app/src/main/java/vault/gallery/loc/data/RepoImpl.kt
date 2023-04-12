package leagueofmonkeys.torqueburnou.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoImpl @Inject constructor(private val app: Application) : Repo {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

    companion object {
        private const val DATA_KEY = "data_key"
    }

    override suspend fun getUrl(): String? {
        val dataStoreKey = stringPreferencesKey(DATA_KEY)
        val preferences = app.dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override suspend fun saveUrl(url: String) {
        val dataStoreKey = stringPreferencesKey(DATA_KEY)
        app.dataStore.edit { data ->
            data[dataStoreKey] = url
        }
    }
}