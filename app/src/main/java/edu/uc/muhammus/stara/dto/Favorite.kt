package edu.uc.muhammus.stara.dto

data class Favorite(var id: String = "id not defined",
                    var name: String = "Name not defined",
                    var subtitle: String? = "Subtitle not defined",
                    var detail: String? = "Detail not defined",
                    var image: String? = null) {
}