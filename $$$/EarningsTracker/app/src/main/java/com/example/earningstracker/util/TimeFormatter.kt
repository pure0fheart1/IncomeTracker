package com.example.earningstracker.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeFormatter {
    
    // Format earnings with 2 decimal places
    fun formatEarnings(earnings: Double): String {
        return String.format("%.2f", earnings)
    }
    
    // Format duration (HH:MM:SS)
    fun formatDuration(durationInSeconds: Long): String {
        val hours = TimeUnit.SECONDS.toHours(durationInSeconds)
        val minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) % 60
        val seconds = durationInSeconds % 60
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    
    // Format date (MMM dd, yyyy - HH:mm)
    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
        return formatter.format(date)
    }
    
    // Format simple date (MMM dd, yyyy)
    fun formatSimpleDate(date: Date): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(date)
    }
} 