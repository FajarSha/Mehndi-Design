package com.hennamehndi.mehndipatterns.bridalmehndi.designs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        findViewById<ImageView>(R.id.iv_design).setOnClickListener {
            startActivity(Intent(this,
                MainActivity::class.java))


        }

        findViewById<ImageView>(R.id.iv_share).setOnClickListener {


            try {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=$packageName"
                )
                startActivity(Intent.createChooser(i, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }

        findViewById<ImageView>(R.id.iv_rate).setOnClickListener {

            try {
                val uri = Uri.parse("market://details?id=$packageName")
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }
//        App.showInterstitial(this@MenuActivity)

    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}