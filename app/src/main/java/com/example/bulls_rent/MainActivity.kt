package com.example.bulls_rent

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.bulls_rent.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var topAnimation: Animation
    private lateinit var bottomAnimation: Animation
    private lateinit var logoImage: ImageView
    private lateinit var logoText: TextView
    private lateinit var slogan: TextView
    private lateinit var username: TextInputLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setFullScreen()

        setAnimations()

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

    private fun setAnimations(){
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        logoImage = mBinding.logoImage
        logoText = mBinding.logo
        slogan = mBinding.slogan

        logoImage.animation = topAnimation
        logoText.animation = bottomAnimation
        slogan.animation = bottomAnimation

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login::class.java)
            var pairs = android.util.Pair<View, String>(logoImage, "logo_image_animated");Pair<View, String>(logoText, "logo_text_animated"); Pair<View, String>(slogan, "logo_desc")
            val options = ActivityOptions.makeSceneTransitionAnimation(this, pairs)
            startActivity(intent, options.toBundle())
        }, SPLASH_SCREEN)
    }

    companion object {
        private const val SPLASH_SCREEN: Long = 5000
    }
}