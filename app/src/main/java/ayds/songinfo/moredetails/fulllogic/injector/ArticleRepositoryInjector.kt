package ayds.songinfo.moredetails.fulllogic.injector

import android.content.Context
import androidx.room.Room
import ayds.songinfo.moredetails.fulllogic.ArticleDatabase
import ayds.songinfo.moredetails.fulllogic.LastFMAPI
import ayds.songinfo.moredetails.fulllogic.OtherInfoWindow
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private  const val DB_NAME = "db-name"

object ArticleRepositoryInjector {

    fun init(context: Context){
        val lastFMAPI = initFMAPI()
        val dataBase = initDataBase(context)
    }


    private fun initFMAPI(): LastFMAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(OtherInfoWindow.LASTFM_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        return retrofit.create(LastFMAPI::class.java)
    }

    private fun initDataBase(context: Context) =
            Room.databaseBuilder(context, ArticleDatabase::class.java, DB_NAME)
                .build()
}