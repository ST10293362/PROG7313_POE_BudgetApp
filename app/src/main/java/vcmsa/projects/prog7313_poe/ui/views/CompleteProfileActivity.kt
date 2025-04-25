package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import vcmsa.projects.prog7313_poe.R

class CompleteProfileActivity : AppCompatActivity() {
    private lateinit var accountEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var cardNumberEditText: EditText
    private lateinit var cardTypeSpinner: Spinner
    private lateinit var cvcEditText: EditText
    private lateinit var expiryEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var profileImageView: ImageView
    private lateinit var addProfilePicButton: Button

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        accountEditText = findViewById(R.id.accountEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        cardNumberEditText = findViewById(R.id.cardNumberEditText)
        cardTypeSpinner = findViewById(R.id.cardTypeSpinner)
        cvcEditText = findViewById(R.id.cvcEditText)
        expiryEditText = findViewById(R.id.expiryEditText)
        saveButton = findViewById(R.id.saveButton)
        profileImageView = findViewById(R.id.profileImageView)
        addProfilePicButton = findViewById(R.id.addProfilePicButton)

        // Set up the spinner with card types
        val cardTypes = arrayOf("Savings Account", "Cheque", "Debit")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cardTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        cardTypeSpinner.adapter = adapter

        addProfilePicButton.setOnClickListener {
            openGallery()
        }

        saveButton.setOnClickListener {
            // Logic to save user profile details including card information
            startActivity(Intent(this, ManageCategoriesActivity::class.java))
            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            // Use Glide to load the image and apply a circular crop transformation
            Glide.with(this)
                .load(imageUri)
                .transform(CircleCrop()) // This will make the image circular
                .into(profileImageView)
        }
    }
}