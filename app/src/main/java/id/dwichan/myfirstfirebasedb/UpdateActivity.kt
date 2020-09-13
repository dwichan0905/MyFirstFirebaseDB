package id.dwichan.myfirstfirebasedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val bundle = intent.extras!!
        val mNim = bundle.getString("dataNIM")
        val mNama = bundle.getString("dataNama")
        val mJurusan = bundle.getString("dataJurusan")
        val mKey = bundle.getString("getPrimaryKey")!!
        new_nim.setText(mNim)
        new_nama.setText(mNama)
        new_jurusan.setText(mJurusan)

        update.setOnClickListener {
            val cekNim = new_nim.text.toString()
            val cekNama = new_nama.text.toString()
            val cekJurusan = new_jurusan.text.toString()

            if (cekNim.isEmpty() || cekNama.isEmpty() || cekJurusan.isEmpty()) {
                Toast.makeText(this, "Data dilarang kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val mahasiswa = Mahasiswa()

                mahasiswa.nim = cekNim
                mahasiswa.nama = cekNama
                mahasiswa.jurusan = cekJurusan

                // Update data!
                val userId = auth.uid!!
                database.child("Admin")
                    .child(userId)
                    .child("Mahasiswa")
                    .child(mKey)
                    .setValue(mahasiswa)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data Berhasil diubah", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal update data!\nAlasan: ${it.message!!}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}