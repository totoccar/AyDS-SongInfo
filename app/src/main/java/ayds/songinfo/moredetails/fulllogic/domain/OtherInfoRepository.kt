package ayds.songinfo.moredetails.fulllogic.domain

interface OtherInfoRepository {
    fun getArtistInfo(artistName: String): ArtistBiography


}