package ayds.songinfo.moredetails.fulllogic.injector
import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.data.OtherInfoRepositoryImpl
import ayds.artist.external.lastfm.inyector.LastFmInjector
import ayds.songinfo.moredetails.fulllogic.data.local.data.CardDatabase
import ayds.songinfo.moredetails.fulllogic.data.local.data.OtherInfoLocalStorageImpl
import ayds.songinfo.moredetails.fulllogic.presentation.CardDescriptionHelperImpl
import ayds.songinfo.moredetails.fulllogic.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.fulllogic.presentation.OtherInfoPresenterImpl


private const val ARTICLE_BD_NAME = "database-article"
object OtherInfoInjector {
    lateinit var presenter: OtherInfoPresenter
    fun initGraph(context: Context) {

        LastFmInjector.init()

        val cardDatabase =
            Room.databaseBuilder(context, CardDatabase::class.java, ARTICLE_BD_NAME).build()


        val articleLocalStorage = OtherInfoLocalStorageImpl(cardDatabase)

        val repository = OtherInfoRepositoryImpl(articleLocalStorage, LastFmInjector.lastFmService)

        val artistBiographyDescriptionHelper = CardDescriptionHelperImpl()

        presenter = OtherInfoPresenterImpl(repository, artistBiographyDescriptionHelper)
    }
}