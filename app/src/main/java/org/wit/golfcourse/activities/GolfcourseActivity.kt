package org.wit.golfcourse.activities


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.golfcourse.R
import org.wit.golfcourse.databinding.ActivityGolfcourseBinding
import org.wit.golfcourse.helpers.showImagePicker
import org.wit.golfcourse.main.MainApp
import org.wit.golfcourse.models.Location
import org.wit.golfcourse.models.GolfcourseModel
import timber.log.Timber.Forest.i

class GolfcourseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGolfcourseBinding
    var golfcourse = GolfcourseModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        edit = false

        binding = ActivityGolfcourseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Golfcourse Activity started...")

        if (intent.hasExtra("golfcourse_edit")) {
            edit = true
            golfcourse = intent.extras?.getParcelable("golfcourse_edit")!!
            binding.golfcourseTitle.setText(golfcourse.title)
            binding.description.setText(golfcourse.description)
            binding.dateplayed.setText(golfcourse.dateplayed)
            binding.price.setText(golfcourse.price)
            binding.ratingBar.rating = golfcourse.rating
            binding.btnAdd.setText(R.string.save_golfcourse)
            Picasso.get()
                .load(golfcourse.image)
                .into(binding.golfcourseImage)
            if (golfcourse.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_golfcourse_image)
            }

        }

        binding.btnAdd.setOnClickListener() {
            golfcourse.title = binding.golfcourseTitle.text.toString()
            golfcourse.description = binding.description.text.toString()
            golfcourse.dateplayed = binding.dateplayed.text.toString()
            golfcourse.price = binding.price.text.toString()
            golfcourse.rating = binding.ratingBar.rating
            if (golfcourse.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_golfcourse_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.golfcourses.update(golfcourse.copy())
                } else {
                    app.golfcourses.create(golfcourse.copy())
                }
            }
            i("add Button Pressed: $golfcourse")

            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }

        binding.golfcourseLocation.setOnClickListener {
            val location = Location(51.8985, -8.4756, 13f)
            if (golfcourse.zoom != 0f) {
                location.lat =  golfcourse.lat
                location.lng = golfcourse.lng
                location.zoom = golfcourse.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_golfcourse, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.golfcourses.delete(golfcourse)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            golfcourse.image = image

                            Picasso.get()
                                .load(golfcourse.image)
                                .into(binding.golfcourseImage)
                            binding.chooseImage.setText(R.string.change_golfcourse_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            golfcourse.lat = location.lat
                            golfcourse.lng = location.lng
                            golfcourse.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
