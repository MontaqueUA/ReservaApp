package com.example.reservabuses.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ReservaBuses.CredentialsManager

import com.example.reservabuses.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCredentials()
    }

    override fun onStart() {
        super.onStart()
        setCredentials()
    }

    private fun setCredentials() {
        val credentials = CredentialsManager.getInstance(context!!).loadUser()
        val prependPlaceholder = getString(R.string.home_fragment_welcome_prepend)
        val welcomeMessage = "$prependPlaceholder ${credentials!!.first}"
        welcomeMessageTextView.text = welcomeMessage
    }
}