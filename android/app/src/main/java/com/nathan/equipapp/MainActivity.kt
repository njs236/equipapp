package com.nathan.equipapp

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DetailsFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener, QuestionFragment.OnFragmentInteractionListener, TalkFragment.OnFragmentInteractionListener{
    var fragmentManager = supportFragmentManager
    val TAG = this.javaClass.simpleName

//TODO: Load fragments.
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_map -> {
                val fragment = MapFragment()
                Log.d(TAG, "map selected")
                changeView(fragment)
                item.setChecked(true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_talks -> {
                val fragment = TalkFragment()
                Log.d(TAG, "talk selected")
                changeView(fragment)
                item.setChecked(true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_question -> {
                val fragment = QuestionFragment()
                Log.d(TAG, "question selected")
                changeView(fragment)
                item.setChecked(true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_details -> {
                //
                val fragment = DetailsFragment()
                Log.d(TAG, "details selected")
                changeView(fragment)
                item.setChecked(true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        setUpView()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun changeView(fragment: Fragment) {
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun setUpView() {
        var fragment = DetailsFragment()
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        navigation.selectedItemId = R.id.navigation_details
    }

    override fun onTalkInteraction(uri: Uri) {

    }

    override fun onDetailsInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMapInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onQuestionInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
