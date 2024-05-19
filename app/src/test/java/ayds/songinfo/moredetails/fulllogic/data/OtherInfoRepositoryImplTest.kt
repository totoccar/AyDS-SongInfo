package ayds.songinfo.moredetails.fulllogic.data

import ayds.songinfo.moredetails.fulllogic.data.external.data.OtherInfoService
import ayds.songinfo.moredetails.fulllogic.data.local.data.OtherInfoLocalStorage
import ayds.songinfo.moredetails.fulllogic.domain.ArtistBiography
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OtherInfoRepositoryImplTest {

    @Test
    fun `getArtistInfo with existing local article`() {
        val expectedResult= ArtistBiography("Artist", "Local Bio", "local-url", false)
        val otherInfoLocalStorage = mockk<OtherInfoLocalStorage> {
            every { getArticle(any()) } returns expectedResult
        }
        val otherInfoService = mockk<OtherInfoService>()

        val repository = OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)

        val artistInfo = repository.getArtistInfo("Artist")

        expectedResult.isLocallyStored= true
        assertEquals(expectedResult, artistInfo)
        assertEquals(true, artistInfo.isLocallyStored)
    }

    @Test
    fun `getArtistInfo with non-existing local article`() {
        val otherInfoLocalStorage = mockk<OtherInfoLocalStorage> {
            every { getArticle(any()) } returns null
        }
        val otherInfoService = mockk<OtherInfoService> {
            every { getArticle(any()) } answers {
                val artistName = firstArg<String>()
                ArtistBiography(artistName, "Remote Bio", "remote-url", true)
            }
        }

        val repository = OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)

        val artistInfo = repository.getArtistInfo("Artist")

        assertEquals("Remote Bio", artistInfo.biography)
        assertEquals(false, artistInfo.isLocallyStored)
    }

    @Test
    fun `getArtistInfo with empty biography`() {
        val otherInfoLocalStorage = mockk<OtherInfoLocalStorage> {
            every { getArticle(any()) } returns null
        }
        val otherInfoService = mockk<OtherInfoService> {
            every { getArticle(any()) } returns ArtistBiography("Artist", "", "empty-url", false)
        }

        val repository = OtherInfoRepositoryImpl(otherInfoLocalStorage, otherInfoService)

        val artistInfo = repository.getArtistInfo("Artist")

        assertEquals("", artistInfo.biography)
        assertEquals(false, artistInfo.isLocallyStored)
    }
}
