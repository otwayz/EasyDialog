package com.otway.easydialog

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.otway.easydialog.EasyDialog.Builder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var easyDialog: EasyDialog

    override fun onClick(v: View?) {
        val id = v!!.id
        when (id) {
            R.id.id_iv_close -> {
                Toast.makeText(this, "点了关闭", Toast.LENGTH_SHORT).show()
                easyDialog.dismiss()
            }
            R.id.id_btn_checkin -> {
                Toast.makeText(this, "点了打卡", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            easyDialog = Builder(this).setContentViewId(R.layout.dialog_ugc_finish_read)
                    .setWidth(-2)
                    .setHeight(EasyDialog.WRAP_CONTENT)
                    .setDimAmount(0.8f)
                    .setBackgroundColor(Color.CYAN)
                    .setOnClickListener(R.id.id_iv_close, this)
                    .setOnClickListener(R.id.id_btn_checkin, this)
                    .show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
