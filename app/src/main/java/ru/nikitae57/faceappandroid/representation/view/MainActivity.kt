package ru.nikitae57.faceappandroid.representation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esafirm.imagepicker.features.registerImagePicker
import dagger.hilt.android.AndroidEntryPoint
import ru.nikitae57.faceappandroid.R
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ImageChoiceFragment.newInstance())
                    .commitNow()
        }
    }
}