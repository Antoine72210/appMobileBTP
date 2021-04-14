package fr.antoine.maintenancecesi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fr.antoine.maintenancecesi.fragments.AddTicketFragment
import fr.antoine.maintenancecesi.fragments.CollectionFragment
import fr.antoine.maintenancecesi.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        loadFragment(HomeFragment(this), R.string.home_page_title)

        // importer la bottomNavigationView
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.home_page -> {
                    loadFragment(HomeFragment(this), R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collection_page -> {
                    loadFragment(CollectionFragment(this), R.string.collection_page)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.add_ticket_page -> {
                    loadFragment(AddTicketFragment(this), R.string.add_page_ticket)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.login_page -> {
                    val currentUser: FirebaseUser = auth.currentUser
                    if (currentUser != null){
                        startActivity(Intent(this, ProfileActivity::class.java))
                        return@setOnNavigationItemSelectedListener true
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, string: Int) {
        // charger le TicketRepository
        val repo = TicketRepository()

        // actualiser le titre de la page
        findViewById<TextView>(R.id.page_title).text = resources.getString(string)

        // mettre a jour la liste de plantes
        repo.updateData {
            // Injecter le fragment Home dans notre boite (fragment_container)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
