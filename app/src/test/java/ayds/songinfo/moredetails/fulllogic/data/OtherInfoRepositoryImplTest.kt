package ayds.songinfo.moredetails.fulllogic.data

import ayds.songinfo.moredetails.fulllogic.data.external.data.OtherInfoService
import ayds.songinfo.moredetails.fulllogic.data.local.data.OtherInfoLocalStorage
import io.mockk.mockk
import org.junit.Test

class OtherInfoRepositoryImplTest {
    private val otherInfoLocalStorage: OtherInfoLocalStorage= mockk(relaxUnitFun = true)
    private val otherInfoService: OtherInfoService= mockk(relaxUnitFun = true)
    @Test
    fun`if article is in the database should mark it as local`(){


    }
}