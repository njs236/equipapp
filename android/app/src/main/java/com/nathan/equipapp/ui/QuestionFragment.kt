package com.nathan.equipapp.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.nathan.equipapp.R
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
class QuestionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var editTextName: EditText? = null
    private var editTextQuestion: EditText? = null
    private var editTextSpeaker: EditText? = null
    private var buttonSend: Button? = null
    private var db: FirebaseFirestore? = null
    private var TAG = this.javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        editTextName = view.findViewById(R.id.editTextName)
        editTextQuestion = view.findViewById(R.id.editTextQuestion)
        editTextSpeaker = view.findViewById(R.id.editTextSpeaker)
        buttonSend = view.findViewById<Button>(R.id.buttonSend)

        buttonSend?.setOnClickListener { v: View? ->
            val author = editTextName!!.text.toString()
            val speaker = editTextSpeaker!!.text.toString()
            val question = editTextQuestion!!.text.toString()
            val data = HashMap<String, Any>()
            data["author"] = author
            data["question"] = question
            data["speaker"] = speaker


            var date = Calendar.getInstance().time

            data["cr_date"] = date


            if (speaker == "") {

                Toast.makeText(context, getString(R.string.valid_speaker), Toast.LENGTH_LONG).show()
            } else if (question == "") {
                Toast.makeText(context, getString(R.string.nonempty_question), Toast.LENGTH_LONG).show()

            } else {
                db?.collection("questions")?.add(data)?.addOnSuccessListener { ref ->

                    Log.d(TAG, "created Document: ${ref.id}")
                }?.addOnFailureListener { e ->

                    Log.w(TAG, "Error adding document: $e")

                }
            }

            editTextName?.text?.clear()
            editTextQuestion?.text?.clear()
            editTextSpeaker?.text?.clear()


        }
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onQuestionInteraction(uri)
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
        fun onQuestionInteraction(uri: Uri)
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
