package com.example.earningstracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.earningstracker.R
import com.example.earningstracker.ui.components.SessionHistoryItem
import com.example.earningstracker.ui.navigation.AppBar
import com.example.earningstracker.ui.viewmodel.EarningsViewModel

@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: EarningsViewModel = viewModel()
) {
    val sessions by viewModel.allWorkSessions.collectAsState(initial = emptyList())
    
    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.session_history),
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (sessions.isEmpty()) {
                // Show empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.empty_history),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Show session list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                ) {
                    items(sessions) { session ->
                        SessionHistoryItem(
                            session = session,
                            onDeleteClick = { viewModel.deleteWorkSession(it) }
                        )
                    }
                }
            }
        }
    }
} 