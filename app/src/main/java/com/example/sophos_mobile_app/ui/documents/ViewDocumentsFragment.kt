package com.example.sophos_mobile_app.ui.documents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.databinding.FragmentViewDocumentsBinding
import com.example.sophos_mobile_app.ui.menu.MenuFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewDocumentsFragment : Fragment() {

    private val args: ViewDocumentsFragmentArgs by navArgs()

    private var _binding: FragmentViewDocumentsBinding? = null
    private val binding get() = _binding!!

    private val viewDocumentsViewModel: ViewDocumentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewDocumentsViewModel.documents.observe(viewLifecycleOwner){ documents ->
            binding.rvViewDocuments.adapter = DocumentAdapter(documents)
        }
    }

    private fun setComponents() {
        viewDocumentsViewModel.getDocuments(args.email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}