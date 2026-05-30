package com.nutrilens.presentation.util

/**
 * Eventos de una sola emisión (fire-and-forget).
 * Usados con SharedFlow en los ViewModels para acciones que no son estado:
 * navegar, mostrar Snackbar, etc.
 */
sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    data class NavigateTo(val route: String)     : UiEvent()
    data object NavigateBack                     : UiEvent()
}
