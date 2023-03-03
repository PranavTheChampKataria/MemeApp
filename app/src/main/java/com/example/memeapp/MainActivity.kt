package com.example.memeapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.android.volley.Request
import com.android.volley.Request.*
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeapp.databinding.ActivityMainBinding
import com.example.memeapp.databinding.ActivityMainBinding.*
import java.lang.Math.abs


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    lateinit var gestureDetector: GestureDetector //Define gesture detector object
    // for measuring swipe distance
    var x1:Float=0.0f
    var x2:Float=0.0f
    var y1:Float=0.0f
    var y2:Float=0.0f
    // min distance for comparison with swipe distance
    companion object{
        const val MIN_DISTANCE=130
    }

    var currentMemeUrl: String? = null

     private lateinit var binding: ActivityMainBinding //instance of ActivityMainBinding to communicate with GUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= inflate(layoutInflater)
        val view = binding.root //access GUI components
        setContentView(view)

        // edge-to-edge screen
        WindowCompat.setDecorFitsSystemWindows(window, false)

        gestureDetector= GestureDetector(this,this) //calling gesture
        loadMeme() //calling loadmeme

        binding.button.setBackgroundColor(Color.BLACK)   //button color

        binding.button.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND) //creating intent
            i.type = "text/plain"  //defining type of data to be sent
            i.putExtra(Intent.EXTRA_TEXT, "Hi, checkout this meme $currentMemeUrl")  //shown on send
            startActivity(Intent.createChooser(i, "Share this meme with"))
        }
    }

   private fun loadMeme(){
        binding.progressBar.visibility = View.VISIBLE  //making progressbar visible
        // Instantiate the RequestQueue.

        val url = "https://meme-api.herokuapp.com/gimme"

// Request a json response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest (
            Method.GET, url,null,
            { response ->
            val url = response.getString("url")
                Glide.with(this).load(url).listener(object :RequestListener<Drawable>{ //show progressbar till glide load next meme
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                }).into(binding.imageView)
            },
            {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //when we start to swipe
        when(event?.action) {
            0 -> {
                x1 = event.x
                y1 = event.y
            }

            //when we end to swipe
            1 -> {
                x2 = event.x
                y2 = event.y

                val valueX:Float=x2-x1
                val valueY:Float=y2-y1

                if(abs(valueX)> MIN_DISTANCE){
                    if(x1>x2){  //left swipe
                        loadMeme()
                    }
                    else{ //right swipe
                        Toast.makeText(this,"Can't go back",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    //just for implementation; no use of them rn
    override fun onDown(e: MotionEvent?): Boolean {
//        TODO("Not yet implemented")
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
//        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
//        TODO("Not yet implemented")
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
//        TODO("Not yet implemented")
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
//        TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
//        TODO("Not yet implemented")
        return false
    }

}