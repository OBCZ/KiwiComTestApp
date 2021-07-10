package com.baarton.kiwicomtestapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.baarton.kiwicomtestapp.R
import com.baarton.kiwicomtestapp.db.AppDatabase
import com.baarton.kiwicomtestapp.ui.results.ResultsFragment
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.logging.Level
import java.util.logging.Logger


class StartFragment : Fragment() {

    companion object {
        private val logger = Logger.getLogger(StartFragment::class.java.name)

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

        btnNuke = view.findViewById(R.id.btn_nuke)
        btnNuke.setOnClickListener {
            lifecycleScope.launch {
                logger.log(Level.INFO, "Nuking the DB: START")
                databaseModule.flightDao().nuke()
                logger.log(Level.INFO, "Nuking the DB: END")
                Toast.makeText(context, getString(R.string.text_start_flights_cleared), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun start() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, ResultsFragment.newInstance())
            .commitNow()
    }

}