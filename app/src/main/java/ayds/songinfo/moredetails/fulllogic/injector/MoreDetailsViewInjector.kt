package ayds.songinfo.moredetails.fulllogic.injector
import ayds.songinfo.moredetails.fulllogic.presentation.View

object MoreDetailsViewInjector {
    fun init(moreDetailsView: View){
        MoreDetailsPresenterInjector.init(moreDetailsView)
    }
}