package com.example.sophos_mobile_app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private val args: MenuFragmentArgs by navArgs()

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        setComponents()
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.btnMenuScreenSendDocs.setOnClickListener {
            val action = MenuFragmentDirections.actionToSendDocumentsFragmentDestination()
            findNavController().navigate(action)
        }
        binding.btnMenuScreenSeeDocs.setOnClickListener {
            val action =
                MenuFragmentDirections.actionMenuFragmentDestinationToViewDocumentsFragmentDestination(
                    //args.userEmail
                    "naruto@hotmail.com"
                )
            findNavController().navigate(action)
        }
    }

    private fun setComponents() {
        //Set toolbar
        binding.toolbarMenuScreen.title = args.userName
        binding.toolbarMenuScreen.overflowIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_menu_24)
    }

}