import ayds.songinfo.moredetails.fulllogic.domain.OtherInfoRepository
import ayds.songinfo.moredetails.fulllogic.presentation.ArtistBiographyDescriptionHelper
import ayds.songinfo.moredetails.fulllogic.presentation.ArtistBiographyUiState
import ayds.songinfo.moredetails.fulllogic.presentation.OtherInfoPresenter
import ayds.songinfo.moredetails.fulllogic.presentation.OtherInfoPresenterImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {

    private val otherInfoRepository: OtherInfoRepository = mockk()
    private val artistBiographyDescriptionHelper: ArtistBiographyDescriptionHelper = mockk()
    private val otherInfoPresenter: OtherInfoPresenter =
        OtherInfoPresenterImpl(otherInfoRepository, artistBiographyDescriptionHelper)

    @Test
    fun `getArtistInfo should return artist biography ui state`() {
        val lastFmBiography = ayds.artist.external.lastfm.data.LastFmBiography(
            "artistName",
            "biography",
            "articleUrl"
        )
        every { otherInfoRepository.getArtistInfo("artistName") } returns lastFmBiography
        every { artistBiographyDescriptionHelper.getDescription(lastFmBiography) } returns "description"
        val artistBiographyTester: (ArtistBiographyUiState) -> Unit = mockk(relaxed = true)

        otherInfoPresenter.artistBiographyObservable.subscribe(artistBiographyTester)
        otherInfoPresenter.getArtistInfo("artistName")

        val result = ArtistBiographyUiState("artistName", "description", "articleUrl")
        verify { artistBiographyTester(result) }
    }
}