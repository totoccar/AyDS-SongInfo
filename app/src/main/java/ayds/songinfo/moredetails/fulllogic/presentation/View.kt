package ayds.songinfo.moredetails.fulllogic.presentation

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.songinfo.R
import android.app.Activity


const val LASTFM_IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"


class View: Activity(){
    private lateinit var articleTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var lastFMImageView: ImageView
    private fun initViewProperties() {
        articleTextView = findViewById(R.id.textPane1)
        openUrlButton = findViewById(R.id.openUrlButton)
        lastFMImageView = findViewById(R.id.lastFMImageView)
    }

}