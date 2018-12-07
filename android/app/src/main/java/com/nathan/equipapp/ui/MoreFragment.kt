package com.nathan.equipapp.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.joanzapata.iconify.widget.IconTextView
import com.nathan.equipapp.MyApplication

import com.nathan.equipapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MoreFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MoreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var rl_questions: RelativeLayout? = null
    private var rl_send_noti: RelativeLayout? = null
    private var fa_send_noti_button: IconTextView? = null
    private var fa_questions_button: IconTextView? = null
    private var tv_questions: TextView? = null
    private var tv_send_noti: TextView? = null
    private var TAG = this.javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        rl_questions?.setOnClickListener { v ->
            // send to the questionfragment.

            val fragment = QuestionReceiverFragment()
            Log.d(TAG, "question displayer selected")
            changeView(fragment)
        }


        rl_send_noti?.setOnClickListener { v->

            val fragment = NotificationFragment()
            Log.d(TAG, "notification sender selected")
            changeView(fragment)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_more, container, false)
        Log.d(TAG, "more created")
        rl_questions = view.findViewById(R.id.rl_questions)
        rl_send_noti = view.findViewById(R.id.rl_send_noti)
        fa_questions_button = view.findViewById(R.id.fa_questions_button)
        fa_send_noti_button = view.findViewById(R.id.fa_send_noti_button)
        tv_questions = view.findViewById(R.id.tv_questions)
        tv_send_noti = view.findViewById(R.id.tv_send_noti)
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onMoreInteraction(uri)
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
        fun onMoreInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MoreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun changeView(fragment: Fragment) {
        var fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_layout, fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }
}
