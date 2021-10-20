package com.diegolima.rsscarsg1.UI

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.diegolima.rsscarsg1.Adapter.FeedAdapter
import com.diegolima.rsscarsg1.R
import com.diegolima.rsscarsg1.common.HTTPDataHandler
import com.diegolima.rsscarsg1.constants.RSS_link
import com.diegolima.rsscarsg1.constants.RSS_to_JSON_API
import com.diegolima.rsscarsg1.model.RSSObject
import com.google.gson.Gson
import java.lang.StringBuilder

import android.net.ConnectivityManager
import android.os.CountDownTimer
import android.view.View
import androidx.core.content.ContextCompat
import com.diegolima.rsscarsg1.common.NetworkChecker
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.util.*

class MainActivity : AppCompatActivity() {

    private val timer = Timer()

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

        loading()
    }

    private fun loading() {
        networkChecker.performActionIfConnected {
            rss()
            ivNotConnected.visibility = View.GONE
            tvMessageError.visibility = View.GONE
        }
        if (!networkChecker.hasInternet()) {
            tvMessageError.visibility = View.VISIBLE
            ivNotConnected.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            pbLoading.visibility = View.GONE
        }
    }

    private fun rss() {

        var exception: Exception? = null

        val loadRSSAsync = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<String, String, String>() {

            override fun onPreExecute() {
                pbLoading.visibility = View.VISIBLE
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: String?) {
                var rssObject: RSSObject
                try {
                    rssObject = Gson().fromJson<RSSObject>(result, RSSObject::class.java)
                    val adapter = FeedAdapter(rssObject, baseContext)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    messageNotification("Erro ao carregar. Tente novamente.")
                }
                pbLoading.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: String): String? {
                try {
                    var result: String
                    val http = HTTPDataHandler()
                    while (http.GetHTTPDataHandler(params[0]) != null) {
                        result = http.GetHTTPDataHandler(params[0])
                        if (result == null) {
                            exception = result
                        }
                        return result
                    }
                } catch (e: MalformedURLException) {
                    exception = e
                } catch (e: XmlPullParserException) {
                    exception = e
                } catch (e: IOException) {
                    exception = e
                }
                return "$exception"
            }
        }

        val url_get_data = StringBuilder(RSS_to_JSON_API)
        url_get_data.append(RSS_link)
        loadRSSAsync.execute(url_get_data.toString())
    }

    private fun messageNotification(s: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("ATENÇÃO")
        builder.setMessage(s)
        builder.setNeutralButton("OK") { _, _ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_refresh) {
            val timer = object : CountDownTimer(500, 500) {
                override fun onTick(millisUntilFinished: Long) {
                    jump()
                }

                override fun onFinish() {
                    loading()
                }
            }

            timer.start()
        }

        return true
    }

    private fun jump() {
        pbLoading.visibility = View.VISIBLE
        ivNotConnected.visibility = View.GONE
        tvMessageError.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

}