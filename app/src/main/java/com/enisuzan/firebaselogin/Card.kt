package com.enisuzan.firebaselogin

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Card(var name: String? = null, var house: String? = null, var score: String? = null, var image: String? = null, var isFaceUp: Boolean? = false, var isMatched: Boolean? = false) {
}