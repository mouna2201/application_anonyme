package com.example.application_anonyme.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.votrenom.anonymoussocial.databinding.FragmentCreatePostBinding
import com.votrenom.anonymoussocial.utils.*

class CreatePostFragment : DialogFragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()

        // Focus automatique sur le champ de texte
        binding.etContent.requestFocus()
        binding.etContent.showKeyboard()
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnPublish.setOnClickListener {
            val content = binding.etContent.text.toString().trim()

            if (validateContent(content)) {
                binding.root.hideKeyboard()
                viewModel.createPost(content)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.createResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    showLoading(true)
                }
                is NetworkResult.Success -> {
                    showLoading(false)

                    val isApproved = result.data?.post?.isApproved
                    val message = when (isApproved) {
                        true -> "Post publié avec succès!"
                        false -> "Votre post a été bloqué par la modération."
                        null -> "Votre post est en cours de vérification."
                        else -> "Post créé!"
                    }

                    context?.showToast(message)
                    dismiss()
                }
                is NetworkResult.Error -> {
                    showLoading(false)
                    context?.showToast(result.message ?: "Erreur de publication")
                }
            }
        }
    }

    private fun validateContent(content: String): Boolean {
        if (content.isEmpty()) {
            binding.tilContent.error = "Le contenu ne peut pas être vide"
            return false
        }

        if (content.length < Constants.MIN_POST_LENGTH) {
            binding.tilContent.error = "Contenu trop court (min ${Constants.MIN_POST_LENGTH} caractères)"
            return false
        }

        if (content.length > Constants.MAX_POST_LENGTH) {
            binding.tilContent.error = "Contenu trop long (max ${Constants.MAX_POST_LENGTH} caractères)"
            return false
        }

        binding.tilContent.error = null
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visible()
        } else {
            binding.progressBar.gone()
        }
        binding.btnPublish.isEnabled = !isLoading
        binding.btnCancel.isEnabled = !isLoading
        binding.etContent.isEnabled = !isLoading
    }

    override fun onStart() {
        super.onStart()
        // Définir la taille du dialogue
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}