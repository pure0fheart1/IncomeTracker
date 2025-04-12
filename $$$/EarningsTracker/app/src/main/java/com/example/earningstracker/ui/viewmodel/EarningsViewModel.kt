package com.example.earningstracker.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.earningstracker.data.database.AppDatabase
import com.example.earningstracker.data.model.WorkSession
import com.example.earningstracker.data.preferences.UserPreferences
import com.example.earningstracker.data.repository.WorkSessionRepository
import com.example.earningstracker.util.TimeFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class EarningsViewModel(application: Application) : AndroidViewModel(application) {
    
    // Repository
    private val repository: WorkSessionRepository
    
    // User preferences
    private val userPreferences = UserPreferences(application)
    
    // Timer values
    private val _timerSeconds = MutableStateFlow(0L)
    val timerSeconds: StateFlow<Long> = _timerSeconds
    
    private val _formattedTime = MutableStateFlow("00:00:00")
    val formattedTime: StateFlow<String> = _formattedTime
    
    // Earnings values
    private val _currentEarnings = MutableStateFlow(0.0)
    val currentEarnings: StateFlow<Double> = _currentEarnings
    
    private val _formattedEarnings = MutableStateFlow("0.00")
    val formattedEarnings: StateFlow<String> = _formattedEarnings
    
    // Hourly wage
    private val _hourlyWage = MutableStateFlow(0.0)
    val hourlyWage: StateFlow<Double> = _hourlyWage
    
    // Timer state
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning
    
    // Session data
    private val _startTime = MutableLiveData<Date>()
    private val _endTime = MutableLiveData<Date>()
    
    // Current session summary
    private val _sessionDuration = MutableStateFlow(0L)
    val sessionDuration: StateFlow<Long> = _sessionDuration
    
    private val _sessionEarnings = MutableStateFlow(0.0)
    val sessionEarnings: StateFlow<Double> = _sessionEarnings
    
    // Timer job
    private var timerJob: Job? = null
    
    init {
        val workSessionDao = AppDatabase.getDatabase(application).workSessionDao()
        repository = WorkSessionRepository(workSessionDao)
        
        // Load hourly wage from preferences
        viewModelScope.launch {
            _hourlyWage.value = userPreferences.hourlyWage.first()
        }
    }
    
    // Save hourly wage
    fun saveHourlyWage(wage: Double) {
        if (wage <= 0) return
        
        viewModelScope.launch {
            userPreferences.saveHourlyWage(wage)
            _hourlyWage.value = wage
        }
    }
    
    // Start timer
    fun startTimer() {
        if (_isTimerRunning.value) return
        
        _isTimerRunning.value = true
        _startTime.value = Date()
        
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timerSeconds.value += 1
                _formattedTime.value = TimeFormatter.formatDuration(_timerSeconds.value)
                
                // Calculate earnings
                if (_hourlyWage.value > 0) {
                    val earningsPerSecond = _hourlyWage.value / 3600.0
                    _currentEarnings.value = earningsPerSecond * _timerSeconds.value
                    _formattedEarnings.value = TimeFormatter.formatEarnings(_currentEarnings.value)
                }
            }
        }
    }
    
    // Stop timer
    fun stopTimer() {
        if (!_isTimerRunning.value) return
        
        timerJob?.cancel()
        _isTimerRunning.value = false
        _endTime.value = Date()
        
        // Set session summary
        _sessionDuration.value = _timerSeconds.value
        _sessionEarnings.value = _currentEarnings.value
    }
    
    // Reset timer
    fun resetTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
        _timerSeconds.value = 0
        _formattedTime.value = "00:00:00"
        _currentEarnings.value = 0.0
        _formattedEarnings.value = "0.00"
        _sessionDuration.value = 0
        _sessionEarnings.value = 0.0
    }
    
    // Save session
    fun saveSession(): Long {
        val startTime = _startTime.value ?: return -1
        val endTime = _endTime.value ?: return -1
        
        val workSession = WorkSession(
            startTime = startTime,
            endTime = endTime,
            hourlyWage = _hourlyWage.value,
            durationSeconds = _sessionDuration.value,
            earnings = _sessionEarnings.value
        )
        
        var sessionId = -1L
        
        viewModelScope.launch {
            sessionId = repository.insertWorkSession(workSession)
            resetTimer()
        }
        
        return sessionId
    }
    
    // Discard session
    fun discardSession() {
        resetTimer()
    }
    
    // Get all work sessions
    val allWorkSessions = repository.allWorkSessions
    
    // Delete work session
    fun deleteWorkSession(workSession: WorkSession) {
        viewModelScope.launch {
            repository.deleteWorkSession(workSession)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
} 