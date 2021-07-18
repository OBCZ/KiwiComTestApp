package com.baarton.kiwicomtestapp.app

import com.baarton.kiwicomtestapp.ui.results.ResultsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


object AppModule {

    fun get(requestHandler: IRequestHandler, responseHandler: IResponseHandler, databaseModule: IDatabaseModule): Module {
        return module {
            single { requestHandler }
            single { responseHandler }
            single { databaseModule }
            viewModel { parameters -> ResultsViewModel(parameters.get()) }
        }
    }

}