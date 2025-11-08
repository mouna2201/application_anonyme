package com.votrenom.anonymoussocial.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.votrenom.anonymoussocial.databinding.FragmentFeedBinding
import com.votrenom.anonymoussocial.ui.post.CreatePostFragment
import com.votrenom.anonymoussocial.utils.*

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        viewModel.loadFeed()
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter()
        binding.rvPosts.layoutManager = LinearLayoutManager(context)
        binding.rvPosts.adapter = adapter
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.fabCreatePost.setOnClickListener {
            showCreatePostDialog()
        }
    }

    private fun observeViewModel() {
        viewModel.feedPosts.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    if (!binding.swipeRefresh.isRefreshing) {
                        binding.progressBar.visible()
                    }
                    binding.emptyState.gone()
                }
                is NetworkResult.Success -> {
                    binding.progressBar.gone()
                    binding.swipeRefresh.isRefreshing = false

                    result.data?.let { feedResponse ->
                        if (feedResponse.posts.isEmpty()) {
                            binding.emptyState.visible()
                            binding.rvPosts.gone()
                        } else {
                            binding.emptyState.gone()
                            binding.rvPosts.visible()
                            adapter.submitList(feedResponse.posts)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    binding.progressBar.gone()
                    binding.swipeRefresh.isRefreshing = false
                    context?.showToast(result.message ?: "Erreur de chargement")
                }
            }
        }
    }

    private fun showCreatePostDialog() {
        val dialog = CreatePostFragment()
        dialog.show(parentFragmentManager, "CreatePostDialog")
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
