package com.arc49.airflow.presentations.tabs

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi

data class Exercise(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val description: String
)

data class Routine(
    val id: String,
    val title: String,
    val description: String,
    val exercises: List<Exercise>
)

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
internal fun RoutineTab(modifier: Modifier) {
    var selectedRoutine by remember { mutableStateOf<Routine?>(null) }
    val haptic = LocalHapticFeedback.current
    
    val routines = remember {
        listOf(
            Routine(
                id = "1",
                title = "Sprint Training",
                description = "Classic HIIT training for maximum bone stimulation",
                exercises = listOf(
                    Exercise("1", "HIIT Sprints", 5, 8, "20-30 seconds sprints with 1 minute rest"),
                    Exercise("2", "Hill Sprints", 5, 8, "15-20 seconds uphill sprints"),
                    Exercise("3", "AirFlow Breathing", 3, 1, "15 minutes of alternating high-intensity and slow breathing")
                )
            ),
            Routine(
                id = "2",
                title = "Strength Training",
                description = "Multi-joint exercises for maximum bone load",
                exercises = listOf(
                    Exercise("1", "Squats", 4, 8, "Focus on form and depth"),
                    Exercise("2", "Deadlifts", 3, 6, "Keep back straight"),
                    Exercise("3", "Overhead Press", 3, 8, "Full range of motion"),
                    Exercise("4", "Bench Press", 3, 8, "Control the weight"),
                    Exercise("5", "Neck Harness", 3, 10, "Isometric holds for jaw development")
                )
            ),
            Routine(
                id = "3",
                title = "GH and IGF-1 Maximizing",
                description = "Evening circuit training to boost growth hormones",
                exercises = listOf(
                    Exercise("1", "Circuit Training", 3, 1, "20 minutes of squats, push-ups, deadlifts, pull-ups with minimal rest"),
                    Exercise("2", "All-out Sprints", 5, 1, "30 seconds maximum effort"),
                    Exercise("3", "AirFlow Breathing", 3, 1, "15 minutes of alternating high-intensity and slow breathing")
                )
            )
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(routines) { routine ->
                RoutineCard(
                    routine = routine,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        selectedRoutine = routine
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = selectedRoutine != null,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            selectedRoutine?.let { routine ->
                RoutineDetail(
                    routine = routine,
                    onClose = { selectedRoutine = null }
                )
            }
        }
    }
}

@Composable
private fun RoutineCard(
    routine: Routine,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = routine.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = routine.description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${routine.exercises.size} exercises",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun RoutineDetail(
    routine: Routine,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = routine.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onClose) {
                    Text(
                        text = "×",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = routine.description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(routine.exercises) { exercise ->
                    ExerciseItem(exercise = exercise)
                }
            }
        }
    }
}

@Composable
private fun ExerciseItem(
    exercise: Exercise
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = exercise.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${exercise.sets} sets × ${exercise.reps} reps",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = exercise.description,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        }
    }
}