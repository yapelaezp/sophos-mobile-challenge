package com.example.sophos_mobile_app.ui.documents

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentSeeDocumentsBinding
import com.example.sophos_mobile_app.utils.AppLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeeDocumentsFragment : Fragment() {

    private val args: SeeDocumentsFragmentArgs by navArgs()

    private var _binding: FragmentSeeDocumentsBinding? = null
    private val binding get() = _binding!!
    private val appLanguage = AppLanguage()

    private val seeDocumentsViewModel: SeeDocumentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeeDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents()
        setListeners()
        observeViewModel()
    }

    private fun setListeners() {
        binding.toolbarViewDocumentsScreen.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.action_language -> {
                    lifecycleScope.launch { appLanguage.changeLanguage() }
                    true
                }
                R.id.action_main_menu -> {
                    findNavController().popBackStack(R.id.menuFragmentDestination, false)
                    true
                }
                R.id.action_send_docs -> {
                    navigateToSendDocs()
                    true
                }
                R.id.action_offices -> {
                    navigateToOffices()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        seeDocumentsViewModel.documents.observe(viewLifecycleOwner){ documents ->
            binding.rvViewDocuments.adapter = DocumentDetailAdapter(documents){ registerId ->
                seeDocumentsViewModel.getDocumentDetail(registerId)
            }
        }
        seeDocumentsViewModel.imageBitmap.observe(viewLifecycleOwner){ imageBitmap ->
            binding.ivViwDocumentsAttached.setImageBitmap(imageBitmap)
        }
    }

    private fun setComponents() {
        //Get document list
        seeDocumentsViewModel.getDocuments(args.email)
        //Set Toolbar
        binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_see_docs).isVisible = false
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()){
                binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_language).title = "Español"
            } else{
                binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_language).title = "English"
            }
        }
    }

    private fun navigateToSendDocs() {
        val action = SeeDocumentsFragmentDirections.actionViewDocumentsFragmentDestinationToSendDocumentsFragmentDestination(null)
        findNavController().navigate(action)
    }

    private fun navigateToOffices() {
        val action = SeeDocumentsFragmentDirections.actionViewDocumentsFragmentDestinationToPermissionsFragment(Manifest.permission.ACCESS_COARSE_LOCATION)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}