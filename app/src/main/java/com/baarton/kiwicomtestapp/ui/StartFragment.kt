package com.baarton.kiwicomtestapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.ui.results.ResultsFragment
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject


class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private val databaseModule: AppDatabase by inject()

    private lateinit var btnStart: Button
    private lateinit var btnNuke: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnStart = view.findViewById(R.id.btn_start)
        btnStart.setOnClickListener { start() }

        //TODO remove after tests
        btnNuke = view.findViewById(R.id.btn_nuke)
        btnNuke.setOnClickListener {
            lifecycleScope.launch {
                databaseModule.flightDao().nuke()
            }
        }
    }

    private fun start() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, ResultsFragment.newInstance())
            .commitNow()
    }

}