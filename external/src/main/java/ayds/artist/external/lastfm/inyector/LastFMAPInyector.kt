package ayds.artist.external.lastfm.inyector

import ayds.artist.external.lastfm.data.LastFMAPI
import ayds.artist.external.lastfm.data.LastFMToBiographyResolver
import ayds.artist.external.lastfm.data.LastFMToBiographyResolverImpl
import ayds.artist.external.lastfm.data.LastFmService
import ayds.artist.external.lastfm.data.LastFmServiceImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object LastFmInjector {

    lateinit var lastFmService: LastFmService

    fun init() {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)

        val lastFMToArtistBiographyResolver = LastFMToBiographyResolverImpl()
        lastFmService = LastFmServiceImpl(
            lastFMAPI,
            lastFMToArtistBiographyResolver
        )
    }
}