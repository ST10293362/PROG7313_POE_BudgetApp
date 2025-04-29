package vcmsa.projects.prog7313_poe.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.prog7313_poe.R

class ProfileActivity : AppCompatActivity() {
    private lateinit var homeButton: ImageView
    private lateinit var profileButton: ImageView
    private lateinit var transactionsButton: ImageView
    private lateinit var statsButton: ImageView
    private lateinit var profileOptionsList: RecyclerView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        homeButton = findViewById(R.id.homeButton)
        profileButton = findViewById(R.id.profileButton)
        transactionsButton = findViewById(R.id.transactionsButton)
        statsButton = findViewById(R.id.statsButton)
        profileOptionsList = findViewById(R.id.profileOptionsList)
        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)

        // Set up navigation buttons
        setupNavigationButtons()

        // Set up profile options list
        setupProfileOptions()

        // Load user data
        loadUserData()
    }

    private fun setupNavigationButtons() {
        homeButton.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        profileButton.setOnClickListener {
            // Already in profile activity
        }

        transactionsButton.setOnClickListener {
            startActivity(Intent(this, ViewExpensesActivity::class.java))
            finish()
        }

        statsButton.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
            finish()
        }
    }

    private fun setupProfileOptions() {
        val options = listOf(
            ProfileOption("Personal Information", R.drawable.person_24px),
            ProfileOption("Security Settings", R.drawable.security_24px),
            ProfileOption("Notification Preferences", R.drawable.notifications_24px),
            ProfileOption("Help & Support", R.drawable.help_24px),
            ProfileOption("About", R.drawable.info_24px),
            ProfileOption("Logout", R.drawable.logout_24px)
        )

        profileOptionsList.layoutManager = LinearLayoutManager(this)
        profileOptionsList.adapter = ProfileOptionsAdapter(options) { option ->
            when (option.title) {
                "Personal Information" -> {
                    startActivity(Intent(this, PersonalInfoActivity::class.java))
                }
                "Security Settings" -> {
                    // TODO: Implement Security Settings
                    Toast.makeText(this, "Security Settings coming soon", Toast.LENGTH_SHORT).show()
                }
                "Notification Preferences" -> {
                    // TODO: Implement Notification Preferences
                    Toast.makeText(this, "Notification Preferences coming soon", Toast.LENGTH_SHORT).show()
                }
                "Help & Support" -> {
                    // TODO: Implement Help & Support
                    Toast.makeText(this, "Help & Support coming soon", Toast.LENGTH_SHORT).show()
                }
                "About" -> {
                    // TODO: Implement About
                    Toast.makeText(this, "About coming soon", Toast.LENGTH_SHORT).show()
                }
                "Logout" -> {
                    // Handle logout
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun loadUserData() {
        // TODO: Load user data from SharedPreferences or database
        profileName.text = "John Doe"
        profileEmail.text = "john.doe@example.com"
    }
}

data class ProfileOption(
    val title: String,
    val iconResId: Int
)

class ProfileOptionsAdapter(
    private val options: List<ProfileOption>,
    private val onItemClick: (ProfileOption) -> Unit
) : RecyclerView.Adapter<ProfileOptionsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val optionIcon: ImageView = view.findViewById(R.id.optionIcon)
        val optionTitle: TextView = view.findViewById(R.id.optionTitle)
        val arrowIcon: ImageView = view.findViewById(R.id.arrowIcon)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.optionIcon.setImageResource(option.iconResId)
        holder.optionTitle.text = option.title
        
        holder.itemView.setOnClickListener {
            onItemClick(option)
        }
    }

    override fun getItemCount() = options.size
} 