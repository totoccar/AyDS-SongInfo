package ayds.songinfo.moredetails.fulllogic.data.local.data
import ayds.artist.external.lastfm.data.ArtistBiography

interface OtherInfoLocalStorage {
    fun getArticle(artistName: String): ayds.artist.external.lastfm.data.ArtistBiography?
    fun insertArtist(artistBiography: ayds.artist.external.lastfm.data.ArtistBiography)
}

internal class OtherInfoLocalStorageImpl(
    private val articleDatabase: ArticleDatabase,
) : OtherInfoLocalStorage {

    override fun getArticle(artistName: String): ayds.artist.external.lastfm.data.ArtistBiography? {
        val artistEntity = articleDatabase.ArticleDao().getArticleByArtistName(artistName)
        return artistEntity?.let {
            ayds.artist.external.lastfm.data.ArtistBiography(
                artistName,
                artistEntity.biography,
                artistEntity.articleUrl
            )
        }
    }

    override fun insertArtist(artistBiography: ayds.artist.external.lastfm.data.ArtistBiography) {
        articleDatabase.ArticleDao().insertArticle(
            ArticleEntity(
                artistBiography.artistName, artistBiography.biography, artistBiography.articleUrl
            )
        )
    }
}