package safira.meetme.view

import android.os.Bundle
import android.support.v4.app.FragmentTabHost
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.TabHost.*

import safira.meetme.R

class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.accent))
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        val tabHost = findViewById<View>(android.R.id.tabhost) as FragmentTabHost
        tabHost.setup(this, supportFragmentManager, android.R.id.tabcontent)

        val spec: TabSpec = tabHost.newTabSpec("Friends")
        spec.setIndicator("Friends")
        tabHost.addTab(spec, FriendListFragment::class.java, null)


        tabHost.currentTab = 0
    }
}
