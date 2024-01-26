package com.lovevery.ui.messages

import androidx.lifecycle.Observer
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lovevery.R
import com.lovevery.databinding.FragmentNewMessageBinding
import com.lovevery.domain.models.Message
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewMessageFragment : Fragment() {

    private val newMessageViewModel: NewMessageViewModel by viewModels()
    private var _binding: FragmentNewMessageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.tvUsername
        val subjectEditText = binding.tvSubject
        val messageEditText = binding.tvMessageBody
        val saveButton = binding.btnSave
        val loadingProgressBar = binding.loading

        newMessageViewModel.newMessageFormState.observe(viewLifecycleOwner,
            Observer { formState ->
                if (formState == null) {
                    return@Observer
                }

                saveButton.isEnabled = formState.isDataValid

                formState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }

                formState.subjectError?.let {
                    subjectEditText.error = getString(it)
                }

                formState.messageError?.let {
                    messageEditText.error = getString(it)
                }
            })

        newMessageViewModel.newMessageResult.observe(viewLifecycleOwner,
            Observer { result ->
                result ?: return@Observer
                loadingProgressBar.visibility = View.GONE

                result.error?.let {
                    val appContext = context?.applicationContext
                    Toast.makeText(
                        appContext,
                        getString(R.string.create_message_failed),
                        Toast.LENGTH_LONG
                    ).show()
                }

                result.success?.let {
                    val appContext = context?.applicationContext
                    Toast.makeText(
                        appContext,
                        getString(R.string.create_message_success),
                        Toast.LENGTH_LONG
                    ).show()

                    findNavController().navigateUp()
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                newMessageViewModel.newMessageDataChanged(
                    usernameEditText.text.toString(),
                    subjectEditText.text.toString(),
                    messageEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        subjectEditText.addTextChangedListener(afterTextChangedListener)
        messageEditText.addTextChangedListener(afterTextChangedListener)

        saveButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE

            newMessageViewModel.saveMessage(
                Message(
                    user = usernameEditText.text.toString(),
                    subject = subjectEditText.text.toString(),
                    message = messageEditText.text.toString()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}