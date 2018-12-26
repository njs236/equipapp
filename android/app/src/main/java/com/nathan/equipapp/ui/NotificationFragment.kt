package com.nathan.equipapp.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.nathan.equipapp.R
import kotlinx.android.synthetic.main.fragment_notis.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QuestionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class NotificationFragment: Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    var db = FirebaseFirestore.getInstance()
    private val TAG = this.javaClass.simpleName
    var descriptionField :EditText?= null
    var dateField :EditText? = null
    var timeField :EditText?= null
    var sendButton: Button? = null
    var notiCheck: CheckBox? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // form fills out and stores notifications.





        //see the notification service for receiving notifications from firebase.
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_notis, container, false)
        descriptionField = view.findViewById(R.id.notiDescription)
        dateField = view.findViewById(R.id.notiDate)
        timeField = view.findViewById(R.id.notiTime)
        sendButton = view.findViewById(R.id.notiSend)
        notiCheck = view.findViewById(R.id.noti_checkInstant)
        notiCheck?.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d(TAG, "isChecked: $isChecked")
            if (isChecked) {
                dateField!!.visibility  = View.INVISIBLE
                timeField!!.visibility = View.INVISIBLE
            } else {
                dateField!!.visibility = View.VISIBLE
                timeField!!.visibility = View.VISIBLE
            }


        }

        sendButton?.setOnClickListener { result ->
            //send to firebase
            val data = HashMap<String, Any?>()
            data["description"] = descriptionField?.text.toString()
            if (noti_checkInstant.isChecked) {
                val cal = Calendar.getInstance()
                data["now"] = true
                data["date"] = cal.time
            } else {

                val date = dateField?.text.toString()
                val time = timeField?.text.toString()
                Log.d(TAG, "date: $date")
                Log.d(TAG, "time: $time")
                // change date to local time.
                val dateTime = "$date $time +1300"
                Log.d(TAG, "dateTime: $dateTime")
                val format = SimpleDateFormat("dd/MM/yyyy hh:mm Z", Locale.ENGLISH)

                data["date"] = format.parse(dateTime)
            }

            data["delay"] = 300


            db.collection("notis").add(data).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID:" + documentReference.id)
            }.addOnFailureListener{e ->
                Log.w(TAG, "Error adding document", e)
            }

        }
        return view

        // will be getting input names from this method.
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onNotificationInteraction(uri)
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
        fun onNotificationInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuestionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}