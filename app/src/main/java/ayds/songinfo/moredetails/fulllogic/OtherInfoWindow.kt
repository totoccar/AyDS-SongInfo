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
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.Locale

const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"
class OtherInfoWindow : Activity() {
    private lateinit var textPane1: TextView
    private lateinit var lastFMAPI : LastFMAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeGUI()
        open(getArtistName())
    }

    private fun getArtistName() = intent.getStringExtra("artistName")

    private fun initializeGUI() {
        setContentView(R.layout.activity_other_info)
        textPane1 = findViewById(R.id.textPane1)
    }

    private fun getArticle(artistName: String): Article? {
        var article = getArticleFromDB(artistName)
        if( article == null ){
            article = getArticleFromService(artistName)
            if (article?.biography != null){
                setArticle(article)
            }
        }
        return article
    }

    private fun setArticle(article: Article) {

    }

    private fun getArticleFromService(artistName: String): Article? {
        val callResponse: Response<String> = lastFMAPI.getArtistInfo(artistName).execute()
        Log.e("TAG", "JSON " + callResponse.body())


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

    private fun getUrl(artist: Any): String {

    }

    private fun getBioText(artist: Any, artistName: String): String? {
        TODO("Not yet implemented")
    }

    private fun getArtist(jobj: Any): Any {
        TODO("Not yet implemented")
    }

    private fun getJson(callResponse: Response<String>): Any {
        TODO("Not yet implemented")
    }

    private fun getArticleFromDB(artistName: String): Article? {
        val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName)
        return if (article != null){
            Article(article.artistName, article.biography, article.articleUrl, true)
    }
        else null
    }

    private fun getArtistInfo(artistName: String?) {

        val retrofit = getRetrofit()

        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        Log.e("TAG", "artistName $artistName")
        Thread {
            val article = dataBase!!.ArticleDao().getArticleByArtistName(artistName!!)
            var textBiography = ""
            if (article != null) { // exists in db
                textBiography = "[*]" + article.biography
                val urlString = article.articleUrl
                findViewById<View>(R.id.openUrlButton1).setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(urlString))
                    startActivity(intent)
                }
            } else { // get from service
                val callResponse: Response<String>
                try {
                    callResponse = lastFMAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val artist = jobj["artist"].getAsJsonObject()
                    val bio = artist["bio"].getAsJsonObject()
                    val extract = bio["content"]
                    val url = artist["url"]
                    if (extract == null) {
                        textBiography = "No Results"
                    } else {
                        textBiography = extract.asString.replace("\\n", "\n")
                        textBiography = textToHtml(textBiography, artistName)


                        // save to DB  <o/
                        val biography = textBiography
                        Thread {
                            dataBase!!.ArticleDao().insertArticle(ArticleEntity(artistName, biography, url.asString))
                        }
                            .start()
                    }
                    val urlString = url.asString
                    findViewById<View>(R.id.openUrlButton1).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(urlString))
                        startActivity(intent)
                    }
                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Log.e("TAG", "Get Image from $imageUrl")
            val finalText = textBiography
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView1) as ImageView)
                textPane1!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()).build()
    }

    private var dataBase: ArticleDatabase? = null
    private fun open(artist: String?) {
        dataBase = databaseBuilder(this, ArticleDatabase::class.java, "database-name-thename").build()
        Thread {
            dataBase!!.ArticleDao().insertArticle(ArticleEntity("test", "sarasa", ""))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("test"))
            Log.e("TAG", "" + dataBase!!.ArticleDao().getArticleByArtistName("nada"))
        }.start()
        getArtistInfo(artist)
    }

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
}

internal data class Article(val artistName: String,
                            val biography: String,
                            val articleUrl: String,
                            val isLocallyStored: Boolean)
