package com.aldana.talle2app

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldana.talle2app.adapter.CoinAdapter
import com.aldana.talle2app.models.Coin
import com.aldana.talle2app.utils.CoinSerializer
import com.aldana.talle2app.utils.NetworkUtilities
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var viewAdapter: CoinAdapter
    lateinit var viewManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = CoinAdapter(listOf<Coin>()) {
            Snackbar.make(rv_moneda,
                    "CLick en " + it.name,
                    Snackbar.LENGTH_SHORT)
                    .show()
        }

        rv_moneda.apply {
            adapter = viewAdapter
            layoutManager = viewManager
        }

        CoinsFetch().execute()
    }


    inner class CoinsFetch : AsyncTask<Unit, Unit, List<Coin>>() {

        override fun doInBackground(vararg params: Unit?): List<Coin> {
            val url = NetworkUtilities.buildURL()
            val resultString = NetworkUtilities.getHTTPResult(url)

            val resultJSON = JSONObject(resultString)

            return if (resultJSON.getBoolean("success")) {
                CoinSerializer.parseCoins(
                        resultJSON.getJSONArray("docs").toString()
                )
            } else {
                listOf<Coin>()
            }
        }

        override fun onPostExecute(result: List<Coin>) {
            if (result.isNotEmpty()) {
                viewAdapter.setData(result)
            } else {
                Snackbar.make(rv_moneda,
                        "No se pudo obtener datos",
                        Snackbar.LENGTH_SHORT).show()
            }
        }

    }
}
