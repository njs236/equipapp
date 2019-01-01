package com.nathan.equipapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.youtube.player.*
import com.nathan.equipapp.R
import com.nathan.equipapp.Utils.DeveloperKey
import java.io.BufferedReader
import java.io.InputStreamReader


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var videoUrl = "vQ2x4R9r7QA"
    private var registerUrl = "http://www.equipconference.org.nz/registration-1-1/"
    private var northRegisterUrl = "http://www.equipconference.org.nz/registration-1/"
    private var count = 0
    private var tv_speaker1: TextView? = null
    private var tv_speaker2: TextView? = null
    private var youtube_viewer: YouTubePlayerSupportFragment? = null
    private var youtube_player: YouTubePlayer? = null
    private var register_south: Button? = null
    private var register_north: Button? = null
    private var details_title: TextView? = null
    private var ll_menu_map : LinearLayout? = null
    private var ll_menu_question: LinearLayout? = null
    private var ll_menu_talks: LinearLayout? = null
    private var ll_menu_timetable: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    fun readTableData(): ArrayList<String> {
        count = 0
        var fileIn = context?.assets?.open("speaker_info.txt")
        var reader = InputStreamReader(fileIn)
        var speakerlist = ArrayList<String>()
        var inputString = BufferedReader(reader).useLines { lines->
            val results = StringBuilder()
            lines.forEach { speakerlist.add(it)}
            results.toString()
        }
        return speakerlist




    }
    fun writeSpeakerText() {
        val array = readTableData()
        val speakerText = ArrayList<String>()
        // split array down "lines"
        for (speaker in array) {
            val newArray = speaker.split("/")
            var newText = ""
            for (word in newArray) {
                newText += "$word \n"
            }
            speakerText.add(newText)

        }

        tv_speaker1?.text = speakerText[0]
        tv_speaker2?.text = speakerText[1]
    }

    val youtubeOnInitializedListener = YoutubeOnInitializedListener()

    inner class YoutubeOnInitializedListener : YouTubePlayer.OnInitializedListener {
        override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

        }

        override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
            youtube_player = p1
            p1?.cueVideo(videoUrl)
            p1?.play()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var view = inflater.inflate(R.layout.fragment_details, container, false)
        ll_menu_map = view.findViewById(R.id.ll_details_menu_map)
        ll_menu_question = view.findViewById(R.id.ll_details_menu_question)
        ll_menu_talks = view.findViewById(R.id.ll_details_menu_talks)
        ll_menu_timetable = view.findViewById(R.id.ll_details_menu_timetable)

        ll_menu_map?.setOnClickListener { onButtonPressed(Uri.parse("map")) }
        ll_menu_question?.setOnClickListener { onButtonPressed(Uri.parse("question")) }
        ll_menu_talks?.setOnClickListener { onButtonPressed(Uri.parse("talks")) }
        ll_menu_timetable?.setOnClickListener { onButtonPressed(Uri.parse("timetable")) }
        return view
    }
/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        youtube_viewer = YouTubePlayerSupportFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.playerView_details, youtube_viewer as Fragment).commit()
        youtube_viewer!!.initialize(DeveloperKey.DEVELOPER_KEY, youtubeOnInitializedListener)

    }
*/


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onDetailsInteraction(uri)
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
        fun onDetailsInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
