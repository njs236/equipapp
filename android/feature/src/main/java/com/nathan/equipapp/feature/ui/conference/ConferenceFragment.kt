package com.nathan.equipapp.feature.ui.conference

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nathan.equipapp.feature.R

class ConferenceFragment : Fragment() {

    companion object {
        fun newInstance() = ConferenceFragment()
    }

    private lateinit var viewModel: ConferenceViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.conference_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConferenceViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
