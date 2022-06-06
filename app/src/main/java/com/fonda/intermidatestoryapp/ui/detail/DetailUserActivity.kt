package com.fonda.intermidatestoryapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fonda.intermidatestoryapp.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_CREATEDAT = "extra_createdat"
    }

    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "User Detail"

        val name = intent.getStringExtra(EXTRA_NAME)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val createdAt = intent.getStringExtra(EXTRA_CREATEDAT)

        binding.apply {
            Glide.with(this@DetailUserActivity)
                .load(avatarUrl)
                .into(imgItemPhoto)
            tvItemName.text = name
            tvItemDescription.text = description
            tvCreatedate.text = createdAt
        }

    }
}

