package id.dwichan.myfirstfirebasedb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            with (itemView) {
                nim.text = mahasiswa.nim
                nama.text = mahasiswa.nama
                jurusan.text = mahasiswa.jurusan

                setOnLongClickListener {
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