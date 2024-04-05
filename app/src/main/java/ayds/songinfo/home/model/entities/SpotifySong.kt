package ayds.songinfo.home.model.entities

import java.time.Month
import java.time.Year

sealed class Song {
    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val releaseDatePrecision: String,
        val spotifyUrl: String,
        val imageUrl: String,
        var isLocallyStored: Boolean = false
    ) : Song() {
        fun showReleaseDate():String{
            val listDate = releaseDate.split("-").map{it.toInt() }
            return when (releaseDatePrecision){
                "day"-> return dayReleaseDate(listDate)
                "month"-> return monthReleaseDate(listDate)
                "year"-> return yearReleaseDate(listDate)
                else -> "invalidReleaseDateException"
            }
        }
        private fun dayReleaseDate(listDate: List<Int>): String{
            return  "${listDate[2]}/${listDate[1]}/${listDate[0]}"
        }

        private fun monthReleaseDate(listDate: List<Int>): String{
            val month= Month.of(listDate[1]).toString()
            return "${month},${listDate[0]}"
        }

        private fun yearReleaseDate(listDate: List<Int>): String{
            return "${listDate[0]} ${leapYear(listDate[0])}"
        }

        private fun leapYear(year: Int): String{
            return if (Year.of(year).isLeap)
                "(leap year)"
            else
                "(not a leap year)"
        }

    }


    object EmptySong : Song()
}

