package com.erman.percentagecalculator.di

import androidx.room.Room
import com.erman.percentagecalculator.BuildConfig
import com.erman.percentagecalculator.data.local.AppDatabase
import com.erman.percentagecalculator.data.repository.HistoryRepositoryImpl
import com.erman.percentagecalculator.data.repository.PreferencesRepositoryImpl
import com.erman.percentagecalculator.domain.CalculationMiddleware
import com.erman.percentagecalculator.domain.CalculationReducer
import com.erman.percentagecalculator.domain.LoggingMiddleware
import com.erman.percentagecalculator.domain.batch.BatchMiddleware
import com.erman.percentagecalculator.domain.batch.BatchReducer
import com.erman.percentagecalculator.domain.compoundinterest.CompoundInterestMiddleware
import com.erman.percentagecalculator.domain.compoundinterest.CompoundInterestReducer
import com.erman.percentagecalculator.domain.history.HistoryMiddleware
import com.erman.percentagecalculator.domain.history.HistoryReducer
import com.erman.percentagecalculator.domain.home.HomeMiddleware
import com.erman.percentagecalculator.domain.home.HomeReducer
import com.erman.percentagecalculator.domain.repository.HistoryRepository
import com.erman.percentagecalculator.domain.repository.PreferencesRepository
import com.erman.percentagecalculator.domain.settings.SettingsMiddleware
import com.erman.percentagecalculator.domain.settings.SettingsReducer
import com.erman.percentagecalculator.domain.tipcalculator.TipCalculatorMiddleware
import com.erman.percentagecalculator.domain.tipcalculator.TipCalculatorReducer
import com.erman.percentagecalculator.presentation.viewModel.BatchViewModel
import com.erman.percentagecalculator.presentation.viewModel.CompoundInterestViewModel
import com.erman.percentagecalculator.presentation.viewModel.HistoryViewModel
import com.erman.percentagecalculator.presentation.viewModel.HomeViewModel
import com.erman.percentagecalculator.presentation.viewModel.PercentageCalculatorViewModel
import com.erman.percentagecalculator.presentation.viewModel.SettingsViewModel
import com.erman.percentagecalculator.presentation.viewModel.TipCalculatorViewModel
import com.erman.percentagecalculator.domain.service.CalculationProvider
import com.erman.percentagecalculator.domain.service.CalculationService
import com.erman.percentagecalculator.domain.service.CompoundInterestProvider
import com.erman.percentagecalculator.domain.service.CompoundInterestService
import com.erman.percentagecalculator.domain.service.HistoryProvider
import com.erman.percentagecalculator.domain.service.HistoryService
import com.erman.percentagecalculator.domain.service.ShareService
import com.erman.percentagecalculator.data.ShareServiceImpl
import com.erman.percentagecalculator.domain.service.TipCalculationProvider
import com.erman.percentagecalculator.domain.service.TipCalculationService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        single<CalculationService> { CalculationProvider() }

        single<TipCalculationService> { TipCalculationProvider() }

        single<CompoundInterestService> { CompoundInterestProvider() }

        single<ShareService> { ShareServiceImpl() }

        single<PreferencesRepository> { PreferencesRepositoryImpl(androidContext()) }

        single<HistoryService> { HistoryProvider(get()) }

        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "percentage_calculator_db",
            ).build()
        }

        single { get<AppDatabase>().historyDao() }

        single<HistoryRepository> { HistoryRepositoryImpl(get()) }

        viewModel {
            HistoryViewModel(
                reducer = HistoryReducer(),
                middlewares = listOf(HistoryMiddleware(get())),
            )
        }

        viewModel {
            PercentageCalculatorViewModel(
                reducer = CalculationReducer(),
                middlewares =
                    buildList {
                        if (BuildConfig.DEBUG) add(LoggingMiddleware())
                        add(CalculationMiddleware(get(), get()))
                    },
            )
        }

        viewModel {
            TipCalculatorViewModel(
                reducer = TipCalculatorReducer(),
                middlewares = listOf(TipCalculatorMiddleware(get(), get())),
            )
        }

        viewModel {
            CompoundInterestViewModel(
                reducer = CompoundInterestReducer(),
                middlewares = listOf(CompoundInterestMiddleware(get(), get())),
            )
        }

        viewModel {
            HomeViewModel(
                reducer = HomeReducer(),
                middlewares = listOf(HomeMiddleware(get(), get())),
            )
        }

        viewModel {
            SettingsViewModel(
                reducer = SettingsReducer(),
                middlewares = listOf(SettingsMiddleware(get())),
            )
        }

        viewModel {
            BatchViewModel(
                reducer = BatchReducer(),
                middlewares = listOf(BatchMiddleware(get(), get())),
            )
        }
    }
