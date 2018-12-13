package com.nathan.equipapp.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.FontAwesomeIcons
import com.joanzapata.iconify.widget.IconButton
import com.joanzapata.iconify.widget.IconTextView
import com.joanzapata.iconify.widget.IconToggleButton
import com.nathan.equipapp.MyApplication
import com.nathan.equipapp.R
import com.nathan.equipapp.assets.Question
import com.nathan.equipapp.assets.Recording
import com.nathan.equipapp.services.MediaPlayerService
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TalkFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TalkFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

private const val ACTION_PLAY : String = "com.nathan.equipapp.action.PLAY"
private const val ACTION_STOP : String = "com.nathan.equipapp.action.STOP"
private const val URL_EXTRA = "com.nathan.equipapp.extras.URL"
class TalkFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var TAG = this.javaClass.simpleName
    private var recordings = ArrayList<Recording>()
    var tbl_talk_layout: TableLayout? = null
    private var selectedIndex: Int = -1
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var mMediaBrowser: MediaBrowserCompat
    private var SELECTED_INDEX = "talk_selected_index"


    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // get current state with index of played music if it exists.
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, -1)

            if (selectedIndex != -1) {
                var playButton = tbl_talk_layout?.getChildAt(selectedIndex)!!.findViewById<ToggleButton>(R.id.row_talk_play)
                playButton.setButtonDrawable(R.drawable.ic_stop_circle_solid)
                playButton.isChecked = true
            }
        }


        mMediaBrowser = MediaBrowserCompat(context, ComponentName(context, MediaPlayerService::class.java), mConnectionCallbacks, null)
        mMediaBrowser.connect()

        context?.startService(Intent(context!!,
            MediaPlayerService::class.java))




    }

    val mConnectionCallbacks = object: MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mMediaBrowser.sessionToken.also {token->
                val mediaController = MediaControllerCompat (MyApplication.currentActivity, token)
                mediaController.registerCallback(controllerCallback)

                // Save the controller
                MediaControllerCompat.setMediaController(MyApplication.currentActivity, mediaController)


            }

            super.onConnected()
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (selectedIndex != -1) {
            outState.putInt(SELECTED_INDEX, selectedIndex)
        }
        super.onSaveInstanceState(outState)
    }

    private var controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_talk, container, false)
        tbl_talk_layout = view.findViewById(R.id.tbl_talk_layout)
        inflateTableLayout()

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onTalkInteraction(uri)
    }

     fun playSound(row: TableRow, index: Int) {
         Intent().also { intent->
             intent.action = "com.nathan.equipapp.action.STOP"
             LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
         }

         Log.d(TAG, "index: $index")
         val recording = recordings[index]
         //has another button been pressed.
         Log.d(TAG, "selectedIndex: isChanged: ${this.selectedIndex != index}")
         if (this.selectedIndex != -1  && this.selectedIndex != index) {
             Log.d(TAG, "selectedIndex: changed: $index")
            // find row and deselect.
             var previousRow = tbl_talk_layout?.getChildAt(selectedIndex) as TableRow
             val playButton = previousRow.findViewById<ToggleButton>(R.id.row_talk_play)
             playButton.isChecked = false
             playButton.setButtonDrawable(R.drawable.ic_play_circle_solid)
         }
         this.selectedIndex = index



         val url = recording.url

         //if the button has been pressed, then play sound.
         if (row.findViewById<ToggleButton>(R.id.row_talk_play).isChecked) {
             //play sound.
           Intent().also { intent->
               intent.action = "com.nathan.equipapp.action.PLAY"
               intent.putExtra(URL_EXTRA, url)
               LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
               Log.d(TAG, "playSound: intent: ")

           }



         } else {
         }


     }

    fun inflateTableLayout() {
        getRecordingData()
        tbl_talk_layout?.isStretchAllColumns = true


    }

    val playButtonChecked = CompoundButton.OnCheckedChangeListener { view, checked ->
        // send click event to layout and get the player to play url.
        val row = view.parent.parent.parent as TableRow
        val index = tbl_talk_layout!!.indexOfChild(row)
        Log.d(TAG, "isChecked: $checked")
        if (checked) {
            view.setButtonDrawable(R.drawable.ic_stop_circle_solid)
        } else {
            view.setButtonDrawable(R.drawable.ic_play_circle_solid)
        }
         playSound(row, index)

    }

    fun getRecordingData() {
        this.recordings.clear()
        var recordingsCollection = db.collection("recordings")

        recordingsCollection.orderBy("date", Query.Direction.DESCENDING).orderBy("speaker").orderBy("url", Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
            for (document in result) {
                val question = document.get("question")
                //optional author
                var name = ""
                if (document.get("name") != null) {
                    name = document.get("name") as String
                }
                var speaker = ""
                if (document.get("speaker") != null) {
                    speaker = document.get("speaker") as String
                }
                val timestamp = document.getTimestamp("date")
                val date = timestamp?.toDate()
                val seconds = timestamp?.seconds
                val nanos = timestamp?.nanoseconds
                val url = document.get("url") as String
                /*Log.d(TAG, "name: $name")
                Log.d(TAG, "speaker: $speaker")

                Log.d(TAG, "url: $url")*/

                this.recordings.add(Recording(speaker, name, date!!, url))
                //TODO: writing a mutable list that holds the data needed for the table
            }
            fillTable()

        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }
    }

    fun fillTable() {
        var lp = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(10,10,10,10)
        recordings.forEach { recording ->
            val row = View.inflate(MyApplication.currentActivity, R.layout.row_talk_layout, null)
            val speaker = row.findViewById<TextView>(R.id.row_talk_speaker)
            val name = row.findViewById<TextView>(R.id.row_talk_recording)
            val date = row.findViewById<TextView>(R.id.row_talk_date)
            val play = row.findViewById<ToggleButton>(R.id.row_talk_play)
            play.setButtonDrawable(R.drawable.ic_play_circle_solid)
            speaker.text = recording.speaker
            name.text = recording.name
            date.text = recording.formattedDate
            play.setOnCheckedChangeListener(playButtonChecked)
            row.layoutParams = lp
            tbl_talk_layout?.addView(row, lp)


        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onTalkInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TalkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TalkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
