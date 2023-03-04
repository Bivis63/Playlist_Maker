package com.example.playlistmaker

class TrackList {
    var trackList = mutableListOf<Track>()
        init {

            trackList=(1..5).map {
                Track(
                    trackName[it],
                    artistName[it],
                    trackTime[it],
                    artworkUrl[it]
                    )
            }.toMutableList()

        }
    companion object{
        private val trackName= mutableListOf(
            "Smells Like Teen Spirit",
            "Billie Jean",
            "Stayin' Alive",
            "Whole Lotta Love",
            "Sweet Child O'Mine"
        )
        private val artistName = mutableListOf(
            "Nirvana",
            "Michael Jackson",
            "Bee Gees",
            "Led Zeppelin",
            "Guns N' Roses"
        )
        private val trackTime = mutableListOf(
            "5:01",
            "4:35",
            "4:10",
            "5:33",
            "5:03"
        )
        private val artworkUrl = mutableListOf(
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )
    }

}