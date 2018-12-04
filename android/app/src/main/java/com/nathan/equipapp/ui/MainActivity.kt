package com.nathan.equipapp.ui

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
//import jdk.nashorn.internal.runtime.ECMAException.getException
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.nathan.equipapp.R


class MainActivity : AppCompatActivity(), DetailsFragment.OnFragmentInteractionListener,
    MapFragment.OnFragmentInteractionListener,
    QuestionFragment.OnFragmentInteractionListener,
    TalkFragment.OnFragmentInteractionListener,
NotificationFragment.OnFragmentInteractionListener,
QuestionReceiverFragment.OnFragmentInteractionListener{
    var fragmentManager = supportFragmentManager
    val TAG = this.javaClass.simpleName
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

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
            R.id.navigation_question_receiver ->{
                val fragment = QuestionReceiverFragment()
                Log.d(TAG, "question displayer selected")
                changeView(fragment)
                item.setChecked(true)
                return@OnNavigationItemSelectedListener true


            }
            /*
            R.id.noti_sender ->{
                val fragment = NotificationFragment()
                Log.d(TAG, "Notification selected")
                changeView(fragment)
                item.setChecked(true)
                return@OnNavigationItemSelectedListener true


            }
*/

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        setUpView()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val currentUser = mAuth.currentUser
        if(currentUser != null) {
            updateUI(currentUser)
        } else {
            mAuth.signInAnonymously().addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success")
                        val user = mAuth?.getCurrentUser()
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

    private fun updateUI(currentUser: FirebaseUser?) {
        //show user that they have access to the database.

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


}
