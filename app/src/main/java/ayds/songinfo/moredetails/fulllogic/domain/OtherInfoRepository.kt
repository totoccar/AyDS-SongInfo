package ayds.songinfo.moredetails.fulllogic.domain

interface OtherInfoRepository {
    fun getCard(artistName: String): Card

}