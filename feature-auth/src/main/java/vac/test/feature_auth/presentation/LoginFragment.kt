package vac.test.feature_auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vac.test.core_api.Token
import vac.test.core_di.AuthInjector
import vac.test.feature_auth.databinding.LoginFragmentBinding
import vac.test.feature_navigation.navigateToParent

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding

    private val viewModel by viewModels<LoginViewModel> {
        val authDi = requireContext().applicationContext as AuthInjector
        LoginViewModelFactory(authDi.getAuthDiComponent())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setFlowCollectors()
    }

    private fun setFlowCollectors() {
        viewModel.stateFlow.onEach { handleState(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.errorFlow.onEach { handleError(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.accessTokenFlow.onEach { handleAccessToken(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setClickListeners() {
        with(binding) {
            firstNameInputText.addTextChangedListener { clearAlarm() }
            passwordInputText.addTextChangedListener { clearAlarm() }
            loginButton.setOnClickListener { login() }
        }
    }

    private fun handleState(state: State) {
        when (state) {
            State.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            State.Complete -> {
                binding.progressBar.visibility = View.GONE
            }

            else -> Unit
        }
    }

    private fun handleError(error: State.Error) {
        with(binding) {
            errorTextView.text = error.message
            errorTextView.isVisible = error.message != null
        }
    }

    private fun handleAccessToken(token: Token) {
        viewModel.saveToken(token)
        navigateToParent(vac.test.feature_navigation.R.id.action_loginFragment_to_mapsFragment)
    }

    private fun login() {
        clearAlarm()
        with(binding) {
            viewModel.login(firstNameInputText.text.toString(), passwordInputText.text.toString())
        }
    }

    private fun clearAlarm() {
        with(binding) {
            errorTextView.text = null
            errorTextView.isVisible = false
        }
    }
}