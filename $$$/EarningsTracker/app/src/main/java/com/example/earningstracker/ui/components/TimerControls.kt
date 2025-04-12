package com.example.earningstracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.earningstracker.R

@Composable
fun TimerControls(
    isRunning: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val startButtonColor by animateColorAsState(
            targetValue = if (isRunning) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary,
            animationSpec = tween(durationMillis = 300), label = "StartButtonColor"
        )
        
        val stopButtonColor by animateColorAsState(
            targetValue = if (!isRunning) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.error,
            animationSpec = tween(durationMillis = 300), label = "StopButtonColor"
        )
        
        Button(
            onClick = { onStartClick() },
            enabled = !isRunning,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = startButtonColor,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.start))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Button(
            onClick = { onStopClick() },
            enabled = isRunning,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = stopButtonColor,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.stop))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Button(
            onClick = { onResetClick() },
            shape = CircleShape,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.reset))
        }
    }
} 