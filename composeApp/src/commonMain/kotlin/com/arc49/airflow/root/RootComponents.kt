package com.arc49.airflow.root

import com.arc49.airflow.database.supabase.auth.AuthViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RootComponents : KoinComponent {
    val viewModel : AuthViewModel by inject()
}
//class RootOpenAI : KoinComponent {
//    val viewModel : ChatCompletionViewModel by inject()
//}