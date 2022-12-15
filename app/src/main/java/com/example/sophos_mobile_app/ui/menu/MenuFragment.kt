package com.example.sophos_mobile_app.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.databinding.FragmentMenuBinding
import com.example.sophos_mobile_app.databinding.FragmentSendDocumentsBinding
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
    }

    private fun setComponents() {
        binding.toolbarMenuScreen.title = args.userName
    }


}