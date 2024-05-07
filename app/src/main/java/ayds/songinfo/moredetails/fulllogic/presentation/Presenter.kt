package ayds.songinfo.moredetails.fulllogic.presentation
import ayds.songinfo.moredetails.fulllogic.domain.ArticleRepository


interface MoreDetailsPresenter{
    fun getArtistInfoAsync(artistName:String)
}

class MoreDetailsPresenterImpl( private val articleRepository: ArticleRepository):MoreDetailsPresenter {
     override fun getArtistInfoAsync(artistName:String) {
        Thread {
            getArtistInfo(artistName)
        }.start()
    }

    private fun getArtistInfo(artistName:String) {

        var article = articleRepository.getArticle(artistName)

        //TODO: updateUi(artistBiography)
    }

}