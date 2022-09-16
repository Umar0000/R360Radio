package com.r360.radioApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.material.navigation.NavigationView
import com.r360.radioApp.databinding.ActivityMain2Binding
import com.r360.radioApp.ui.service.MusicPlayerService
import com.r360.radioApp.utils.Constant
import com.r360.radioApp.utils.Constant.ACTION_ACTIVITY_TO_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity2 : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMain2Binding
    private lateinit var simpleExoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.appBarMain.contenMain.lotifile.pauseAnimation()
        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbar.outlineProvider = null

        val action = intent.action
        if(action.equals(ACTION_ACTIVITY_TO_FRAGMENT))
        {
            binding.appBarMain.contenMain.btnPlay.isVisible = false
            binding.appBarMain.contenMain.btnStop.isVisible = true
            binding.appBarMain.contenMain.btnPause.isVisible = true
            binding.appBarMain.contenMain.txt2.text ="Playing"
            binding.appBarMain.contenMain.txt2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_animation))

            binding.appBarMain.contenMain.lotifile.playAnimation()

        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
            drawerLayout.addDrawerListener(object:  DrawerLayout.DrawerListener{
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                }

                override fun onDrawerOpened(drawerView: View) {
                    binding.appBarMain.icBtn.visibility=View.GONE
                }

                override fun onDrawerClosed(drawerView: View) {
                    binding.appBarMain.icBtn.visibility=View.VISIBLE
                }

                override fun onDrawerStateChanged(newState: Int) {
                 
                }

            })
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.txtHome -> {

                    goToBrowser("https://www.r360radio.co.uk/")

                }
                R.id.schedule -> {

                    goToBrowser("https://www.r360radio.co.uk/schedules")

                }
                R.id.postCast -> {

                    goToBrowser("https://www.r360radio.co.uk/podcasts")

                }
                R.id.nav_setting -> {
                    goToBrowser("https://www.r360radio.co.uk/settings")

                }
                R.id.nav_contactUs -> {

                    goToBrowser("https://www.r360radio.co.uk/contact-us")

                }
                R.id.nav_shop -> {
                    goToBrowser("https://www.r360radio.co.uk/shop")
                }
                R.id.nav_PrivacyPolicy -> {
                    goToBrowser("https://www.r360radio.co.uk/privacy-policy")
                }
                R.id.nav_catchUp -> {
                    goToBrowser("https://www.r360radio.co.uk/catch-up")
                }
            }
            NavigationUI.onNavDestinationSelected(it, navController)
            drawerLayout.closeDrawer(GravityCompat.START)

            true
        }
        binding.appBarMain.icBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        prepareMediaPlayer()
        initListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity2, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun prepareMediaPlayer() {

        if(!MusicPlayerService.isServiceStarted)
        {

            val mediaDataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this)

            val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(RADIO_URL))
            val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)
            simpleExoPlayer = ExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()
            MusicPlayerService.simpleExoPlayer = simpleExoPlayer
            simpleExoPlayer.addMediaSource(mediaSource)
            simpleExoPlayer.prepare()
        }
        else
        {
            simpleExoPlayer = MusicPlayerService.simpleExoPlayer
        }
    }
    fun sendCommandService(action: String) =
        Intent(this, MusicPlayerService::class.java).also {
            it.action = action
            startService(it)
        }

    private fun initListeners() {


        binding.appBarMain.contenMain.btnPlay.setOnClickListener {
            prepareMediaPlayer()

            simpleExoPlayer.playWhenReady = true
            binding.appBarMain.contenMain.btnPlay.isVisible = false

            binding.appBarMain.contenMain.txt2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_animation))
            binding.appBarMain.contenMain.txt2.text="Playing.."
            binding.appBarMain.contenMain.btnStop.isVisible = true
            binding.appBarMain.contenMain.btnPause.isVisible = true
            binding.appBarMain.contenMain.lotifile.playAnimation()
            sendCommandService(Constant.ACTION_START_OR_RESUME_SERVICE)
        }

        binding.appBarMain.contenMain.btnPause.setOnClickListener {
            simpleExoPlayer.playWhenReady = false
            binding.appBarMain.contenMain.lotifile.pauseAnimation()
            sendCommandService(Constant.ACTION_STOP_SERVICE)
            binding.appBarMain.contenMain.txt2.text="Station Pause.."
            binding.appBarMain.contenMain.txt2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_animation))
            binding.appBarMain.contenMain.btnPause.isVisible = false
            binding.appBarMain.contenMain.btnStop.isVisible = true
            binding.appBarMain.contenMain.btnPlay.isVisible = true
        }




        binding.appBarMain.contenMain.btnStop.setOnClickListener {
            simpleExoPlayer.playWhenReady = false
            sendCommandService(Constant.ACTION_STOP_SERVICE)
            binding.appBarMain.contenMain.txt2.text="Station Stopped.."
            binding.appBarMain.contenMain.lotifile.pauseAnimation()
            binding.appBarMain.contenMain.txt2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.text_animation))
            binding.appBarMain.contenMain.btnStop.isVisible = false
            binding.appBarMain.contenMain.btnPause.isVisible = false
            binding.appBarMain.contenMain.btnPlay.isVisible = true
        }



        binding.appBarMain.contenMain.btnFacebook.setOnClickListener {
            Toast.makeText(applicationContext, "facebook", Toast.LENGTH_SHORT).show()
            goToBrowser("fb://profile/2044850612251819")
        }

        binding.appBarMain.contenMain.btnInstagram.setOnClickListener {
            Toast.makeText(applicationContext, "Instagram", Toast.LENGTH_SHORT).show()
            goToBrowser("https://www.instagram.com/r360radio")
        }

        binding.appBarMain.contenMain.btnYouTube.setOnClickListener {
            Toast.makeText(applicationContext, "Youtube", Toast.LENGTH_SHORT).show()
            goToBrowser("https://www.youtube.com/channel/UCFBXk9CkBUamZt5P_4QxumA")
        }

        binding.appBarMain.contenMain.btnTwitter.setOnClickListener {
            Toast.makeText(applicationContext, "Twitter", Toast.LENGTH_SHORT).show()
            goToBrowser("https://twitter.com/R360TV")
        }

        binding.appBarMain.contenMain.btnR360.setOnClickListener {
            Toast.makeText(applicationContext, "R360 Radio", Toast.LENGTH_SHORT).show()
            goToBrowser("https://www.r360radio.co.uk/")
        }

    }

    private fun goToBrowser(url: String) {
        val YourPageURL = url
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL))
        startActivity(browserIntent)
    }


    override fun onDestroy() {
//        simpleExoPlayer.playWhenReady = false
        super.onDestroy()
    }

    companion object {
        const val RADIO_URL =
            "https://s14.myradiostream.com/29856/listen.mp3"
    }


//    private fun View.fadeIn(durationMillis: Long = 250) {
//        this.startAnimation(AlphaAnimation(0F, 1F).apply {
//            duration = durationMillis
//            fillAfter = true
//        })
//    }
//
//    private fun View.fadeOut(durationMillis: Long = 250) {
//        this.startAnimation(AlphaAnimation(1F, 0F).apply {
//            duration = durationMillis
//            fillAfter = true
//        })
//    }
}