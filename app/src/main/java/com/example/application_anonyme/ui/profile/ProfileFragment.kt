package com.example.application_anonyme.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.votrenom.anonymoussocial.databinding.FragmentProfileBinding
import com.votrenom.anonymoussocial.ui.auth.LoginActivity
import com.votrenom.anonymoussocial.ui.feed.PostAdapter
import com.votrenom.anonymoussocial.utils.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        setupListeners()
        observeViewModel()

        viewModel.loadUserPosts()
    }

    private fun setupUI() {
        binding.tvPseudo.text = "@${viewModel.getUserPseudo()}"
    }

    private fun setupRecyclerView() {
        adapter = PostAdapter()
        binding.rvUserPosts.layoutManager = LinearLayoutManager(context)
        binding.rvUserPosts.adapter = adapter
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadUserPosts()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun observeViewModel() {
        viewModel.userPosts.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    if (!binding.swipeRefresh.isRefreshing) {
                        binding.progressBar.visible()
                    }
                }
                is NetworkResult.Success -> {
                    binding.progressBar.gone()
                    binding.swipeRefresh.isRefreshing = false

                    result.data?.let { posts ->
                        binding.tvPostCount.text = "${posts.size} posts"

                        if (posts.isEmpty()) {
                            binding.emptyState.visible()
                            binding.rvUserPosts.gone()
                        } else {
                            binding.emptyState.gone()
                            binding.rvUserPosts.visible()
                            adapter.submitList(posts)
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

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Déconnexion")
            .setMessage("Voulez-vous vraiment vous déconnecter ?")
            .setPositiveButton("Oui") { _, _ ->
                viewModel.logout()
                navigateToLogin()
            }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun navigateToLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}