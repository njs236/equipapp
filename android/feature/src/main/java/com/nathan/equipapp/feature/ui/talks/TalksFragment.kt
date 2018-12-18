package com.nathan.equipapp.feature.ui.talks

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nathan.equipapp.feature.R

class TalksFragment : Fragment() {

    companion object {
        fun newInstance() = TalksFragment()
    }

    private lateinit var viewModel: TalksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.talks_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TalksViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
