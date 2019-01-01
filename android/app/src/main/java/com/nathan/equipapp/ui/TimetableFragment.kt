package com.nathan.equipapp.ui

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView

import com.nathan.equipapp.R
import java.io.BufferedReader
import java.io.InputStreamReader

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TimetableFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TimetableFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TimetableFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var timetableLayout : View?= null
    private var timetable_content: LinearLayout? = null
    private var TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       timetableLayout = inflater.inflate(R.layout.fragment_timetable, container, false)
        inflateTimetable()
        return timetableLayout
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onTimetableInteraction(uri)
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
        fun onTimetableInteraction(uri: Uri)
    }

    fun inflateTimetable() {
        // find content tab.
        timetable_content = timetableLayout?.findViewById(R.id.timetable_content)
        // for each day
        readTableData()

    }

    fun readTableData(): ArrayList<String> {
        val days: Array<String> = arrayOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

        var fileIn = context?.assets?.open("timetable.txt")
        var reader = InputStreamReader(fileIn)
        var speakerlist = ArrayList<String>()
        var inputString = BufferedReader(reader).useLines { lines->
            val results = StringBuilder()
            lines.forEach {
                Log.d(TAG, "it: $it")
                var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.setMargins(10,10,10,10)
                val row = LinearLayout(context)
                if (days.contains(it)) {
                    val colorValue = ContextCompat.getColor(context!!, R.color.grey)
                    row.setBackgroundColor(colorValue)
                }
                row.orientation = LinearLayout.HORIZONTAL
                row.layoutParams = lp
                val text = TextView(context)
                text.text = it
                row.addView(text)
                timetable_content?.addView(row, lp)







            }
            results.toString()
        }
        return speakerlist




    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimetableFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimetableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
