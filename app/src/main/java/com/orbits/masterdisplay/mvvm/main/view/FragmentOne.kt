package com.orbits.masterdisplay.mvvm.main.view

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import com.orbits.masterdisplay.R
import com.orbits.masterdisplay.databinding.FragmentLogoBinding
import com.orbits.masterdisplay.databinding.FragmentOneBinding
import com.orbits.masterdisplay.helper.FileConfig.image_FilePaths
import com.orbits.masterdisplay.helper.FileConfig.readImageFile
import com.orbits.masterdisplay.helper.FileConfig.readVideoFile
import com.orbits.masterdisplay.helper.FileConfig.video_FilePaths

class FragmentOne : Fragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding : FragmentOneBinding
    private var pos = 0
    var currentVideoIndex: Int = 0
    var mc: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_one, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFields()
    }

    private fun initializeFields(){
        readVideoFile()
        println("here is video starting 000")
        if (video_FilePaths != null && !binding.videoPlayer.isPlaying) {
            println("here is video starting")
            currentVideoIndex = 0
            playVideo(currentVideoIndex)
        }
    }

    fun playVideo(index: Int) {
        try {
            mc?.setAnchorView(binding.videoPlayer)
            mc?.setMediaPlayer(binding.videoPlayer)

            // Check if the video file is of a valid format
            if (video_FilePaths?.get(index)?.endsWith("mp4") == true ||
                video_FilePaths?.get(index)?.endsWith("3gp") == true) {
                binding.videoPlayer.setVideoPath(video_FilePaths?.get(index))
            }

            binding.videoPlayer.requestFocus()
            binding.videoPlayer.start()

            // Set the OnPreparedListener to handle video preparation
            binding.videoPlayer.setOnPreparedListener { mp ->
                var mediaPlayer = mp
                try {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.stop()
                        mediaPlayer.release()
                        mediaPlayer = MediaPlayer()
                    }
                    mediaPlayer.isLooping = false
                    mediaPlayer.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // Set the OnCompletionListener to handle video completion
            binding.videoPlayer.setOnCompletionListener { player ->
                player.stop()

                // Play the next video file
                if (currentVideoIndex < (video_FilePaths!!.size - 1)) {
                    currentVideoIndex += 1
                    playVideo(currentVideoIndex)
                } else {
                    // All videos have been played, restart from the beginning
                    currentVideoIndex = 0
                    playVideo(currentVideoIndex)
                }
            }

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}