package com.example.sophos_mobile_app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.R
import com.example.sophos_mobile_app.data.source.remote.api.ResponseStatus
import com.example.sophos_mobile_app.databinding.FragmentLoginBinding
import com.example.sophos_mobile_app.utils.UserDataStore
import com.example.sophos_mobile_app.utils.Validation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var userDataStore: UserDataStore

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val NAME = "name"
        const val BIOMETRIC_INTENTION = "biometric_intention"
        const val BIOMETRIC_EMAIL = "biometric_email"
        const val BIOMETRIC_PASSWORD = "biometric_password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDataStore = UserDataStore(requireContext())
        setupBiometricAccess()
        isUserLogged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setListeners()
        observeViewModel()
        return binding.root
    }

    private fun isUserLogged() {
        lifecycleScope.launch(Dispatchers.Main) {
            userDataStore.getDataStorePreferences().collect() { userPreferences ->
                if (userPreferences.email.isNotEmpty()) {
                    val action = LoginFragmentDirections.actionToMenuFragmentDestination(
                        userPreferences.name,
                        userPreferences.email
                    )
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun observeViewModel() {
        loginViewModel.user.observe(viewLifecycleOwner) { user ->
            val userEmail = binding.etvLoginEmail.text.toString()
            val userPassword = binding.etvLoginPassword.text.toString()
            lifecycleScope.launch(Dispatchers.IO) {
                user?.name?.let {
                    userDataStore.saveUserInDataStore(userEmail, userPassword, it)
                    userDataStore.getDataStorePreferences().collect { userPreferences ->
                        if (userPreferences.biometricIntention) {
                            userDataStore.saveBiometricData(userEmail, userPassword)
                        }
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        val action =
                            LoginFragmentDirections.actionToMenuFragmentDestination(
                                user.name as String,
                                userEmail
                            )
                        findNavController().navigate(action)
                    }
                }
                    ?: lifecycleScope.launch(Dispatchers.Main) { showErrorDialog(R.string.invalid_email_or_password) }
            }
        }
        loginViewModel.status.observe(viewLifecycleOwner) { status ->
            println(status)
            when (status) {
                is ResponseStatus.Error -> {
                    binding.pbLogin.visibility = View.GONE
                    showErrorDialog(status.messageId)
                }
                is ResponseStatus.Loading -> binding.pbLogin.visibility = View.VISIBLE
                is ResponseStatus.Success -> binding.pbLogin.visibility = View.GONE
                else -> {}
            }
        }
    }

    private fun setListeners() {
        binding.btnLoginLogin.setOnClickListener {
            val email = binding.etvLoginEmail.text.toString()
            val password = binding.etvLoginPassword.text.toString()
            if (validateEmail(email) && validatePassword(password)) {
                loginViewModel.login(email, password)
            }
        }
        binding.btnLoginFingerprint.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun setupBiometricAccess() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    lifecycleScope.launch(Dispatchers.IO) {
                        userDataStore.getDataStorePreferences().collect() { userPreferences ->
                            if (!userPreferences.biometricIntention) userDataStore.setBiometricIntention()
                            if (userPreferences.biometricEmail.isEmpty()) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.auth_with_password_first),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                userDataStore.getDataStorePreferences().collect { userPreferences ->
                                    loginViewModel.login(
                                        userPreferences.biometricEmail,
                                        userPreferences.biometricPassword
                                    )
                                }
                            }
                        }
                    }
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.authentication_success), Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(), getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.fingerprint_auth))
            .setSubtitle(getString(R.string.use_biometric_as_authentication))
            .setNegativeButtonText(getString(R.string.use_password_auth))
            .build()
    }

    private fun validatePassword(password: String): Boolean {
        return if (Validation.isFieldEmpty(password)) {
            binding.tilLoginPassword.error = getString(R.string.password_not_empy)
            false
        } else {
            binding.tilLoginPassword.error = null
            true
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (Validation.isFieldEmpty(email)) {
            binding.tilLoginEmail.error = getString(R.string.email_not_empty)
            false
        } else if (!Validation.isEmailValid(email)) {
            binding.tilLoginEmail.error = getString(R.string.invalid_email)
            false
        } else {
            binding.tilLoginEmail.error = null
            true
        }
    }

    private fun showErrorDialog(messageId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_message)
            .setMessage(messageId)
            .setPositiveButton(android.R.string.ok) { _, _ -> /** Dissmiss dialog **/ }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}