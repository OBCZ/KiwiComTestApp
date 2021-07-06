package com.baarton.kiwicomtestapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.ui.results.ResultsFragment
import kotlinx.coroutines.*
import java.util.logging.Logger


class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()

        val logger = Logger.getLogger("KIWISEARCH")

    }

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
        btnNuke.setOnClickListener { runBlocking {
            launch { AppDatabase.getInstance(requireContext()).flightDao().nuke() }
        } }
    }

    private fun start() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container,
                ResultsFragment.newInstance()
            )
            .commitNow()
    }


}