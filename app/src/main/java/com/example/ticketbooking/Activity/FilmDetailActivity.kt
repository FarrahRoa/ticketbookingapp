package com.example.ticketbooking.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.ticketbooking.adapter.CategoryEachFilmAdapter
import com.example.ticketbooking.Models.Film
import com.example.ticketbookingapp.R
import com.example.ticketbookingapp.databinding.ActivityFilmDetailBinding
import android.graphics.RenderEffect
import android.graphics.Shader
import android.view.View

class FilmDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilmDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setVariable()
    }

    private fun setVariable() {
        val item: Film? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("object", Film::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("object")
        }

        if (item == null) {
            Toast.makeText(this, "Film details not available", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val requestOptions = RequestOptions()
            .transform(CenterCrop(), GranularRoundedCorners(0f, 0f, 50f, 50f))

        Glide.with(this)
            .load(item.Poster)
            .apply(requestOptions)
            .into(binding.filmPic)

        binding.titleTxt.text = item.Title
        binding.imdbTxt.text = "IMDB ${item.Imdb}"
        binding.movieTimeTxt.text = "${item.Year} - ${item.Time}"
        binding.movieSummaryTxt.text = item.Description

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.buyTicketBtn.setOnClickListener {
            val intent = Intent(this, SeatListActivity::class.java)
            intent.putExtra("film", item)
            startActivity(intent)
        }

        // Apply blur effect based on device version
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Use RenderEffect for Android 12+ devices
            val blurEffect = RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.CLAMP)
            binding.blurContainer.setRenderEffect(blurEffect)
        } else {
            // Fallback for older devices
            binding.blurContainer.background = windowsBackground
        }

        binding.blurContainer.clipToOutline = true

        // Genre List Setup
        item.Genre?.let { genres ->
            binding.genreView.adapter = CategoryEachFilmAdapter(genres)
            binding.genreView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        // Cast List Setup
        item.Casts?.let { casts ->
            binding.castListView.adapter = CategoryEachFilmAdapter(casts.map { it.toString() })
            binding.castListView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
