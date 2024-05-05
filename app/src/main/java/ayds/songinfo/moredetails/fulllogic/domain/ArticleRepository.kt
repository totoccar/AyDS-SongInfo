package ayds.songinfo.moredetails.fulllogic.domain

interface ArticleRepository {
    fun getARticle(artistName: String): Article?
}