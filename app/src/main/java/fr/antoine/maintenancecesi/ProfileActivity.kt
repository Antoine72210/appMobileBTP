package fr.antoine.maintenancecesi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profiles")

        // importer la bottomNavigationView
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.home_page -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.login_page -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        loadProfile()
    }

    private fun loadProfile() {
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)
        val firstNameText = findViewById<TextView>(R.id.firstnameText)
        val lastNameText = findViewById<TextView>(R.id.lastnameText)
        val emailText = findViewById<TextView>(R.id.emailText)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        emailText.text = "Adresse mail --> " + user?.email


        userReference?.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {

                firstNameText.text = "Votre Prénom : " + snapshot.child("lastname").value.toString()
                lastNameText.text = "Votre Nom : " + snapshot.child("firstname").value.toString()
                emailText.text = "Votre Adresse mail : " + snapshot.child("email").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}