package com.lovevery.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lovevery.R
import com.lovevery.databinding.FragmentHomeBinding
import com.lovevery.ui.home.adapters.MessagesAdapter
import com.lovevery.ui.main.MainActivity
import com.lovevery.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MessagesAdapter

    override fun onStart() {
        super.onStart()
        viewModel.retrieveAllMessages()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        activity?.title = ""
        activity?.actionBar?.hide();
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MessagesAdapter()
        with(binding) {
            rvMessages.adapter = adapter
            tvEmptyMessages.visibility = View.VISIBLE
            rvMessages.visibility = View.GONE
        }

        // Handle search
        activity?.let {
            (activity as MainActivity)
                .mainViewModel
                .searchQuery.observe(viewLifecycleOwner, Observer {query ->
                    if (!query.isNullOrEmpty()) {
                        viewModel.currentJob.cancel()
                        Timber.d("R2D2 home fragment received a query: $query")
                        viewModel.retrieveMessagesByUser(query)
                        binding.btnClear.visibility = View.VISIBLE
                        binding.tvTitle.text = String.format(
                            getString(R.string.showing_messages_from_template),
                            query
                        )
                        (activity as MainActivity).clearSearchQuery()
                    }
                })
        }

        viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->
            if (messages.isNotEmpty()) {
                with(binding) {
                    tvEmptyMessages.visibility = View.GONE
                    rvMessages.visibility = View.VISIBLE
                    adapter.submitList(messages)
                    if (refreshLayout.isRefreshing ) {
                        refreshLayout.isRefreshing = false
                    }
                }
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
            Timber.e(errorMessage)
        })

        binding.btnClear.setOnClickListener {
            clear()
        }

        binding.refreshLayout.setColorSchemeResources(
            R.color.primary
        )
        binding.refreshLayout.setOnRefreshListener {
            clear()
        }
    }

    private fun clear() {
        binding.btnClear.visibility = View.GONE
        binding.tvTitle.text = getString(R.string.welcome_label)
        viewModel.currentJob.cancel()
        viewModel.retrieveAllMessages()
        if (binding.refreshLayout.isRefreshing ) {
            binding.refreshLayout.isRefreshing = false
        }
    }

    companion object {
        private const val HIDE_INTRO_DELAY = 2000L
        private const val INTRO_ANIMATION_DURATION= 1000L
        private const val EMPTY_STRING = ""
    }
}