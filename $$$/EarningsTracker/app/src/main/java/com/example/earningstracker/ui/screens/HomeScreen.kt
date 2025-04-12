package com.example.earningstracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.earningstracker.ui.components.EarningsDisplay
import com.example.earningstracker.ui.components.SessionSummary
import com.example.earningstracker.ui.components.TimerControls
import com.example.earningstracker.ui.components.WageInput
import com.example.earningstracker.ui.navigation.AppBar
import com.example.earningstracker.ui.viewmodel.EarningsViewModel

@Composable
fun HomeScreen(
    onNavigateToHistory: () -> Unit,
    viewModel: EarningsViewModel = viewModel()
) {
    val hourlyWage by viewModel.hourlyWage.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val formattedTime by viewModel.formattedTime.collectAsState()
    val formattedEarnings by viewModel.formattedEarnings.collectAsState()
    val sessionDuration by viewModel.sessionDuration.collectAsState()
    val sessionEarnings by viewModel.sessionEarnings.collectAsState()
    
    Scaffold(
        topBar = {
            AppBar(
                onHistoryClick = onNavigateToHistory
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
            ) {
                // Show hourly wage input if not set yet
                if (hourlyWage <= 0) {
                    WageInput(
                        initialWage = hourlyWage,
                        onSaveWage = { wage ->
                            viewModel.saveHourlyWage(wage)
                        }
                    )
                }
                
                // Show earnings display
                EarningsDisplay(
                    earnings = formattedEarnings,
                    timer = formattedTime
                )
                
                // Show timer controls
                TimerControls(
                    isRunning = isTimerRunning,
                    onStartClick = { viewModel.startTimer() },
                    onStopClick = { viewModel.stopTimer() },
                    onResetClick = { viewModel.resetTimer() }
                )
                
                // Show session summary if timer is stopped and there's some data
                if (!isTimerRunning && sessionDuration > 0) {
                    SessionSummary(
                        duration = formattedTime,
                        earnings = formattedEarnings,
                        onSaveClick = { viewModel.saveSession() },
                        onDiscardClick = { viewModel.discardSession() }
                    )
                }
            }
        }
    }
} 