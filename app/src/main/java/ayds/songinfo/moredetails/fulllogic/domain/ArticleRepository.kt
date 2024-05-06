package ayds.songinfo.moredetails.fulllogic.domain

interface ArticleRepository {
    fun getArticle(artistName: String): Article?
}