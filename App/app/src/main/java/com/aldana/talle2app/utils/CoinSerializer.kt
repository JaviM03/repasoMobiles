package com.aldana.talle2app.utils

import com.aldana.talle2app.models.Coin
import org.json.JSONArray
import org.json.JSONObject

class CoinSerializer {

    companion object {

        fun parseCoins(coinsText: String): List<Coin> {
            val coinsJSON = JSONArray(coinsText)
            return MutableList(coinsJSON.length() - 1) {
                parseCoin(coinsJSON[it].toString())
            }
        }

        fun parseCoin(coinText: String): Coin {
            val coinJSON = JSONObject(coinText)

           return with(coinJSON) {
                Coin(getString("name"),getDouble("value"))
            }
        }

    }
}