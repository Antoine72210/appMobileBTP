package fr.antoine.maintenancecesi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import fr.antoine.maintenancecesi.fragments.AddTicketFragment
import fr.antoine.maintenancecesi.fragments.CollectionFragment
import fr.antoine.maintenancecesi.fragments.HomeFragment

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

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

        login()
    }

    @SuppressLint("WrongViewCast")
    private fun login() {

        val userNameInput = findViewById<EditText>(R.id.login_page_username)
        val passwordInput = findViewById<EditText>(R.id.login_page_password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerText = findViewById<TextView>(R.id.registerButton)

        loginButton.setOnClickListener {
            if (TextUtils.isEmpty(userNameInput.text.toString())) {
                userNameInput.setError("Veuillez renseigner votre nom d'utilisateur")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordInput.text.toString())) {
                passwordInput.setError("Veuillez renseigner votre mot de passe")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(userNameInput.text.toString(), passwordInput.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "La connexion a échoué, veuillez réessayer !", Toast.LENGTH_LONG).show()
                }
            }
        }

        registerText.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))

        }
    }
}