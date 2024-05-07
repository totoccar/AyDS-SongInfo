package ayds.songinfo.moredetails.fulllogic.injector
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresenter
import ayds.songinfo.moredetails.fulllogic.presentation.MoreDetailsPresenterImpl

import android.content.Context

object MoreDetailsPresenterInjector {
    lateinit var presenter: MoreDetailsPresenter
    fun init(context: Context){
        ArticleRepositoryInjector.init(context)
        presenter = MoreDetailsPresenterImpl(ArticleRepositoryInjector.repository)
    }
}