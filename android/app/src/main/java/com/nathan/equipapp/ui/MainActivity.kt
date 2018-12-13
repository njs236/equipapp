package com.nathan.equipapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.telecom.Call
import android.util.Log
import android.view.MenuItem
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
//import jdk.nashorn.internal.runtime.ECMAException.getException
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.nathan.equipapp.R
import com.joanzapata.iconify.fonts.FontAwesomeIcons
import com.joanzapata.iconify.IconDrawable
import com.nathan.equipapp.MyApplication
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.FirebaseFirestore
import com.nathan.equipapp.services.NotificationService


class MainActivity : AppCompatActivity(), DetailsFragment.OnFragmentInteractionListener,
    MapFragment.OnFragmentInteractionListener,
    QuestionFragment.OnFragmentInteractionListener,
    TalkFragment.OnFragmentInteractionListener,
NotificationFragment.OnFragmentInteractionListener,
QuestionReceiverFragment.OnFragmentInteractionListener,
MoreFragment.OnFragmentInteractionListener{
    val TAG = this.javaClass.simpleName
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mSelectedItem: Int = -1
    private val SELECTED_ITEM = "arg_selected_item"

//TODO: Load fragments.
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
       changeView(item)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.currentActivity = this
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)


        val selectedItem: MenuItem
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0)
            selectedItem = navigation.menu.findItem(mSelectedItem)
        } else {
            selectedItem = navigation.menu.getItem(3)
        }

        changeView(selectedItem)


        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        firestore.firestoreSettings = settings

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val currentUser = mAuth.currentUser
        if(currentUser != null) {
            updateUI(currentUser)

        } else {
            mAuth.signInAnonymously().addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success")
                        val user = mAuth.getCurrentUser()
                        updateUI(user)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.exception)
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }

                }
        }

    }

    override fun onBackPressed() {
        val homeItem = navigation.menu.getItem(0)
        if (mSelectedItem != homeItem.itemId) {
            // select home item
            changeView(homeItem)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM, mSelectedItem)
        super.onSaveInstanceState(outState)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        //show user that they have access to the database.
        if (currentUser != null) {
            startService(
                Intent(
                    MyApplication.currentActivity,
                    NotificationService::class.java
                )
            )
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(MyApplication.currentActivity,
            NotificationService::class.java))
    }

    private fun getCurrentUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = user.uid
        }
        return
    }

    //convenience functions for getting notis and questions.

    fun changeView(item: MenuItem) {
        var frag: Fragment? = null
        when (item.itemId) {
            R.id.navigation_map -> frag = MapFragment()
            R.id.navigation_talks -> frag = TalkFragment()
            R.id.navigation_question -> frag = QuestionFragment()
            R.id.navigation_details-> frag = DetailsFragment()
            R.id.navigation_question_receiver -> frag = QuestionReceiverFragment()
        }

        mSelectedItem = item.itemId

        for (i in 0 until navigation.menu.size() - 1) {
            val menuItem = navigation.menu.getItem(i)
            menuItem.isChecked = menuItem.itemId == item.itemId
        }
        if (frag!= null) {
            var fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_layout, frag)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    override fun onTalkInteraction(uri: Uri) {

    }

    override fun onDetailsInteraction(uri: Uri) {
        //TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }

    override fun onMapInteraction(uri: Uri) {
        //TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }

    override fun onQuestionInteraction(uri: Uri) {
        //TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }

    override fun onQuestionsInteraction(uri: Uri) {
        //TODO("not implemented") To change body of created functions use File | Settings | File Templates.
    }

    override fun onNotificationInteraction(uri: Uri) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMoreInteraction(uri: Uri) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
