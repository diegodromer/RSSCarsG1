package com.diegolima.rsscarsg1.UI

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.diegolima.rsscarsg1.Adapter.FeedAdapter
import com.diegolima.rsscarsg1.R
import com.diegolima.rsscarsg1.common.HTTPDataHandler
import com.diegolima.rsscarsg1.constants.RSS_link
import com.diegolima.rsscarsg1.constants.RSS_to_JSON_API
import com.diegolima.rsscarsg1.model.RSSObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.recyclerView
import java.lang.StringBuilder
import android.net.NetworkInfo

import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import com.diegolima.rsscarsg1.common.NetworkChecker
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private val networkChecker by lazy {
        NetworkChecker(ContextCompat.getSystemService(this, ConnectivityManager::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = "News Cars G1"
        setSupportActionBar(toolbar)

        val linearLayoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        networkChecker.performActionIfConnected {
            loadRSS()
        }
        if(!networkChecker.hasInternet()) {
            //Toast.makeText(this, "Sem conexão com a Internet.", Toast.LENGTH_LONG).show()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("ATENÇÃO")
            builder.setMessage("Sem acesso a Internet.")
            builder.setNeutralButton("OK"){_,_ ->

            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun loadRSS() {
        val loadRSSAsync = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<String, String, String>() {

            var mDialog = ProgressDialog(this@MainActivity)

            override fun onPreExecute() {
                mDialog.setMessage("Por favor, aguarde...")
                mDialog.show()
            }

            override fun onPostExecute(result: String?) {
                mDialog.dismiss()
                var rssObject: RSSObject
                rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java)
                val adapter = FeedAdapter(rssObject, baseContext)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun doInBackground(vararg params: String): String {
                var result: String
                val http = HTTPDataHandler()
                result = http.GetHTTPDataHandler(params[0])
                return result
            }
        }
        val url_get_data = StringBuilder(RSS_to_JSON_API)
        url_get_data.append(RSS_link)
        loadRSSAsync.execute(url_get_data.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh) {
            loadRSS()
        }
        return true
    }
}