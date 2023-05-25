package com.zariya.zariya.auth.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.zariya.zariya.R
import com.zariya.zariya.auth.presentation.viewmodel.AuthViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentLoginBinding
import com.zariya.zariya.utils.AppUtil
import com.zariya.zariya.utils.RC_SIGN_IN
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel by viewModels<AuthViewModel>()

    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGoogleClient()
        setUpListeners()
    }

    private fun setUpListeners() {
        uiEventListener()

        binding.btnLogin.setOnClickListener {
            if (binding.btnLogin.text.equals("Verify OTP")) {
                val credential = PhoneAuthProvider.getCredential(
                    storedVerificationId, binding.tilOTP.editText?.text.toString()
                )
                Log.d("LoginFragment", "login OTP entered")
                signInWithPhoneAuthCredential(credential)
            } else {
                if (validate()) {
                    initiateOTPLogin()
                }
            }
        }

        binding.btnFacebook.setOnClickListener {
            initialiseFacebookLogin()
        }

        binding.btnGoogle.setOnClickListener {
            initiateSignInWithGoogle()
        }
    }

    private fun initiateOTPLogin() {
        val phoneNumber =
            "${binding.countryCodePicker.selectedCountryCodeWithPlus}${binding.tilPhone.editText?.text.toString()}}"
        activity?.let {
            PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions.newBuilder()
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(it)
                    .setCallbacks(callbacks)
                    .build()
            )
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("LoginFragment", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w("LoginFragment", "onVerificationFailed", e)
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {}
                is FirebaseTooManyRequestsException -> {}
                is FirebaseAuthMissingActivityForRecaptchaException -> {}
            }
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d("LoginFragment", "onCodeSent:$verificationId")
            storedVerificationId = verificationId
            resendToken = token
            showOTPUi(true)
        }
    }

    private fun showOTPUi(show: Boolean) {
        binding.apply {
            AppUtil.setVisibility(show, tilOTP)
            AppUtil.setVisibility(show.not(), btnGoogle, btnFacebook, tvOr, viewOrLeft, viewOrRight)
            if (show) {
                btnLogin.text = "Verify OTP"
            } else {
                btnLogin.text = "GET OTP"
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        authViewModel.authenticateWithPhone(credential)
    }

    private fun uiEventListener() {
        authViewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UIEvents.Loading -> {
                    // Handle Loading
                }

                is UIEvents.ShowError -> {
                    Toast.makeText(
                        context,
                        uiEvent.message ?: "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is UIEvents.Navigate -> {
                    uiEvent.navDirections?.let {
                        Navigation.findNavController(binding.root).navigate(
                            it
                        )
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (binding.countryCodePicker.selectedCountryCode.isNullOrEmpty()) {
            binding.tilPhone.error = getString(R.string.validation_select_country)
            return false
        }
        binding.tilPhone.error = ""

        if (binding.tilPhone.editText?.text?.isEmpty() == true) {
            binding.tilPhone.error = getString(R.string.validation_empty_phone)
            return false
        }
        binding.tilPhone.error = ""

        if (binding.tilPhone.editText?.text?.toString()?.length != 10) {
            binding.tilPhone.error = getString(R.string.validation_invalid_phone)
            return false
        }
        binding.tilPhone.error = ""

        return true
    }

    private fun initGoogleClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        activity?.let { googleSignInClient = GoogleSignIn.getClient(it, googleSignInOptions) }
    }

    private fun initiateSignInWithGoogle() {
        if (::googleSignInClient.isInitialized) {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        } else {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    private fun signInWithGoogleAuthCredential(credential: AuthCredential) {
        authViewModel.authenticateWithGoogle(credential)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val googleTokenId = account.idToken
                    val credential = GoogleAuthProvider.getCredential(googleTokenId, null)
                    signInWithGoogleAuthCredential(credential)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        } else {
            if (::callbackManager.isInitialized) {
                callbackManager.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun initialiseFacebookLogin() {
        callbackManager = CallbackManager.Factory.create()
        activity?.let {
            LoginManager.getInstance()
                .logInWithReadPermissions(it, listOf("email", "public_profile"))
        }
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onCancel() {}

                override fun onError(error: FacebookException) {
                    Log.e("LoginFragment", "facebook login error $error")
                }

                override fun onSuccess(result: LoginResult) {
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    signInWithFacebookCredential(credential)
                }
            })
    }

    private fun signInWithFacebookCredential(credential: AuthCredential) {
        authViewModel.authenticateWithFacebook(credential)
    }
}