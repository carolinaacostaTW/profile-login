package com.example.bulls_rent

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.example.bulls_rent.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var logoImage: ImageView
    private lateinit var logoName: TextView
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setFullScreen()

        logoImage = mBinding.logoImage
        logoName = mBinding.logoName
        usernameLayout = mBinding.username
        passwordLayout = mBinding.password

        mBinding.signupButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            val pairs = android.util.Pair<View, String>(logoImage, "logo_image_animated"); Pair<View, String>(logoName, "logo_text_animated")
            val options = ActivityOptions.makeSceneTransitionAnimation(this, pairs)
            startActivity(intent, options.toBundle())
        }
    }

    fun loginUser(view: View){
        if(!validateUsername() || !validatePassword()){
            return
        }
        else {
            isUser()
        }
    }

    private fun isUser() {
        val userEnteredUsername = usernameLayout.editText?.text.toString().trim()
        val userEnteredPassword = passwordLayout.editText?.text.toString().trim()

        val rootNode = FirebaseDatabase.getInstance("https://bulls-rent-e716b-default-rtdb.europe-west1.firebasedatabase.app")
        val reference = rootNode.getReference("users")

        val checkUser = reference.orderByChild("username").equalTo(userEnteredUsername)

        checkUser.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    usernameLayout.error = null
                    usernameLayout.isErrorEnabled = false
                    val passwordFromDatabase = snapshot.child(userEnteredUsername).child("password").value.toString()
                    if(passwordFromDatabase.equals(userEnteredPassword)){

                        usernameLayout.error = null
                        usernameLayout.isErrorEnabled = false

                        val nameFromDatabase = snapshot.child(userEnteredUsername).child("name").value.toString()
                        val usernameFromDatabase = snapshot.child(userEnteredUsername).child("username").value.toString()
                        val phoneNumberFromDatabase = snapshot.child(userEnteredUsername).child("phoneNo").value.toString()
                        val emailFromDatabase = snapshot.child(userEnteredUsername).child("email").value.toString()

                        val intent = Intent(applicationContext, UserProfile::class.java)
                        intent.putExtra("name", nameFromDatabase)
                        intent.putExtra("username", usernameFromDatabase)
                        intent.putExtra("phoneNo", phoneNumberFromDatabase)
                        intent.putExtra("email", emailFromDatabase)
                        intent.putExtra("password", passwordFromDatabase)
                        startActivity(intent)
                    } else{
                        passwordLayout.error = "Wrong Password"
                        passwordLayout.requestFocus()
                    }
                } else {
                    usernameLayout.error = "User does not exist"
                    usernameLayout.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        )
    }

    private fun validatePassword(): Boolean {
        val password = passwordLayout.editText?.text.toString()
        return if (password.isEmpty()) {
            passwordLayout.error = "Field cannot be empty"
            false
        } else {
            passwordLayout.error = null
            passwordLayout.isErrorEnabled = false
            true
        }
    }

    private fun validateUsername(): Boolean {
        val username = usernameLayout.editText?.text.toString()
        return if (username.isEmpty()) {
            usernameLayout.error = "Username cannot be empty"
            false
        } else {
            usernameLayout.error = null
            usernameLayout.isErrorEnabled = false
            true
        }
    }

    private fun setFullScreen(){
        //You also have to go to themes and change to No Action bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}