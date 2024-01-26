package com.lovevery.ui.messages

/**
 * Data validation state of the create messageform.
 */
data class CreateMessageFormState(
    val usernameError: Int? = null,
    val subjectError: Int? = null,
    val messageError: Int? = null,
    val isDataValid: Boolean = false
)