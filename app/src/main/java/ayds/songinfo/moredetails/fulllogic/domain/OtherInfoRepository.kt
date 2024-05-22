package ayds.songinfo.moredetails.fulllogic.domain

interface OtherInfoRepository {
    fun getArtistInfo(artistName: String): ayds.artist.external.lastfm.data.ArtistBiography


}