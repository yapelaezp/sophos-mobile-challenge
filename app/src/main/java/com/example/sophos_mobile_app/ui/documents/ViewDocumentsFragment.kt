package com.example.sophos_mobile_app.ui.documents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentViewDocumentsBinding
import com.example.sophos_mobile_app.utils.AppLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewDocumentsFragment : Fragment() {

    private val args: ViewDocumentsFragmentArgs by navArgs()

    private var _binding: FragmentViewDocumentsBinding? = null
    private val binding get() = _binding!!
    private val appLanguage = AppLanguage()

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
        setListeners()
        observeViewModel()
    }

    private fun setListeners() {
        binding.toolbarViewDocumentsScreen.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.action_language -> {
                    changeLanguage()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        viewDocumentsViewModel.documents.observe(viewLifecycleOwner){ documents ->
            binding.rvViewDocuments.adapter = DocumentDetailAdapter(documents){ registerId ->
                viewDocumentsViewModel.getDocumentDetail(registerId)
            }
        }
        viewDocumentsViewModel.imageBitmap.observe(viewLifecycleOwner){ imageBitmap ->
            binding.ivViwDocumentsAttached.setImageBitmap(imageBitmap)
        }
    }

    private fun setComponents() {
        //Get document list
        viewDocumentsViewModel.getDocuments(args.email)
        //Set Toolbar
        appLanguage.currentLocaleName?.let {
            if ("español" !in it.lowercase()){
                binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_language).title = "Español"
            } else{
                binding.toolbarViewDocumentsScreen.menu.findItem(R.id.action_language).title = "English"
            }
        }
    }

    private fun changeLanguage(){
        appLanguage.currentLocaleName?.let {
            lifecycleScope.launch { appLanguage.changeLanguage() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}