package id.dwichan.myfirstfirebasedb

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_mahasiswa.view.*


class MahasiswaAdapter: RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder>() {

    private val mData = ArrayList<Mahasiswa>()

    fun setData(mahasiswa: ArrayList<Mahasiswa>) {
        this.mData.clear()
        this.mData.addAll(mahasiswa)
        notifyDataSetChanged()
    }

    class MahasiswaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(mahasiswa: Mahasiswa) {
            with(itemView) {
                nim.text = mahasiswa.nim
                nama.text = mahasiswa.nama
                jurusan.text = mahasiswa.jurusan

                setOnLongClickListener {
                    val action: Array<String> = arrayOf("Update", "Delete")
                    val alert = AlertDialog.Builder(context)
                    alert.setItems(action) { dialog, which ->
                        when (which) {
                            0 -> {
                                /*
                                  Berpindah Activity pada halaman layout updateData
                                  dan mengambil data pada listMahasiswa, berdasarkan posisinya
                                  untuk dikirim pada activity selanjutnya
                                */
                                val bundle = Bundle()
                                bundle.putString("dataNIM", mahasiswa.nim)
                                bundle.putString("dataNama", mahasiswa.nama)
                                bundle.putString("dataJurusan", mahasiswa.jurusan)
                                bundle.putString("getPrimaryKey", mahasiswa.key)
                                val intent = Intent(context, UpdateActivity::class.java)
                                intent.putExtras(bundle)
                                context.startActivity(intent)
                            }
                            1 -> {
                                val userId = FirebaseAuth.getInstance().uid!!
                                val reference = FirebaseDatabase.getInstance().reference
                                reference.child("Admin")
                                    .child(userId)
                                    .child("Mahasiswa")
                                    .child(mahasiswa.key!!)
                                    .removeValue()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "Berhasil menghapus data ${mahasiswa.nama}!!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Gagal hapus data!\nAlasan: ${it.message!!}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }
                    alert.create().show()
                    return@setOnLongClickListener true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mahasiswa, parent, false)
        return MahasiswaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MahasiswaViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size
}