package ayds.songinfo.moredetails.fulllogic.data
import ayds.artist.external.lastfm.data.OtherInfoService
import ayds.songinfo.moredetails.fulllogic.data.local.data.OtherInfoLocalStorage
import ayds.artist.external.lastfm.data.ArtistBiography
import ayds.songinfo.moredetails.fulllogic.domain.OtherInfoRepository

internal class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val otherInfoService: ayds.artist.external.lastfm.data.OtherInfoService,
) : OtherInfoRepository {

    override fun getArtistInfo(artistName: String): ArtistBiography {
        val dbArticle = otherInfoLocalStorage.getArticle(artistName)

        val artistBiography: ayds.artist.external.lastfm.data.ArtistBiography

        if (dbArticle != null) {
            artistBiography = dbArticle.apply { markItAsLocal() }
        } else {
            artistBiography = otherInfoService.getArticle(artistName)
            if (artistBiography.biography.isNotEmpty()) {
                otherInfoLocalStorage.insertArtist(artistBiography)
            }
        }
        return artistBiography
    }

    private fun ayds.artist.external.lastfm.data.ArtistBiography.markItAsLocal() {
        isLocallyStored = true
    }
}