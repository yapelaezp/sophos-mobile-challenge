package com.example.sophos_mobile_app.ui.login

import android.os.Bundle
import android.view.*
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sophos_mobile_app.databinding.FragmentLoginBinding
import com.example.sophos_mobile_app.utils.Validation
import com.example.sophos_mobile_app.utils.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    companion object {
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val NAME = "name"
        const val BIOMETRIC_EMAIL = "biometric_email"
        const val BIOMETRIC_PASSWORD = "biometric_password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataStorePreferences()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        isUserLogged()
        setListeners()
        observeViewModel()
        return binding.root
    }

    private fun isUserLogged() {
        lifecycleScope.launch(Dispatchers.IO) {
            getDataStorePreferences()?.collect() { userData ->
                println(userData)
                if (userData.first.isNotEmpty()) {
                    withContext(Dispatchers.Main){
                        val action = LoginFragmentDirections.actionToMenuFragmentDestination(userData.third, userData.first)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        val userEmail = binding.etvLoginEmail.text.toString()
        val userPassword = binding.etvLoginPassword.text.toString()
        loginViewModel.user.observe(viewLifecycleOwner) { user ->
            lifecycleScope.launch(Dispatchers.IO) {
                saveUserInDataStore(userEmail, userPassword, user.name)
            }
            val action =
                LoginFragmentDirections.actionToMenuFragmentDestination(user.name, userEmail)
            findNavController().navigate(action)
        }
    }

    private fun setListeners() {
        binding.btnLoginLogin.setOnClickListener {
            val email = binding.etvLoginEmail.text.toString()
            val password = binding.etvLoginPassword.text.toString()
            validateEmail(email)
            validatePassword(password)
            if (validateEmail(email) && validatePassword(password)) {
                loginViewModel.login(email, password)
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (Validation.isFieldEmpty(password)) {
            binding.tilLoginPassword.error = "Password can\'t be empty"
            false
        } else {
            binding.tilLoginPassword.error = null
            true
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (Validation.isFieldEmpty(email)) {
            binding.tilLoginEmail.error = "Email can\'t be empty"
            false
        } else if (!Validation.isEmailValid(email)) {
            binding.tilLoginEmail.error = "Invalid email"
            false
        } else {
            binding.tilLoginEmail.error = null
            true
        }
    }

    private fun getDataStorePreferences() = context?.dataStore?.data?.map { preferences ->
        Triple(
            preferences[stringPreferencesKey(EMAIL)].orEmpty(),
            preferences[stringPreferencesKey(PASSWORD)].orEmpty(),
            preferences[stringPreferencesKey(NAME)].orEmpty()
        )
    }

    private suspend fun saveUserInDataStore(email: String, password: String, name: String) {
        context?.dataStore?.edit { preferences ->
            preferences[stringPreferencesKey(EMAIL)] = email
            preferences[stringPreferencesKey(PASSWORD)] = password
            preferences[stringPreferencesKey(NAME)] = name
            preferences[stringPreferencesKey(BIOMETRIC_EMAIL)] = ""
            preferences[stringPreferencesKey(BIOMETRIC_PASSWORD)] = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}