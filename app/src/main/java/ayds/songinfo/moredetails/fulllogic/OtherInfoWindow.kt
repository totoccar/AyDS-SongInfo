package ayds.songinfo.moredetails.fulllogic

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.room.Room.databaseBuilder
import ayds.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

private const val IMAGE_URL = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"
class OtherInfoWindow : Activity() {
    private lateinit var lastFMAPI: LastFMAPI
    private var artistNameTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGUI()
        artistNameTextView = findViewById(R.id.textPane1)
        open(getArtistName())
    }

    private fun getArtistName() = intent.getStringExtra("artistName")

    private fun initGUI() {
        setContentView(R.layout.activity_other_info)
    }

    private fun getArtistInfo(artistName: String) {

        Log.e("TAG", "artistName $artistName")
        Thread {
            val article = getArticle(artistName)
            Log.e("TAG", "Get Image from $IMAGE_URL")
            updateTextView(article)
        }.start()
    }

    private fun updateTextView(article: Article?) {
        runOnUiThread {
            if (article != null) {
                setUrlLink(article.articleUrl)
            } else
            showTextView(getText(article))
        }
    }

    private fun showTextView(text: String) {
        Picasso.get().load(IMAGE_URL).into(findViewById<View>(R.id.imageView1) as ImageView)
        artistNameTextView!!.text = Html.fromHtml(text)
    }

    private fun getText(article: Article?): String{
        return if(article != null)
            (if (article.isLocallyStored) "[*]" else "") + (article.biography ?: "No Results")
        else
            ""
    }

    private fun getArticle(artistName: String): Article? {
        var article = getArticleFromDB(artistName)
        if (article == null) {
            article = getArticleFromService(artistName)
            if (article?.biography != null){
                insertArticleInDB(article)
            }
        }
        return article
    }

    private fun initFMAPI(): LastFMAPI {
        val retrofit = createRetrofit()
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun createRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun getArticleFromService(artistName: String): Article? {
        val callResponse: Response<String>
        return try {
            callResponse = lastFMAPI.getArtistInfo(artistName).execute()
            Log.e("TAG", "JSON " + callResponse.body())
            getArticleFromResponse(callResponse,artistName)
        } catch (e1: IOException) {
            Log.e("TAG", "Error $e1")
            e1.printStackTrace()
            null
        }
    }

    private fun getArticleFromResponse(
        callResponse: Response<String>,
        artistName: String
    ): Article {
        val jobj = getJson(callResponse)
        val artist = getArtist(jobj)
        val text: String? = getBioText(artist, artistName)
        val url = getUrl(artist)
        return Article(artistName, text, url, false)
    }

    private fun getUrl(artist: JsonObject): String {
        TODO("Not yet implemented")
    }

    private fun getBioText(artist: JsonObject, artistName: String): String? {
        TODO("Not yet implemented")
    }

    private fun getJson(callResponse: Response<String>): JsonObject {
        TODO("Not yet implemented")
    }

    private fun getArtist(jobj: JsonObject): JsonObject {
        val artist = jobj["artist"].getAsJsonObject()
        return artist
    }

    private fun setUrlLink(url: String) {
        findViewById<View>(R.id.openUrlButton1).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun GetTextFromBioContent(
        text: String,
        extract: JsonElement,
        artistName: String
    ): String {
        var text1 = text
        text1 = extract.asString.replace("\\n", "\n")
        text1 = textToHtml(text1, artistName)
        return text1
    }

    private fun insertArticleInDB(article: Article) {
        if (article.biography != null) {
            Thread {
                dataBase?.ArticleDao()?.insertArticle(
                    ArticleEntity(
                        article.artistName,
                        article.biography,
                        article.articleUrl
                    )
                )
            }.start()
        }
    }

    private fun getArticleFromDB(
        artistName: String
    ): Article ? {
        val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)
        return if(article != null){
            Article(article.artistName, article.biography, article.articleUrl, true)
        }  else {
            return null
        }
    }

    private var dataBase: ArticleDatabase? = null
    private fun open(artist: String?) {
        dataBase =
            databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        initFMAPI()
        if (artist != null) {
            getArtistInfo(artist)
        }
    }

    private fun articleEntity(artistName: String) = dataBase!!.ArticleDao().getArticleByArtistName(artistName)

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
    internal data class Article(
        val artistName: String,
        val biography: String?,
        val articleUrl: String,
        val isLocallyStored: Boolean
    )

}
