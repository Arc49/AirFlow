package com.arc49.airflow.presentations.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arc49.airflow.data.RoutineRepository
import com.arc49.airflow.presentations.tabs.Exercise
import com.arc49.airflow.presentations.tabs.Routine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RoutineViewModel(
    private val repository: RoutineRepository
) : ViewModel() {
    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines.asStateFlow()

    private val _selectedRoutine = MutableStateFlow<Routine?>(null)
    val selectedRoutine: StateFlow<Routine?> = _selectedRoutine.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _remainingTime = MutableStateFlow(0)
    val remainingTime: StateFlow<Int> = _remainingTime.asStateFlow()

    init {
        viewModelScope.launch {
            repository.routines.collectLatest { routines ->
                _routines.value = routines
            }
        }
    }

    fun selectRoutine(routine: Routine?) {
        _selectedRoutine.value = routine
    }

    fun toggleTimer() {
        _isTimerRunning.value = !_isTimerRunning.value
    }

    fun setRemainingTime(time: Int) {
        _remainingTime.value = time
    }

    fun addRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.addRoutine(routine)
        }
    }

    fun updateRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.updateRoutine(routine)
        }
    }

    fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            repository.deleteRoutine(routineId)
        }
    }

    fun createNewRoutine(
        title: String,
        description: String,
        exercises: List<Exercise>
    ) {
        val newRoutine = Routine(
            id = System.currentTimeMillis().toString(),
            title = title,
            description = description,
            exercises = exercises
        )
        addRoutine(newRoutine)
    }
} 