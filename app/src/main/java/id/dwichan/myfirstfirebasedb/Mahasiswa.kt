package id.dwichan.myfirstfirebasedb

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mahasiswa(
    var nim: String? = null,
    var nama: String? = null,
    var jurusan: String? = null,
    var key: String? = null
) : Parcelable