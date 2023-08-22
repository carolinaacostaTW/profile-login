package com.example.bulls_rent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.bulls_rent.databinding.ActivityUserProfileBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserProfile : AppCompatActivity() {

    private lateinit var mBinding: ActivityUserProfileBinding
    private lateinit var fullName: TextInputLayout
    private lateinit var emailLabel: TextInputLayout
    private lateinit var phoneNumber: TextInputLayout
    private lateinit var passwordLabel: TextInputLayout
    private lateinit var fullNameLabel: TextView
    private lateinit var usernameLabel: TextView

    private lateinit var reference: DatabaseReference

    //Global variables to hold user data from database
    private var USERNAME: String? = null
    private var FULL_NAME: String? = null
    private var PHONENO: String? = null
    private var EMAIL: String? = null
    private var PASSWORD: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val rootNode = FirebaseDatabase.getInstance("https://bulls-rent-e716b-default-rtdb.europe-west1.firebasedatabase.app")
        reference = rootNode.getReference("users")

        fullName = mBinding.fullName
        emailLabel = mBinding.email
        phoneNumber = mBinding.phoneNumber
        passwordLabel = mBinding.password
        fullNameLabel = mBinding.fullNameLabel
        usernameLabel = mBinding.usernameLabel

        showUserData()

    }

    private fun showUserData() {
        val intent = intent
        USERNAME = intent.getStringExtra("username")
        FULL_NAME = intent.getStringExtra("name")
        PHONENO = intent.getStringExtra("phoneNo")
        EMAIL = intent.getStringExtra("email")
        PASSWORD = intent.getStringExtra("password")

        fullNameLabel.text = FULL_NAME
        usernameLabel.text = USERNAME
        fullName.editText?.setText(FULL_NAME)
        phoneNumber.editText?.setText(PHONENO)
        emailLabel.editText?.setText(EMAIL)
        phoneNumber.editText?.setText(PHONENO)
        passwordLabel.editText?.setText(PASSWORD)
    }

    fun update(view: View) {
        if(isNameChanged() or isPasswordChanged() or isPhoneNoChanged() or isEmailChanged()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Data is same and cannot be updated", Toast.LENGTH_LONG).show()
        }
    }

    private fun isPasswordChanged(): Boolean {
        return if(!PASSWORD.equals(passwordLabel.editText?.text.toString())){
            reference.child(USERNAME!!).child("password").setValue(passwordLabel.editText?.text.toString())
            PASSWORD = passwordLabel.editText?.text.toString()
            true
        } else {
            false
        }
    }

    private fun isNameChanged(): Boolean {
        return if(!FULL_NAME.equals(fullName.editText?.text.toString())){
            reference.child(USERNAME!!).child("name").setValue(fullName.editText?.text.toString())
            FULL_NAME = fullName.editText?.text.toString()
            fullNameLabel.text = fullName.editText?.text.toString()
            true
        } else {
            false
        }
    }

    private fun isPhoneNoChanged(): Boolean {
        return if(!PHONENO.equals(phoneNumber.editText?.text.toString())){
            reference.child(USERNAME!!).child("phonCeNo").setValue(phoneNumber.editText?.text.toString())
            PHONENO = phoneNumber.editText?.text.toString()
            true
        } else {
            false
        }
    }

    private fun isEmailChanged(): Boolean {
        return if(!EMAIL.equals(emailLabel.editText?.text.toString())){
            reference.child(USERNAME!!).child("email").setValue(emailLabel.editText?.text.toString())
            EMAIL = emailLabel.editText?.text.toString()
            true
        } else {
            false
        }
    }
}