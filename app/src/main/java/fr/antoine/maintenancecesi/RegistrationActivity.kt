package fr.antoine.maintenancecesi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference : DatabaseReference? = null
    var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

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

        register()
    }

    private fun register() {
        val firstNameInput = findViewById<EditText>(R.id.register_page_firstname)
        val lastNameInput = findViewById<EditText>(R.id.register_page_lastname)
        val userNameInput = findViewById<EditText>(R.id.register_page_email)
        val passwordInput = findViewById<EditText>(R.id.register_page_password)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {

            if (TextUtils.isEmpty(firstNameInput.text.toString())) {
                firstNameInput.setError("Veuillez renseigner votre prénom")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(lastNameInput.text.toString())) {
                lastNameInput.setError("Veuillez renseigner votre nom")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(userNameInput.text.toString())) {
                userNameInput.setError("Veuillez renseigner votre adresse mail")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordInput.text.toString())) {
                passwordInput.setError("Veuillez renseigner votre mot de passe")
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(userNameInput.text.toString(), passwordInput.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = auth.currentUser
                    val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                    currentUserDb?.child("firstname")?.setValue(firstNameInput.text.toString())
                    currentUserDb?.child("lastname")?.setValue(lastNameInput.text.toString())
                    currentUserDb?.child("email")?.setValue(userNameInput.text.toString())

                    Toast.makeText(this, "L'inscription a été validée !", Toast.LENGTH_LONG).show()
                    finish()

                } else {
                    Toast.makeText(this, "L'inscription a échoué, veuillez réessayer !", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}