package com.example.ticketbooking.Activity

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
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
import com.eightbitlab.blurview.BlurView
import com.eightbitlab.blurview.RenderScriptBlur

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
            finish() // Exit the activity if no data is passed
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
        binding.buyTicketBtn.setOnClickListener{
            val intent = Intent(this,SeatListActivity::class.java)
            intent.putExtra("film",item)
            startActivity(intent)
        }

        val radius = 10f
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background

        var blurRadius = binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowsBackground)
            .setBlurRadius(radius)
        binding.blurView.clipToOutline = true

        item.Genre?.let { genres ->
            binding.genreView.adapter = CategoryEachFilmAdapter(genres)
            binding.genreView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        item.Casts?.let { casts ->
            binding.castListView.adapter = CategoryEachFilmAdapter(casts.map { it.toString() })
            binding.castListView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}
