package id.dwichan.myfirstfirebasedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list_data.*

class ListDataActivity : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var adapter: MahasiswaAdapter = MahasiswaAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_data)

        supportActionBar?.title = "Data Mahasiswa"

        auth = FirebaseAuth.getInstance()

        datalist.layoutManager = LinearLayoutManager(this)
        datalist.adapter = adapter
        datalist.setHasFixedSize(true)

        // garis pembatas
        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(applicationContext, R.drawable.line)?.let {
            itemDecor.setDrawable(
                it
            )
        }
        datalist.addItemDecoration(itemDecor)

        // get firebase data
        reference = FirebaseDatabase.getInstance().reference
        reference.child("Admin")
            .child(auth.uid!!)
            .child("Mahasiswa")

            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dataMahasiswa = ArrayList<Mahasiswa>()

                    for (snap in snapshot.children) {
                        // mapping data pada DataSnapshot ke dalam object mahasiswa
                        val mahasiswa: Mahasiswa = snap.getValue(Mahasiswa::class.java)!!

                        // mengambil primary key
                        mahasiswa.key = snap.key
                        dataMahasiswa.add(mahasiswa)
                    }

                    adapter.setData(dataMahasiswa)
                    Toast.makeText(this@ListDataActivity, "Data Berhasi di load", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ListDataActivity, "Gagal load!\n${error.details}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}