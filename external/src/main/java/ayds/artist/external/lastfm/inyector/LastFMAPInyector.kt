package ayds.artist.external.lastfm.inyector

import ayds.artist.external.lastfm.data.LastFMAPI
import ayds.artist.external.lastfm.data.LastFMToArtistBiographyResolver
import ayds.artist.external.lastfm.data.LastFMToArtistBiographyResolverImpl
import ayds.artist.external.lastfm.data.OtherInfoServiceImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LASTFM_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

object LastFMAPInyector {
    private val LastFMAPI = initFMAPI()
    private val LastFMAPIToArtistResolver: LastFMToArtistBiographyResolver = LastFMToArtistBiographyResolverImpl()
    private val lastFMArticleService= OtherInfoServiceImpl(LastFMAPI, LastFMAPIToArtistResolver)




    private fun initFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(LastFMAPI::class.java)
    }

}