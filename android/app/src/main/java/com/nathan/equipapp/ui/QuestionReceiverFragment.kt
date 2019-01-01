package com.nathan.equipapp.ui
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import android.widget.ToggleButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nathan.equipapp.MyApplication
import com.nathan.equipapp.R
import com.nathan.equipapp.assets.Question
import org.w3c.dom.Text
import java.sql.Timestamp
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

class QuestionReceiverFragment: Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var questions = ArrayList<Question>()
    private lateinit var tbl_questions_layout: TableLayout
    private val TAG = this.javaClass.simpleName

    var db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    //TODO creating table with layout for questions received.

    // Table displays the question, the speaker and optionally the author. sort by creation date.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragments_questions, container, false)

        tbl_questions_layout = view.findViewById(R.id.tbl_questions_layout)
        inflateTableLayout()

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onQuestionsInteraction(uri)
    }

    private fun getQuestionsData() {
        questions.clear()

        var questionsCollection = db.collection("questions")

        questionsCollection.orderBy("cr_date", Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
            for (document in result) {
                val question = document.get("question")
                //optional author
                val author = document.get("author") as String
                val speaker = document.get("speaker") as String
                val timestamp = document.getTimestamp("cr_date")
                val description = document.get("question") as String
                val id = document.id
                val date = timestamp?.toDate()

                Log.d(TAG, "author: $author")
                Log.d(TAG, "speaker: $speaker")
                Log.d(TAG, "cr_date: ${date.toString()}")
                Log.d(TAG, "question: $question")

                questions.add(Question(speaker, author, description, date!!, id))
                //TODO: writing a mutable list that holds the data needed for the table
            }
            fillTable()
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }


    }

    fun inflateTableLayout() {
        getQuestionsData()
        tbl_questions_layout?.isStretchAllColumns = true


    }

    private fun fillTable() {
        tbl_questions_layout?.removeAllViews()
        var lp = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
        lp.setMargins(10,10,10,10)

        questions.forEach { question ->
            val row = View.inflate(MyApplication.currentActivity, R.layout.row_question_layout, null)
            val speaker = row.findViewById<TextView>(R.id.row_question_speaker)
            val description = row.findViewById<TextView>(R.id.row_question_description)
            val author = row.findViewById<TextView>(R.id.row_question_author)
            val trash = row.findViewById<TextView>(R.id.question_trash)
            trash.setOnClickListener { view->

                db?.collection("questions").document(question.documentID!!).delete().addOnSuccessListener { result->
                    Log.d(TAG, "deleted document")
                }
                getQuestionsData()

            }
            speaker.text = question.speaker
            description.text = question.description
            author.text = question.author
            row.layoutParams = lp
            tbl_questions_layout?.addView(row, lp)


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
        fun onQuestionsInteraction(uri: Uri)
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