package leagueofmonkeys.torqueburnou.data

interface Repo {
    suspend fun getUrl():String?

   suspend fun saveUrl(url: String)
}