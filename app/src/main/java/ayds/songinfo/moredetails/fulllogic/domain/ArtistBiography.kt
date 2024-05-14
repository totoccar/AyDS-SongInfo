package ayds.songinfo.moredetails.fulllogic.domain

data class ArtistBiography
    (val artistName: String,
     val biography: String,
     val articleUrl: String,
     var isLocallyStored: Boolean = false
            )
