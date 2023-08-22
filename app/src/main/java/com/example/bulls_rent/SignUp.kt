package com.example.bulls_rent

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.bulls_rent.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var mBinding: ActivitySignUpBinding
    private lateinit var rootNode: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var registrationName: TextInputLayout
    private lateinit var registrationUsername: TextInputLayout
    private lateinit var registrationEmail: TextInputLayout
    private lateinit var registrationPhoneNo: TextInputLayout
    private lateinit var registrationPassword: TextInputLayout
    private lateinit var registrationButton: Button
    private lateinit var registrationToLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        registrationName = mBinding.name
        registrationUsername = mBinding.username
        registrationEmail = mBinding.email
        registrationPhoneNo = mBinding.phoneno
        registrationPassword = mBinding.password
        registrationButton = mBinding.registrationButton
        registrationToLoginButton = mBinding.registrationLoginButton

    }
    fun registerUser(view: View) {
        if (!validateName() or !validatePassword() or !validatePhoneNumber() or !validateEmail() or !validateUsername()) return

        FirebaseApp.initializeApp(this)
        rootNode =
            FirebaseDatabase.getInstance("https://bulls-rent-e716b-default-rtdb.europe-west1.firebasedatabase.app")
        databaseReference = rootNode.getReference("users")

        val name = registrationName.editText?.text.toString()
        val username = registrationUsername.editText?.text.toString()
        val email = registrationEmail.editText?.text.toString()
        val phoneNo = registrationPhoneNo.editText?.text.toString()
        val password = registrationPassword.editText?.text.toString()

        val user = UserHelper(name, username, email, phoneNo, password)

        databaseReference.child(username).setValue(user)

        val intent = Intent(applicationContext, UserProfile::class.java)
        intent.putExtra("name", name)
        intent.putExtra("username", username)
        intent.putExtra("phoneNo", phoneNo)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
    }

    private fun validateName(): Boolean {
        val name = registrationName.editText?.text.toString()
        return if (name.isEmpty()) {
            registrationName.error = "Field cannot be empty"
            false
        } else {
            registrationName.error = null
            registrationName.isErrorEnabled = false
            true
        }
    }

    private fun validateUsername(): Boolean {
        val username = registrationUsername.editText?.text.toString()
//        val noWhiteSpace = "^[\\S]{4,20}$"
        return if (username.isEmpty()) {
            registrationUsername.error = "Field cannot be empty"
            false
        } else if (username.length >= 15) {
            registrationUsername.error = "Username too long"
            false
//        } else if (username.matches(noWhiteSpace.toRegex()))  {
//            registrationUsername.error = "White spaces are not allowed"
//            false
        } else {
            registrationUsername.error = null
            registrationUsername.isErrorEnabled = false
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = registrationEmail.editText?.text.toString()
        return if (email.isEmpty()) {
            registrationEmail.error = "Field cannot be empty"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registrationEmail.error = "Invalid email address"
            false
        } else {
            registrationEmail.error = null
            registrationEmail.isErrorEnabled = false
            true
        }
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneNumber = registrationPhoneNo.editText?.text.toString()
        return if (phoneNumber.isEmpty()) {
            registrationPhoneNo.error = "Field cannot be empty"
            false
        } else {
            registrationPhoneNo.error = null
            registrationPhoneNo.isErrorEnabled = false
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = registrationPassword.editText?.text.toString()
        // password contains both letters and digits and has a minimum length of 4 characters.
        val passwordVal = "^(?=.*[a-zA-Z])(?=.*\\d).{4,}$"
        return if (password.isEmpty()) {
            registrationPassword.error = "Field cannot be empty"
            false
        } else if (!password.matches(passwordVal.toRegex())) {
            registrationPassword.error = "Password is too weak"
            false
        } else {
            registrationPassword.error = null
            registrationPassword.isErrorEnabled = false
            true
        }
    }
}