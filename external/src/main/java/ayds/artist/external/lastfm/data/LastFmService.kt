package ayds.artist.external.lastfm.data

import java.io.IOException

interface LastFmService {
    fun getArticle(artistName: String): LastFmBiography
}
internal class LastFmServiceImpl(
    private val lastFMAPI: LastFMAPI,
    private val lastFMToBiographyResolver: LastFMToBiographyResolver
) : LastFmService {

    override fun getArticle(artistName: String): LastFmBiography {

        var lastFmBiography = LastFmBiography(artistName, "", "")
        try {
            val callResponse = getSongFromService(artistName)
            lastFmBiography = lastFMToBiographyResolver.map(callResponse.body(), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return lastFmBiography
    }

    private fun getSongFromService(artistName: String) =
        lastFMAPI.getArtistInfo(artistName).execute()

}