package id.dwichan.myfirstfirebasedb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    companion object {
        const val RC_SIGN_IN = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance() // ambil instance Firebase Auth

        logout.setOnClickListener(this)
        login.setOnClickListener(this)
        showdata.setOnClickListener(this)
        save.setOnClickListener(this)

        /**
         * Mendeteksi apakah ada user masuk. Jika tidak, maka setiap komponen UI dimatikan kecuali
         * tombol Login.
         */
        if (auth.currentUser == null) {
            disableUi()
            Toast.makeText(this, "User belum masuk!", Toast.LENGTH_SHORT).show()
        } else {
            enableUi()

            // Selamat datang, ambil data user!
            val user = auth.currentUser!!
            Toast.makeText(this, "Selamat datang, ${user.displayName}!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun disableUi() {
        logout.isEnabled = false
        login.isEnabled = true
        save.isEnabled = false
        showdata.isEnabled = false
        nim.isEnabled = false
        nama.isEnabled = false
        jurusan.isEnabled = false
    }

    private fun enableUi() {
        logout.isEnabled = true
        login.isEnabled = false
        save.isEnabled = true
        showdata.isEnabled = true
        nim.isEnabled = true
        nama.isEnabled = true
        jurusan.isEnabled = true
        progress.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login -> {
                // Login Methods
                val providers = ArrayList<AuthUI.IdpConfig>()
                providers.add(AuthUI.IdpConfig.GoogleBuilder().build())
                //providers.add(AuthUI.IdpConfig.FacebookBuilder().build()) // butuh dependency: com.facebook.android
                //providers.add(AuthUI.IdpConfig.TwitterBuilder().build()) // butuh dependency: com.twitter.sdk.android

                val authUi = AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(true) // menyimpan kredensial pengguna dan memasukkan pengguna ke aplikasi secara otomatis (if true).
                val i = authUi.build()

                startActivityForResult(i, RC_SIGN_IN)
                progress.visibility = View.VISIBLE
            }
            R.id.logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Logout berhasil!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
            R.id.save -> {
                val userId = auth.currentUser?.uid // <-- mengambil User ID pengguna

                // ambil instance Firebase Realtime DB
                val database = FirebaseDatabase.getInstance()
                val reference = database.reference

                val mNim = nim.text.toString()
                val mNama = nama.text.toString()
                val mJurusan = jurusan.text.toString()

                if (mNim.isEmpty() || mNama.isEmpty() || mJurusan.isEmpty()) {
                    Toast.makeText(this, "Data tidak boleh ada yg kosong!", Toast.LENGTH_SHORT).show()
                } else {
                    val mahasiswa = Mahasiswa()
                    mahasiswa.nim = mNim
                    mahasiswa.nama = mNama
                    mahasiswa.jurusan = mJurusan
                    // simpan data ke database!
                    reference.child("Admin")
                        .child(userId!!)
                        .child("Mahasiswa")
                        .push()
                        .setValue(mahasiswa)
                        .addOnSuccessListener {
                            nim.setText("")
                            nama.setText("")
                            jurusan.setText("")

                            Toast.makeText(this, "Berhasil input data!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal input data!\nAlasan: ${it.message!!}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.showdata -> {
                /* no-op */
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Berhasil masuk!", Toast.LENGTH_SHORT).show()
                enableUi()
            } else {
                Toast.makeText(this, "Gagal masuk!", Toast.LENGTH_SHORT).show()
                disableUi()
            }
        }
    }
}